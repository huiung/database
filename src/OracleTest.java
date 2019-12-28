import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
 
 
public class OracleTest 
{
	static Connection conn = null; // DB연결된 상태(세션)을 담은 객체
    static PreparedStatement pstm = null;  // SQL 문을 나타내는 객체
    static ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체
	
    public static void main(String args[])
    {                    	
        while(true) {        	
        	
	        try {
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				conn = DBConnection.getConnection();
				Scanner sc = new Scanner(System.in);
				System.out.println("로그인 모드 선택");
				System.out.println("=============");
				System.out.println("1.관리자 모드");
				System.out.println("2.사용자 모드");
				System.out.println("3.사용자 계정 등록");
				System.out.println("4.프로그램 종료");
				int menu;
				menu = sc.nextInt();
				sc.nextLine();

				if (menu == 1) {
					while(true) {
						System.out.println("관리자 모드로 로그인 하였습니다.");
						System.out.println("사용 기능 선택");
						System.out.println("=====================================");
						System.out.println("1.고객 예약정보 조회");
						System.out.println("2.금일 객실 체크아웃, 하우스키핑할당");
						System.out.println("3.금일 객실 체크인");
						System.out.println("4.금일 객실 정보 조회");
						System.out.println("5.뒤로 가기");
						menu = sc.nextInt();
						sc.nextLine();
						// 사용 가능 기능 메뉴
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
					System.out.println("로그인을 위해 이름과 전화번호를 입력해주세요.");
					String name, phone;
						
					System.out.print("이름:");
					name = sc.nextLine();
					System.out.print("전화번호:");
					phone = sc.nextLine();
					
					//계정 등록 여부 확인
					String quary = "SELECT * FROM CUSTOMER WHERE name="+"'"+name+"'"+" AND phone="+"'"+phone+"'"; 
				       
			        OracleTest.pstm = OracleTest.conn.prepareStatement(quary);
			        OracleTest.rs = OracleTest.pstm.executeQuery();
			        if(OracleTest.rs.next()) {
			        	System.out.println("로그인 성공");
			        }
			        else {
			        	System.out.println("존재하지 않는 계정입니다.");
			        	continue;
			        }													
					
					CustomerClient customer = new CustomerClient(name, phone); // 고객 기능을 수행할 객체
					while (true) {
						System.out.println("사용자 모드로 로그인 하였습니다.");
						System.out.println("사용 기능 선택");
						System.out.println("===========");
						System.out.println("1.객실 조회");
						System.out.println("2.객실 예약");
						System.out.println("3.예약 정보 조회");
						System.out.println("4.예약 취소");
						System.out.println("5.예약 객실 결제");
						System.out.println("6.결제 영수증 확인");
						System.out.println("7.뒤로 가기");
						int choose;
						choose = sc.nextInt();
						sc.nextLine();

						if (choose == 1) {
							customer.print_room("WHERE 1=1");
						} else if (choose == 2) {
							// 예약
							customer.customer_reserve();
						} else if (choose == 3) {
							// 예약정보 조회
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
							System.out.println("잘못 입력하셨습니다.");
						}
					}
				} 
				else if (menu == 3) {
					System.out.println("사용자 계정 등록을 위해 이름과 전화번호를 입력해주세요.");					
					String name, phone;
					System.out.print("이름:");
					name = sc.nextLine();
					System.out.print("전화번호:");
					phone = sc.nextLine();
					
					CustomerClient customer = new CustomerClient(name, phone); // 고객 기능을 수행할 객체
					customer.register(); // 사용자 계정 등록
				}
				else if (menu == 4) {
					break;
				} 
				else {
					System.out.println("잘못 입력하였습니다.");
				}

			} catch (SQLException sqle) {
				System.out.println("SELECT문에서 예외 발생");
				sqle.printStackTrace();

			} finally {
				// DB 연결을 종료한다.
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
