package Library;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Members implements Controller {
    Connection conn = JDBCUtil.getConnection();
    Scanner sc = new Scanner(System.in);

    private String id;
    private String name;
    private String address;
    private String phone;
    private Date birthday;
    private Date joindate;

    private String[] deletedUser = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getJoindate() {
        return joindate;
    }

    public void setJoindate(Date joindate) {
        this.joindate = joindate;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", address=" + address + ", phone=" + phone
                + ", birthday=" + birthday + ", joindate=" + joindate + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, birthday, id, joindate, name, phone);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Members other = (Members) obj;
        return Objects.equals(address, other.address) && Objects.equals(birthday, other.birthday)
                && Objects.equals(id, other.id) && Objects.equals(joindate, other.joindate)
                && Objects.equals(name, other.name) && Objects.equals(phone, other.phone);
    }

    public void memberMain() {
        System.out.println("0.뒤로    1.회원 조회    2.회원 등록    3.회원 수정    4.회원 삭제    5.삭제취소");
        String userinput = sc.nextLine();
        while (true) {
            switch (userinput) {
                case ("0"):
                    return;
                case ("1"):
                    view();
                    break;
                case ("2"):
                    add();
                    break;
                case ("3"):
                    update();
                    break;
                case ("4"):
                    delete();
                    break;
                case ("5"):
                    deleteBack();
                    break;
                default:
                    System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
                    return;
            }
        }
    }

    @Override
    public void view() {
        while (true) {
            System.out.println("회원 정보 목록입니다.");
            String sql = "SELECT * FROM SCOTT.MEMBERS";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rsmd.getColumnName(i) + "\t\t ");
                }
                System.out.println();
                while (rs.next()) {
                    String id = rs.getString("ID");
                    String name = rs.getString("NAME");
                    String address = rs.getString("ADDRESS");
                    String phone = rs.getString("PHONE");
                    Date birthday = rs.getDate("BIRTHDAY");
                    Date joindate = rs.getDate("JOINDATE");
                    System.out.println(id + "\t " + name + "\t\t " + address + "\t\t " + phone + "\t "
                            + birthday + "\t\t " + joindate);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("뒤로 가기(B) 종료 하기(X)");
            String select = sc.nextLine();
            if (select.equalsIgnoreCase("b")) {
                break;
            } else if (select.equalsIgnoreCase("X")) {
                System.out.println("프로그램을 종료합니다.");
                System.exit(0);
            } else {
                System.out.println("잘못 입력하셨습니다. 처음으로 돌아갑니다.");
                break;
            }
        }
        memberMain();
    }

    @Override
    public void add() {
        while (true) {
            System.out.println("회원 이름 입력 : ");
            String name = sc.nextLine();
            System.out.println("회원 주소 입력 : ");
            String address = sc.nextLine();
            System.out.println("전화번호 입력 : (010-XXXX-XXXX) ");
            String phone = sc.nextLine();
            System.out.println("생년월일 입력 : (yyyy-MM-dd) ");
            String birthday = sc.nextLine();

            System.out.println("회원 이름 : " + name);
            System.out.println("회원 주소  : " + address);
            System.out.println("전화번호  : " + phone);
            System.out.println("생년월일  : " + birthday);

            System.out.println("입력하신 사항이 모두 맞습니까? 예(Y) 아니오(N)");
            String confirm = sc.nextLine();
            if (confirm.equalsIgnoreCase("y")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = format.parse(birthday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Timestamp timestamp = new Timestamp(date.getTime());

                String insert_sql =
                        "INSERT INTO SCOTT.MEMBERS(ID, NAME, ADDRESS, PHONE, BIRTHDAY, JOINDATE) "
                                + "VALUES (LPAD(MEMBERS_SEQ.NEXTVAL, 8, '0'), ?, ?, ?, ?, sysdate)";

                try (PreparedStatement pstmt = conn.prepareStatement(insert_sql)) {
                    pstmt.setString(1, name);
                    pstmt.setString(2, address);
                    pstmt.setString(3, phone);
                    pstmt.setTimestamp(4, timestamp);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            } else if (confirm.equalsIgnoreCase("n")) {
                System.out.println("회원 정보를 새로 입력하세요.");
                continue;
            } else {
                System.out.println("잘못된 입력입니다. 초기 화면으로 돌아갑니다.");
                continue;
            }
        }
        memberMain();
    }

    @Override
    public void update() {
        while (true) {
            System.out.println("회원 정보 목록입니다.");
            String sql = "SELECT * FROM SCOTT.MEMBERS";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rsmd.getColumnName(i) + "\t\t ");
                }
                System.out.println();
                while (rs.next()) {
                    String id = rs.getString("ID");
                    String name = rs.getString("NAME");
                    String address = rs.getString("ADDRESS");
                    String phone = rs.getString("PHONE");
                    Date birthday = rs.getDate("BIRTHDAY");
                    Date joindate = rs.getDate("JOINDATE");
                    System.out.println(id + "\t" + name + "\t\t" + address + "\t" + phone + "\t"
                            + birthday + "\t\t" + joindate);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("수정 할 회원의 번호를 입력해주세요");
            String new_id = sc.nextLine();
            System.out.println("1.이름 수정    2.주소 수정    3.전화번호 수정    4.생년월일 수정");
            String userinput = sc.nextLine();
            switch (userinput) {
                case "1":
                    System.out.println("새로운 이름을 입력하세요 : ");
                    String new_name = sc.nextLine();
                    String name_sql = "UPDATE MEMBER SET NAME = ? WHERE ID = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(name_sql)) {
                        pstmt.setString(1, new_name);
                        pstmt.setString(2, new_id);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    System.out.println("새로운 주소를 입력하세요 : ");
                    String new_address = sc.nextLine();
                    String address_sql = "UPDATE MEMBERS SET ADDRESS = ? WHERE ID = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(address_sql)) {
                        pstmt.setString(1, new_address);
                        pstmt.setString(2, new_id);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    System.out.println("새로운 전화번호를 입력하세요 : (010-XXXX-XXXX) ");
                    String new_phone = sc.nextLine();
                    String phone_sql = "UPDATE MEMBERS SET PHONE = ? WHERE ID = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(phone_sql)) {
                        pstmt.setString(1, new_phone);
                        pstmt.setString(2, new_id);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    System.out.println("새로운 생년월일을 입력하세요: ");
                    String new_birthday = sc.nextLine();
                    String publisher_sql = "UPDATE MEMBERS SET BIRTHDAY = ? WHERE ID = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(publisher_sql)) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        try {
                            date = format.parse(new_birthday);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Timestamp timestamp = new Timestamp(date.getTime());
                                                
                        pstmt.setTimestamp(1, timestamp);
                        pstmt.setString(2, new_id);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    continue;
            }
            String select_sql = "SELECT * FROM MEMBERS WHERE ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(select_sql)) {
                pstmt.setString(1, new_id);
                ResultSet rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rsmd.getColumnName(i) + "\t\t ");
                }
                System.out.println();
                while (rs.next()) {
                    String id = rs.getString("ID");
                    String name = rs.getString("NAME");
                    String address = rs.getString("ADDRESS");
                    String phone = rs.getString("PHONE");
                    Date birthday = rs.getDate("BIRTHDAY");
                    Date joindate = rs.getDate("JOINDATE");
                    System.out.println(id + "\t " + name + "\t\t " + address + "\t\t " + phone + "\t "
                            + birthday + "\t\t " + joindate);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            break;
        }
        memberMain();
    }

    @Override
    public void delete() {
        while (true) {
            System.out.println("회원 정보 목록입니다.");
            String sql = "SELECT * FROM SCOTT.MEMBERS";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rsmd.getColumnName(i) + "\t\t ");
                }
                System.out.println();
                while (rs.next()) {
                    String id = rs.getString("ID");
                    String name = rs.getString("NAME");
                    String address = rs.getString("ADDRESS");
                    String phone = rs.getString("PHONE");
                    Date birthday = rs.getDate("BIRTHDAY");
                    Date joindate = rs.getDate("JOINDATE");
                    System.out.println(id + "\t" + name + "\t\t" + address + "\t" + phone + "\t"
                            + birthday + "\t\t" + joindate);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("삭제할 회원의 번호를 입력해주세요");
            String delete_id = sc.nextLine();

            // DELETED_MEMBER 테이블 생성
            String dropTable_sql = "DROP TABLE DELETED_MEMBER";
            String createTable_sql = "CREATE TABLE DELETED_MEMBER("
                    + "ID VARCHAR2(8) CONSTRAINT DMEMBER_ID_PK PRIMARY KEY, "
                    + "NAME VARCHAR(20) NOT NULL, " + "ADDRESS VARCHAR2(200), "
                    + "PHONE VARCHAR2(14) CONSTRAINT DMEMBER_PHONE_UK UNIQUE," + "BIRTHDAY DATE,"
                    + "JOINDATE DATE)";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(dropTable_sql);
                stmt.executeUpdate(createTable_sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String delete_sql = "DELETE FROM SCOTT.MEMBERS WHERE ID = ?";
            String select_sql = "SELECT * FROM SCOTT.MEMBERS WHERE ID = ?";
            String insert_sql =
                    "INSERT INTO DELETED_MEMBER(ID, NAME, ADDRESS, PHONE, BIRTHDAY, JOINDATE) "
                            + "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement deletePstmt = conn.prepareStatement(delete_sql);
                    PreparedStatement insertPstmt = conn.prepareStatement(insert_sql)) {

                // 삭제된 회원 정보 복구를 위해 저장
                insertPstmt.setString(1, delete_id);
                try (PreparedStatement selectPstmt = conn.prepareStatement(select_sql)) {
                    selectPstmt.setString(1, delete_id);
                    ResultSet rs = selectPstmt.executeQuery();
                    while (rs.next()) {
                        String name = rs.getString("NAME");
                        String address = rs.getString("ADDRESS");
                        String phone = rs.getString("PHONE");
                        java.util.Date utilBirthDay = rs.getDate("BIRTHDAY");
                        java.sql.Date birthday = new java.sql.Date(utilBirthDay.getTime());
                        java.util.Date utilJoindate = rs.getDate("JOINDATE");
                        java.sql.Date joindate = new java.sql.Date(utilJoindate.getTime());

                        insertPstmt.setString(2, name);
                        insertPstmt.setString(3, address);
                        insertPstmt.setString(4, phone);
                        insertPstmt.setDate(5, birthday);
                        insertPstmt.setDate(6, joindate);
                        insertPstmt.executeUpdate();
                    }
                }

                // 선택한 회원 정보 삭제
                deletePstmt.setString(1, delete_id);
                deletePstmt.executeUpdate();
                System.out.println("회원정보가 삭제되었습니다.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
            break;
        }
        memberMain();
    }


    @Override
    public void deleteBack() {
        while (true) {
            System.out.println("1.복구하기    2.종료하기");
            String userinput = sc.nextLine();
            switch (userinput) {
                case ("1"):
                    // 삭제된 책 목록 출력
                    String sql = "SELECT * FROM SCOTT.DELETED_MEMBER";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        ResultSet rs = pstmt.executeQuery();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(rsmd.getColumnName(i) + "\t ");
                        }
                        System.out.println();
                        while (rs.next()) {
                            String id = rs.getString("ID");
                            String name = rs.getString("NAME");
                            String address = rs.getString("ADDRESS");
                            String phone = rs.getString("PHONE");
                            Date birthday = rs.getDate("BIRTHDAY");
                            Date joindate = rs.getDate("JOINDATE");
                            System.out.println(id + "\t" + name + "\t\t" + address + "\t" + phone
                                    + "\t" + birthday + "\t\t" + joindate);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("복구할 회원 번호를 입력하세요:");
                    String back_id = sc.nextLine();

                    String selectDeleted_sql = "SELECT * FROM SCOTT.DELETED_MEMBER WHERE ID = ?";
                    String insert_sql =
                            "INSERT INTO MEMBERS(ID, NAME, ADDRESS, PHONE, BIRTHDAY, JOINDATE) "
                                    + "VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement selectDeletedPstmt =
                            conn.prepareStatement(selectDeleted_sql);
                            PreparedStatement insertPstmt = conn.prepareStatement(insert_sql)) {

                        selectDeletedPstmt.setString(1, back_id);
                        ResultSet rs = selectDeletedPstmt.executeQuery();

                        while (rs.next()) {
                            String name = rs.getString("NAME");
                            String address = rs.getString("ADDRESS");
                            String phone = rs.getString("PHONE");
                            java.util.Date utilBirthDay = rs.getDate("BIRTHDAY");
                            java.sql.Date birthday = new java.sql.Date(utilBirthDay.getTime());
                            java.util.Date utilJoindate = rs.getDate("JOINDATE");
                            java.sql.Date joindate = new java.sql.Date(utilJoindate.getTime());

                            insertPstmt.setString(1, back_id);
                            insertPstmt.setString(2, name);
                            insertPstmt.setString(3, address);
                            insertPstmt.setString(4, phone);
                            insertPstmt.setDate(5, birthday);
                            insertPstmt.setDate(6, joindate);
                            insertPstmt.executeUpdate();

                            System.out.println("회원정보가 복구되었습니다.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    memberMain();
                    break;
                case ("2"):
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                default:
                    System.out.println("잘못된 입력입니다.");
                    memberMain();
                    break;
            }
        }
    }
}
