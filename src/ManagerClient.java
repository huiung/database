import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Scanner;

public class ManagerClient {

	static Date time = new Date();
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");    
	static String curday = format.format(time);
	static Scanner sc = new Scanner(System.in);
	
	
	static void print_reserve() throws SQLException {
		
		System.out.println("고객 예약정보리스트\n");
        String quary = "SELECT * FROM CUSTOMER_RESERVE ORDER BY reservedate";       
        OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
        OracleTest.rs = OracleTest.pstm.executeQuery();
        ResultSetMetaData rsmeta = OracleTest.rs.getMetaData();
        printObject.print_dashes(rsmeta);
        printObject.print_headers(rsmeta);
        printObject.print_datarows(rsmeta, OracleTest.rs);
	}
	
	static void check_out() throws SQLException {
	
		System.out.println("금일 객실 체크아웃 및 하우스키핑 할당");
		// 금일 객실 체크아웃 및 하우스키핑 할당
		String quary = "SELECT * FROM ADMINST";
		OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
		OracleTest.rs = OracleTest.pstm.executeQuery();
		Calendar cal = new GregorianCalendar(Locale.KOREA);
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -1); // 하루를 뺀다.
		String strDate = format.format(cal.getTime());
		while (OracleTest.rs.next()) { // 전날 체크인된 객실들 체크아웃하고 하우스키핑 할당
			update_table("WHERE checkdate=" +"'"+strDate+"'", "ADMINST", "checkout='O'");
			update_table("WHERE checkdate=" +"'"+strDate+"'", "ADMINST", "housekeeping='O'");
		}
		
		print_ADMIN();
	}
	static void print_ADMIN() throws SQLException {
		
		System.out.println("금일 객실 체크인, 체크아웃, 하우스키핑 할당 조회\n");					    
		
		String quary = "SELECT * FROM ADMINST";
		OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
        OracleTest.rs = OracleTest.pstm.executeQuery();
        ResultSetMetaData rsmeta = OracleTest.rs.getMetaData();
        printObject.print_dashes(rsmeta);
        printObject.print_headers(rsmeta);
        printObject.print_datarows(rsmeta, OracleTest.rs);	  
	}
	
	static void check_in() throws SQLException {					
		String quary = "SELECT * FROM CUSTOMER_RESERVE WHERE reservedate="+"'"+curday+"'";
		OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
	    OracleTest.rs = OracleTest.pstm.executeQuery();
	    ArrayList<Integer> room = new ArrayList<>();
	    while(OracleTest.rs.next()) { //금일 체크인 해야하는 모든 객실 조회
	    	room.add(OracleTest.rs.getInt(3));
	    }
	    java.sql.Date cur = java.sql.Date.valueOf(curday);
	    	    
	    for(int i : room) { //금일 예약된 객실 체크인
		    quary = "insert into ADMINST values(?,?,?,?,?)";
			OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
			OracleTest.pstm.setInt(1, i);
			OracleTest.pstm.setDate(2, cur);			
			OracleTest.pstm.setString(3, "O"); 
			OracleTest.pstm.setString(4, "X");
			OracleTest.pstm.setString(5, "X");
			OracleTest.pstm.executeUpdate();
	    }
	    System.out.println("금일 예약된 객실 체크인 완료\n");
	    print_ADMIN();
	}
	
	static void update_table(String condition, String table, String setcond) throws SQLException { // WHERE 조건과 table 명, 변경할 칼럼 SET 조건을 받아 UPDATE를 실행함
    	String quary = "UPDATE "+table+" SET "+setcond+" "+condition;
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
    	OracleTest.pstm.executeUpdate();
    }
    
    static void delete_table(String table, String cond) throws SQLException { //table명과 WHERE 조건을 입력받아 해당행을 제거하는 DELETE문 실행
    	String quary = "DELETE FROM "+table+" "+cond;
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
    	OracleTest.pstm.executeUpdate();
    }
}
