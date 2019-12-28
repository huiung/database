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
		
		System.out.println("�� ������������Ʈ\n");
        String quary = "SELECT * FROM CUSTOMER_RESERVE ORDER BY reservedate";       
        OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
        OracleTest.rs = OracleTest.pstm.executeQuery();
        ResultSetMetaData rsmeta = OracleTest.rs.getMetaData();
        printObject.print_dashes(rsmeta);
        printObject.print_headers(rsmeta);
        printObject.print_datarows(rsmeta, OracleTest.rs);
	}
	
	static void check_out() throws SQLException {
	
		System.out.println("���� ���� üũ�ƿ� �� �Ͽ콺Ű�� �Ҵ�");
		// ���� ���� üũ�ƿ� �� �Ͽ콺Ű�� �Ҵ�
		String quary = "SELECT * FROM ADMINST";
		OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
		OracleTest.rs = OracleTest.pstm.executeQuery();
		Calendar cal = new GregorianCalendar(Locale.KOREA);
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -1); // �Ϸ縦 ����.
		String strDate = format.format(cal.getTime());
		while (OracleTest.rs.next()) { // ���� üũ�ε� ���ǵ� üũ�ƿ��ϰ� �Ͽ콺Ű�� �Ҵ�
			update_table("WHERE checkdate=" +"'"+strDate+"'", "ADMINST", "checkout='O'");
			update_table("WHERE checkdate=" +"'"+strDate+"'", "ADMINST", "housekeeping='O'");
		}
		
		print_ADMIN();
	}
	static void print_ADMIN() throws SQLException {
		
		System.out.println("���� ���� üũ��, üũ�ƿ�, �Ͽ콺Ű�� �Ҵ� ��ȸ\n");					    
		
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
	    while(OracleTest.rs.next()) { //���� üũ�� �ؾ��ϴ� ��� ���� ��ȸ
	    	room.add(OracleTest.rs.getInt(3));
	    }
	    java.sql.Date cur = java.sql.Date.valueOf(curday);
	    	    
	    for(int i : room) { //���� ����� ���� üũ��
		    quary = "insert into ADMINST values(?,?,?,?,?)";
			OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
			OracleTest.pstm.setInt(1, i);
			OracleTest.pstm.setDate(2, cur);			
			OracleTest.pstm.setString(3, "O"); 
			OracleTest.pstm.setString(4, "X");
			OracleTest.pstm.setString(5, "X");
			OracleTest.pstm.executeUpdate();
	    }
	    System.out.println("���� ����� ���� üũ�� �Ϸ�\n");
	    print_ADMIN();
	}
	
	static void update_table(String condition, String table, String setcond) throws SQLException { // WHERE ���ǰ� table ��, ������ Į�� SET ������ �޾� UPDATE�� ������
    	String quary = "UPDATE "+table+" SET "+setcond+" "+condition;
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
    	OracleTest.pstm.executeUpdate();
    }
    
    static void delete_table(String table, String cond) throws SQLException { //table��� WHERE ������ �Է¹޾� �ش����� �����ϴ� DELETE�� ����
    	String quary = "DELETE FROM "+table+" "+cond;
    	OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
    	OracleTest.pstm.executeUpdate();
    }
}
