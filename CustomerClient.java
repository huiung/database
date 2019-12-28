import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
 
 
public class CustomerClient 
{	      
    String myname;
    String myphone;
	Date time = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");    
	String curday;
	Scanner sc = new Scanner(System.in);
	
	CustomerClient(String name, String phone) { //고객의 이름과 전화번호로 고객 식별
		myname = name;
		myphone = phone;
		curday = format.format(time);		
	}
	
	void register() throws SQLException {
		String quary;
		quary = "insert into CUSTOMER values(?,?)";
		OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
		OracleTest.pstm.setString(1, myname);
		OracleTest.pstm.setString(2, myphone);				
		OracleTest.pstm.executeUpdate();
		System.out.println("계정등록완료");
	}
		
	
    void print_room(String select) throws SQLException{ //객실리스트 출력 String select는 WHERE 절 의미
    	    
    	System.out.println("요청하신 객실 리스트\n");
        String quary = "SELECT * FROM ROOM " + select;
       
        OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
        OracleTest.rs = OracleTest.pstm.executeQuery();
        ResultSetMetaData rsmeta = OracleTest.rs.getMetaData();
        printObject.print_dashes(rsmeta);
        printObject.print_headers(rsmeta);
        printObject.print_datarows(rsmeta, OracleTest.rs);
        
    }
           
    void customer_reserve() throws SQLException{ //고객예약    	    	
    	//원하는 객실번호 입력
    	System.out.print("예약할 객실번호 입력: ");    	    	    
    	int roomno;
    	roomno = sc.nextInt();    	
    	sc.nextLine();
    	String quary = "SELECT * FROM ROOM WHERE ROOMNO="+roomno;
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
        OracleTest.rs = OracleTest.pstm.executeQuery();
        OracleTest.rs.next();
        int cost = OracleTest.rs.getInt(3);
        
    	System.out.println("예약날짜 입력: 다음과 같은 형식으로 입력: 1998-04-25");
    	//예약날짜 입력받아 sql date 형식으로 변경
    	String date;
    	date = sc.nextLine();    	
    	java.sql.Date cur = java.sql.Date.valueOf(date);
    	
    	
    	//예약 가능시 예약정보 테이블 INSERT 객실 리스트에서 점유여부 변경
    	if(curday.compareTo(date) < 0) { //당일 예약 및 잘못된 날짜 이용불가 테스트를 위해서 아래처럼 사용    	
    		//name, phone, roomno, cost, ispay, date 순서대로 insert         	
    		quary = "insert into CUSTOMER_RESERVE values(?,?,?,?,?,?)";
    		OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
    		OracleTest.pstm.setString(1, myname);
    		OracleTest.pstm.setString(2, myphone);
    		OracleTest.pstm.setInt(3, roomno);
    		OracleTest.pstm.setInt(4, cost);
    		OracleTest.pstm.setString(5, "X");
    		OracleTest.pstm.setDate(6, cur);

    		OracleTest.pstm.executeUpdate();
    		//roomno와 date가 Primary Key이므로 예약창에서 먼저 최종 예약을 누른사람이 예약이 됨.
    		
    		System.out.println("예약이 완료 되었습니다.");
    	}
    	//예약 불가능시 메시지 출력
    	else {    		    			    
    		if(curday.equals(date)) {
    			System.out.println("당일 예약은 불가능 합니다.");
    		}
    		else if(curday.compareTo(date) > 0) {
    			System.out.println("잘못된 날짜 입력입니다.");
    		}
    		
    	}
    }
    
    void print_reserve() throws SQLException { //나의 예약정보 +총 결제금액 출력
    	System.out.println("나의 예약정보리스트\n");
        String quary = "SELECT * FROM CUSTOMER_RESERVE WHERE phone=" + myphone;
        int cost = 0;
        OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
        OracleTest.rs = OracleTest.pstm.executeQuery();
        ResultSetMetaData rsmeta = OracleTest.rs.getMetaData();
        printObject.print_dashes(rsmeta);
        printObject.print_headers(rsmeta);
        printObject.print_datarows(rsmeta, OracleTest.rs);
        OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
        OracleTest.rs = OracleTest.pstm.executeQuery();
        while(OracleTest.rs.next()) {
        	if(OracleTest.rs.getString(5).equals("X"))
        		cost += OracleTest.rs.getInt(4);
        }
        System.out.println("총 결제해야 하는 금액: "+cost);
    }
    
    void update_table(String condition, String table, String setcond) throws SQLException { // WHERE 조건과 table 명, 변경할 칼럼 SET 조건을 받아 UPDATE를 실행함
    	String quary = "UPDATE "+table+" SET "+setcond+" "+condition;
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
    	OracleTest.pstm.executeUpdate();
    }
    
    void delete_table(String table, String cond) throws SQLException { //table명과 WHERE 조건을 입력받아 해당행을 제거하는 DELETE문 실행
    	String quary = "DELETE FROM "+table+" "+cond;
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
    	OracleTest.pstm.executeUpdate();
    }
    
    void cancel_reserve() throws SQLException { //예약취소
    	//취소할 객실 번호 입력 -> 예약정보 delete
    	System.out.print("예약을 취소할 객실번호를 입력해주세요: ");    	
    	int roomno;
    	roomno = sc.nextInt();
    	sc.nextLine();    
    	System.out.print("예약을 취소할 날짜를 입력해주세요: ");
    	String date;
    	date = sc.nextLine();
    	String quary = "SELECT * FROM CUSTOMER_RESERVE WHERE roomno="+roomno+" AND "+"reservedate="+"'"+date+"'";
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);    	
        OracleTest.rs = OracleTest.pstm.executeQuery();       
		if(OracleTest.rs.next()) { //객실번호가 예약 되어 있을 경우
			String cuname = OracleTest.rs.getString(1);
	        String cuphone = OracleTest.rs.getString(2);
	        if(cuname.equals(myname) && cuphone.equals(myphone)) { //취소할 객실이 자신이 예약한 객실이 맞다면
	        	delete_table("CUSTOMER_RESERVE", "WHERE roomno="+roomno+" AND "+"reservedate="+"'"+date+"'");	        	
	        	System.out.println("예약취소 완료");
	        }
	        else { //자신이 예약한 객실이 아닌 경우
	        	System.out.println("내가 예약한 객실이 아닙니다.");
	        }			
		}
		else { //객실번호가 예약되어 있지 않은 경우			       
			 System.out.println("예약한 객실이 아닙니다.");
		}
    }
    
    void pay_room() throws SQLException { //결제하기
    	System.out.print("결제할 객실번호를 입력하세요.");
    	int roomno;
    	roomno = sc.nextInt();
    	sc.nextLine();
    	System.out.print("결제할 객실의 사용날짜를 입력해주세요: ");
    	String date;
    	date = sc.nextLine();
    	String quary = "SELECT * FROM CUSTOMER_RESERVE WHERE roomno="+roomno+" AND "+"name="+"'"+myname+"'"+" AND "+"phone="+"'"+myphone+"'"+" AND "+"reservedate="+"'"+date+"'";
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);    	
        OracleTest.rs = OracleTest.pstm.executeQuery();      
    	if(OracleTest.rs.next()) { //객실번호가 올바르게 입력된 경우 결제 진행
    		if(OracleTest.rs.getString(5).equals("O")) {
    			System.out.println("이미 결제한 객실입니다.");
    		}
    		else {
	    		String payoption;
	    		System.out.println("결제방식을 입력해주세요.");
	    		System.out.println("1.card");
	    		System.out.println("2.account");    		
	    		payoption = sc.nextLine();
	    		if(payoption.equals("1")) payoption = "card";
	    		else if(payoption.equals("2")) payoption = "account";
	    		else {
	    			System.out.println("잘못된 선택 결제취소"); return;
	    		}
	    		
	    		java.sql.Date cur = java.sql.Date.valueOf(curday);
	    		quary = "insert into RECIPT values(reciptno.nextval-1,?,?,?,?,?,?)";
	    		OracleTest.pstm = OracleTest.conn.prepareStatement(quary);    		
	    		OracleTest.pstm.setString(1, myname);
	    		OracleTest.pstm.setString(2, myphone);
	    		OracleTest.pstm.setString(3, payoption);
	    		OracleTest.pstm.setInt(4, OracleTest.rs.getInt(4));    		
	    		OracleTest.pstm.setDate(5, cur);
	    		OracleTest.pstm.setInt(6, roomno);
	    		
	    		OracleTest.pstm.executeUpdate();
	    		
	    		update_table("WHERE ROOMNO="+roomno+" AND "+"reservedate="+"'"+date+"'", "CUSTOMER_RESERVE", "ispay='O'"); //결제여부 변경
	    		System.out.println("결제완료.");
    		}
    	}
    	else { //객실번호가 잘못된 경우
    		System.out.println("예약한 객실이 아닙니다.");
    	}        
    }
    
    void print_recipt() throws SQLException { //결제한 영수증 정보 출력
    	String quary = "SELECT * FROM RECIPT WHERE name="+"'"+myname+"'"+" AND "+"phone="+"'"+myphone+"'";        
        OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
        OracleTest.rs = OracleTest.pstm.executeQuery();
        ResultSetMetaData rsmeta = OracleTest.rs.getMetaData();
        printObject.print_dashes(rsmeta);
        printObject.print_headers(rsmeta);
        printObject.print_datarows(rsmeta, OracleTest.rs);    	
    }
}
    

