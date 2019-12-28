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
	
	CustomerClient(String name, String phone) { //���� �̸��� ��ȭ��ȣ�� �� �ĺ�
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
		System.out.println("������ϿϷ�");
	}
		
	
    void print_room(String select) throws SQLException{ //���Ǹ���Ʈ ��� String select�� WHERE �� �ǹ�
    	    
    	System.out.println("��û�Ͻ� ���� ����Ʈ\n");
        String quary = "SELECT * FROM ROOM " + select;
       
        OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
        OracleTest.rs = OracleTest.pstm.executeQuery();
        ResultSetMetaData rsmeta = OracleTest.rs.getMetaData();
        printObject.print_dashes(rsmeta);
        printObject.print_headers(rsmeta);
        printObject.print_datarows(rsmeta, OracleTest.rs);
        
    }
           
    void customer_reserve() throws SQLException{ //������    	    	
    	//���ϴ� ���ǹ�ȣ �Է�
    	System.out.print("������ ���ǹ�ȣ �Է�: ");    	    	    
    	int roomno;
    	roomno = sc.nextInt();    	
    	sc.nextLine();
    	String quary = "SELECT * FROM ROOM WHERE ROOMNO="+roomno;
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
        OracleTest.rs = OracleTest.pstm.executeQuery();
        OracleTest.rs.next();
        int cost = OracleTest.rs.getInt(3);
        
    	System.out.println("���೯¥ �Է�: ������ ���� �������� �Է�: 1998-04-25");
    	//���೯¥ �Է¹޾� sql date �������� ����
    	String date;
    	date = sc.nextLine();    	
    	java.sql.Date cur = java.sql.Date.valueOf(date);
    	
    	
    	//���� ���ɽ� �������� ���̺� INSERT ���� ����Ʈ���� �������� ����
    	if(curday.compareTo(date) < 0) { //���� ���� �� �߸��� ��¥ �̿�Ұ� �׽�Ʈ�� ���ؼ� �Ʒ�ó�� ���    	
    		//name, phone, roomno, cost, ispay, date ������� insert         	
    		quary = "insert into CUSTOMER_RESERVE values(?,?,?,?,?,?)";
    		OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
    		OracleTest.pstm.setString(1, myname);
    		OracleTest.pstm.setString(2, myphone);
    		OracleTest.pstm.setInt(3, roomno);
    		OracleTest.pstm.setInt(4, cost);
    		OracleTest.pstm.setString(5, "X");
    		OracleTest.pstm.setDate(6, cur);

    		OracleTest.pstm.executeUpdate();
    		//roomno�� date�� Primary Key�̹Ƿ� ����â���� ���� ���� ������ ��������� ������ ��.
    		
    		System.out.println("������ �Ϸ� �Ǿ����ϴ�.");
    	}
    	//���� �Ұ��ɽ� �޽��� ���
    	else {    		    			    
    		if(curday.equals(date)) {
    			System.out.println("���� ������ �Ұ��� �մϴ�.");
    		}
    		else if(curday.compareTo(date) > 0) {
    			System.out.println("�߸��� ��¥ �Է��Դϴ�.");
    		}
    		
    	}
    }
    
    void print_reserve() throws SQLException { //���� �������� +�� �����ݾ� ���
    	System.out.println("���� ������������Ʈ\n");
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
        System.out.println("�� �����ؾ� �ϴ� �ݾ�: "+cost);
    }
    
    void update_table(String condition, String table, String setcond) throws SQLException { // WHERE ���ǰ� table ��, ������ Į�� SET ������ �޾� UPDATE�� ������
    	String quary = "UPDATE "+table+" SET "+setcond+" "+condition;
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
    	OracleTest.pstm.executeUpdate();
    }
    
    void delete_table(String table, String cond) throws SQLException { //table��� WHERE ������ �Է¹޾� �ش����� �����ϴ� DELETE�� ����
    	String quary = "DELETE FROM "+table+" "+cond;
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
    	OracleTest.pstm.executeUpdate();
    }
    
    void cancel_reserve() throws SQLException { //�������
    	//����� ���� ��ȣ �Է� -> �������� delete
    	System.out.print("������ ����� ���ǹ�ȣ�� �Է����ּ���: ");    	
    	int roomno;
    	roomno = sc.nextInt();
    	sc.nextLine();    
    	System.out.print("������ ����� ��¥�� �Է����ּ���: ");
    	String date;
    	date = sc.nextLine();
    	String quary = "SELECT * FROM CUSTOMER_RESERVE WHERE roomno="+roomno+" AND "+"reservedate="+"'"+date+"'";
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);    	
        OracleTest.rs = OracleTest.pstm.executeQuery();       
		if(OracleTest.rs.next()) { //���ǹ�ȣ�� ���� �Ǿ� ���� ���
			String cuname = OracleTest.rs.getString(1);
	        String cuphone = OracleTest.rs.getString(2);
	        if(cuname.equals(myname) && cuphone.equals(myphone)) { //����� ������ �ڽ��� ������ ������ �´ٸ�
	        	delete_table("CUSTOMER_RESERVE", "WHERE roomno="+roomno+" AND "+"reservedate="+"'"+date+"'");	        	
	        	System.out.println("������� �Ϸ�");
	        }
	        else { //�ڽ��� ������ ������ �ƴ� ���
	        	System.out.println("���� ������ ������ �ƴմϴ�.");
	        }			
		}
		else { //���ǹ�ȣ�� ����Ǿ� ���� ���� ���			       
			 System.out.println("������ ������ �ƴմϴ�.");
		}
    }
    
    void pay_room() throws SQLException { //�����ϱ�
    	System.out.print("������ ���ǹ�ȣ�� �Է��ϼ���.");
    	int roomno;
    	roomno = sc.nextInt();
    	sc.nextLine();
    	System.out.print("������ ������ ��볯¥�� �Է����ּ���: ");
    	String date;
    	date = sc.nextLine();
    	String quary = "SELECT * FROM CUSTOMER_RESERVE WHERE roomno="+roomno+" AND "+"name="+"'"+myname+"'"+" AND "+"phone="+"'"+myphone+"'"+" AND "+"reservedate="+"'"+date+"'";
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);    	
        OracleTest.rs = OracleTest.pstm.executeQuery();      
    	if(OracleTest.rs.next()) { //���ǹ�ȣ�� �ùٸ��� �Էµ� ��� ���� ����
    		if(OracleTest.rs.getString(5).equals("O")) {
    			System.out.println("�̹� ������ �����Դϴ�.");
    		}
    		else {
	    		String payoption;
	    		System.out.println("��������� �Է����ּ���.");
	    		System.out.println("1.card");
	    		System.out.println("2.account");    		
	    		payoption = sc.nextLine();
	    		if(payoption.equals("1")) payoption = "card";
	    		else if(payoption.equals("2")) payoption = "account";
	    		else {
	    			System.out.println("�߸��� ���� �������"); return;
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
	    		
	    		update_table("WHERE ROOMNO="+roomno+" AND "+"reservedate="+"'"+date+"'", "CUSTOMER_RESERVE", "ispay='O'"); //�������� ����
	    		System.out.println("�����Ϸ�.");
    		}
    	}
    	else { //���ǹ�ȣ�� �߸��� ���
    		System.out.println("������ ������ �ƴմϴ�.");
    	}        
    }
    
    void print_recipt() throws SQLException { //������ ������ ���� ���
    	String quary = "SELECT * FROM RECIPT WHERE name="+"'"+myname+"'"+" AND "+"phone="+"'"+myphone+"'";        
        OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
        OracleTest.rs = OracleTest.pstm.executeQuery();
        ResultSetMetaData rsmeta = OracleTest.rs.getMetaData();
        printObject.print_dashes(rsmeta);
        printObject.print_headers(rsmeta);
        printObject.print_datarows(rsmeta, OracleTest.rs);    	
    }
}
    

