package Library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class Book implements Controller {
    Connection conn = JDBCUtil.getConnection();
    Scanner sc = new Scanner(System.in);

    private String id;
    private String title;
    private String writer;
    private String publisher;
    private boolean available;

    private String[] deletedBook = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", writer=" + writer + ", publisher="
                + publisher + ", available=" + available + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(available, id, publisher, title, writer);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        return available == other.available && Objects.equals(id, other.id)
                && Objects.equals(publisher, other.publisher) && Objects.equals(title, other.title)
                && Objects.equals(writer, other.writer);
    }

    public void bookMain() {
        String userinput = "";

        while (!userinput.equals("0")) {
            System.out.println("0.뒤로    1.책 조회    2.책 등록    3.책 수정    4.책 삭제    5.삭제취소");
            userinput = sc.nextLine();

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
                    break;
            }
        }
    }

    @Override
    public void view() {
        while (true) {
            System.out.println("보유 도서 목록입니다.");
            String sql = "SELECT * FROM SCOTT.BOOK";
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
                    String title = rs.getString("TITLE");
                    String writer = rs.getString("WRITER");
                    String publisher = rs.getString("PUBLISHER");
                    int available = rs.getInt("AVAILABLE");
                    String availableStr = (available == 1) ? "대출가능" : "대출불가능";
                    System.out.println(id + " " + title + "\t " + writer + "\t " + publisher
                            + "\t\t " + availableStr);
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
        bookMain();
    }

    @Override
    public void add() {
        while (true) {
            System.out.println("책 제목 입력 : ");
            String title = sc.nextLine();
            System.out.println("책 저자 입력 : ");
            String writer = sc.nextLine();
            System.out.println("출판사 입력 : ");
            String publisher = sc.nextLine();
            int available = 1;

            System.out.println("책 제목 : " + title);
            System.out.println("책 저자  : " + writer);
            System.out.println("출판사  : " + publisher);

            System.out.println("입력하신 사항이 모두 맞습니까? 예(Y) 아니오(N)");
            String confirm = sc.nextLine();
            if (confirm.equalsIgnoreCase("y")) {
                String insert_sql =
                        "INSERT INTO SCOTT.BOOK(ID, TITLE, WRITER, PUBLISHER, AVAILABLE) "
                                + "VALUES (LPAD(BOOK_SEQ.NEXTVAL, 8, '0'), ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insert_sql)) {
                    pstmt.setString(1, title);
                    pstmt.setString(2, writer);
                    pstmt.setString(3, publisher);
                    pstmt.setInt(4, available);

                    int result = pstmt.executeUpdate();
                    System.out.println(result + " rows inserted.");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            } else if (confirm.equalsIgnoreCase("n")) {
                System.out.println("도서 정보를 새로 입력하세요.");
                continue;
            } else {
                System.out.println("잘못된 입력입니다. 초기 화면으로 돌아갑니다.");
                continue;
            }
        }
        bookMain();
    }

    @Override
    public void update() {
        while (true) {
            System.out.println("보유 도서 목록입니다.");
            String sql = "SELECT * FROM SCOTT.BOOK";
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
                    String title = rs.getString("TITLE");
                    String writer = rs.getString("WRITER");
                    String publisher = rs.getString("PUBLISHER");
                    int available = rs.getInt("AVAILABLE");
                    System.out.println(id + " " + title + "\t " + writer + "\t " + publisher
                            + "\t\t " + available);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("수정 할 책의 번호를 입력해주세요");
            String new_id = sc.nextLine();
            System.out.println("1.제목 수정    2.저자 수정    3.출판사 수정");
            String userinput = sc.nextLine();
            switch (userinput) {
                case "1":
                    System.out.println("새로운 제목을 입력하세요: ");
                    String new_title = sc.nextLine();
                    String title_sql = "UPDATE BOOK SET TITLE = ? WHERE ID = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(title_sql)) {
                        pstmt.setString(1, new_title);
                        pstmt.setString(2, new_id);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    System.out.println("새로운 저자를 입력하세요: ");
                    String new_writer = sc.nextLine();
                    String writer_sql = "UPDATE BOOK SET WRITER = ? WHERE ID = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(writer_sql)) {
                        pstmt.setString(1, new_writer);
                        pstmt.setString(2, new_id);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    System.out.println("새로운 출판사를 입력하세요: ");
                    String new_publisher = sc.nextLine();
                    String publisher_sql = "UPDATE BOOK SET WRITER = ? WHERE ID = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(publisher_sql)) {
                        pstmt.setString(1, new_publisher);
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
            String select_sql = "SELECT * FROM BOOK WHERE ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(select_sql)) {
                pstmt.setString(1, new_id);
                ResultSet rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rsmd.getColumnName(i) + "\t ");
                }
                System.out.println();
                while (rs.next()) {
                    String id = rs.getString("ID");
                    String title = rs.getString("TITLE");
                    String writer = rs.getString("WRITER");
                    String publisher = rs.getString("PUBLISHER");
                    int available = rs.getInt("AVAILABLE");
                    String availableStr = (available == 1) ? "대출가능" : "대출불가능";
                    System.out.println(id + " " + title + "\t " + writer + "\t " + publisher
                            + "\t\t " + availableStr);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            break;
        }
        bookMain();
    }

    @Override
    public void delete() {
        while (true) {
            // BOOK TABLE 조회
            System.out.println("보유 도서 목록입니다.");
            String sql = "SELECT * FROM SCOTT.BOOK";
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
                    String title = rs.getString("TITLE");
                    String writer = rs.getString("WRITER");
                    String publisher = rs.getString("PUBLISHER");
                    int available = rs.getInt("AVAILABLE");
                    String availableStr = (available == 1) ? "대출가능" : "대출불가능";
                    System.out.println(id + " " + title + "\t " + writer + "\t " + publisher
                            + "\t\t " + availableStr);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            System.out.println("삭제할 책의 번호를 입력해주세요");
            String delete_id = sc.nextLine();

            // DELETED_BOOK 테이블 생성
            String dropTable_sql = "DROP TABLE DELETED_BOOK";
            String createTable_sql = "CREATE TABLE DELETED_BOOK ("
                    + "ID VARCHAR2(8) CONSTRAINT DBOOK_ID_PK PRIMARY KEY, "
                    + "TITLE VARCHAR2(200) NOT NULL, " + "WRITER VARCHAR2(100), "
                    + "PUBLISHER VARCHAR2(100), "
                    + "AVAILABLE NUMBER(1) DEFAULT 1 NOT NULL CONSTRAINT DBOOK_AVAIL CHECK (AVAILABLE IN (0, 1)))";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(dropTable_sql);
                stmt.executeUpdate(createTable_sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String delete_sql = "DELETE FROM SCOTT.BOOK WHERE ID = ?";
            String select_sql = "SELECT * FROM SCOTT.BOOK WHERE ID = ?";
            String insert_sql =
                    "INSERT INTO DELETED_BOOK (ID, TITLE, WRITER, PUBLISHER, AVAILABLE) "
                            + "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement deletePstmt = conn.prepareStatement(delete_sql);
                    PreparedStatement insertPstmt = conn.prepareStatement(insert_sql)) {

                // 삭제된 책 정보 복구를 위해 저장
                insertPstmt.setString(1, delete_id);
                try (PreparedStatement selectPstmt = conn.prepareStatement(select_sql)) {
                    selectPstmt.setString(1, delete_id);
                    ResultSet rs = selectPstmt.executeQuery();
                    while (rs.next()) {
                        String title = rs.getString("TITLE");
                        String writer = rs.getString("WRITER");
                        String publisher = rs.getString("PUBLISHER");
                        int available = rs.getInt("AVAILABLE");

                        insertPstmt.setString(2, title);
                        insertPstmt.setString(3, writer);
                        insertPstmt.setString(4, publisher);
                        insertPstmt.setInt(5, available);
                        insertPstmt.executeUpdate();                 
                    }
                }

                // 선택한 책 정보 삭제
                deletePstmt.setString(1, delete_id);
                deletePstmt.executeUpdate();
                System.out.println("책 정보가 삭제되었습니다.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
            break;
        }
        bookMain();
    }

    @Override
    public void deleteBack() {
        while (true) {
            System.out.println("1.복구하기    2.종료하기");
            String userinput = sc.nextLine();
            switch (userinput) {
                case ("1"):
                    // 삭제된 책 목록 출력
                    String sql = "SELECT * FROM SCOTT.DELETED_BOOK";
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
                            String title = rs.getString("TITLE");
                            String writer = rs.getString("WRITER");
                            String publisher = rs.getString("PUBLISHER");
                            int available = rs.getInt("AVAILABLE");
                            String availableStr = (available == 1) ? "대출가능" : "대출불가능";
                            System.out.println(id + " " + title + "\t " + writer + "\t " + publisher
                                    + "\t\t " + availableStr);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("복구할 책 번호를 입력하세요:");
                    String back_id = sc.nextLine();

                    String selectDeleted_sql = "SELECT * FROM SCOTT.DELETED_BOOK WHERE ID = ?";
                    String insert_sql =
                            "INSERT INTO SCOTT.BOOK (ID, TITLE, WRITER, PUBLISHER, AVAILABLE) "
                                    + "VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement selectDeletedPstmt =
                            conn.prepareStatement(selectDeleted_sql);
                            PreparedStatement insertPstmt = conn.prepareStatement(insert_sql)) {

                        selectDeletedPstmt.setString(1, back_id);
                        ResultSet rs = selectDeletedPstmt.executeQuery();

                        while (rs.next()) {
                            String title = rs.getString("TITLE");
                            String writer = rs.getString("WRITER");
                            String publisher = rs.getString("PUBLISHER");
                            int available = rs.getInt("AVAILABLE");

                            insertPstmt.setString(1, back_id);
                            insertPstmt.setString(2, title);
                            insertPstmt.setString(3, writer);
                            insertPstmt.setString(4, publisher);
                            insertPstmt.setInt(5, available);
                            insertPstmt.executeUpdate();

                            System.out.println("책이 복구되었습니다.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    bookMain();
                    break;
                case ("2"):
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                default:
                    System.out.println("잘못된 입력입니다.");
                    bookMain();
                    break;
            }
        }
    }
}