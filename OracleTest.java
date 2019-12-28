import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
 
 
public class OracleTest 
{
	static Connection conn = null; // DB����� ����(����)�� ���� ��ü
    static PreparedStatement pstm = null;  // SQL ���� ��Ÿ���� ��ü
    static ResultSet rs = null;  // �������� �����Ϳ� ���� ��ȯ���� ���� ��ü
	
    public static void main(String args[])
    {                    	
        while(true) {        	
        	
	        try {
				// SQL ������ ����� ���� ������ ���Ǿ�(SELECT��)���
				// �� ����� ���� ResulSet ��ü�� �غ��� �� �����Ų��.
				conn = DBConnection.getConnection();
				Scanner sc = new Scanner(System.in);
				System.out.println("�α��� ��� ����");
				System.out.println("=============");
				System.out.println("1.������ ���");
				System.out.println("2.����� ���");
				System.out.println("3.����� ���� ���");
				System.out.println("4.���α׷� ����");
				int menu;
				menu = sc.nextInt();
				sc.nextLine();

				if (menu == 1) {
					while(true) {
						System.out.println("������ ���� �α��� �Ͽ����ϴ�.");
						System.out.println("��� ��� ����");
						System.out.println("=====================================");
						System.out.println("1.�� �������� ��ȸ");
						System.out.println("2.���� ���� üũ�ƿ�, �Ͽ콺Ű���Ҵ�");
						System.out.println("3.���� ���� üũ��");
						System.out.println("4.���� ���� ���� ��ȸ");
						System.out.println("5.�ڷ� ����");
						menu = sc.nextInt();
						sc.nextLine();
						// ��� ���� ��� �޴�
						if(menu == 1) {
							ManagerClient.print_reserve();
						}
						else if(menu == 2) {
							ManagerClient.check_out();
						}
						else if(menu == 3) {
							ManagerClient.check_in();
						}
						else if(menu == 4) {
							ManagerClient.print_ADMIN();
						}
						else if(menu == 5) {
							break;
						}
					}
				} else if (menu == 2) {
					System.out.println("�α����� ���� �̸��� ��ȭ��ȣ�� �Է����ּ���.");
					String name, phone;
						
					System.out.print("�̸�:");
					name = sc.nextLine();
					System.out.print("��ȭ��ȣ:");
					phone = sc.nextLine();
					
					//���� ��� ���� Ȯ��
					String quary = "SELECT * FROM CUSTOMER WHERE name="+"'"+name+"'"+" AND phone="+"'"+phone+"'"; 
				       
			        OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
			        OracleTest.rs = OracleTest.pstm.executeQuery();
			        if(OracleTest.rs.next()) {
			        	System.out.println("�α��� ����");
			        }
			        else {
			        	System.out.println("�������� �ʴ� �����Դϴ�.");
			        	continue;
			        }													
					
					CustomerClient customer = new CustomerClient(name, phone); // �� ����� ������ ��ü
					while (true) {
						System.out.println("����� ���� �α��� �Ͽ����ϴ�.");
						System.out.println("��� ��� ����");
						System.out.println("===========");
						System.out.println("1.���� ��ȸ");
						System.out.println("2.���� ����");
						System.out.println("3.���� ���� ��ȸ");
						System.out.println("4.���� ���");
						System.out.println("5.���� ���� ����");
						System.out.println("6.���� ������ Ȯ��");
						System.out.println("7.�ڷ� ����");
						int choose;
						choose = sc.nextInt();
						sc.nextLine();

						if (choose == 1) {
							customer.print_room("WHERE 1=1");
						} else if (choose == 2) {
							// ����
							customer.customer_reserve();
						} else if (choose == 3) {
							// �������� ��ȸ
							customer.print_reserve();
						} else if (choose == 4) {
							customer.cancel_reserve();
						} else if (choose == 5) {
							customer.pay_room();
						} else if (choose == 6) {
							customer.print_recipt();
						} else if (choose == 7) {
							break;
						} else {
							System.out.println("�߸� �Է��ϼ̽��ϴ�.");
						}
					}
				} 
				else if (menu == 3) {
					System.out.println("����� ���� ����� ���� �̸��� ��ȭ��ȣ�� �Է����ּ���.");					
					String name, phone;
					System.out.print("�̸�:");
					name = sc.nextLine();
					System.out.print("��ȭ��ȣ:");
					phone = sc.nextLine();
					
					CustomerClient customer = new CustomerClient(name, phone); // �� ����� ������ ��ü
					customer.register(); // ����� ���� ���
				}
				else if (menu == 4) {
					break;
				} 
				else {
					System.out.println("�߸� �Է��Ͽ����ϴ�.");
				}

			} catch (SQLException sqle) {
				System.out.println("SELECT������ ���� �߻�");
				sqle.printStackTrace();

			} finally {
				// DB ������ �����Ѵ�.
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstm != null) {
						pstm.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {					
					throw new RuntimeException(e.getMessage());
				}

			}
		}
	}

}
