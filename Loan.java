package Library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Loan {
    Connection conn = JDBCUtil.getConnection();

    // 책 이름, 회원 이름, 대출일, 반납 날짜
    Scanner sc = new Scanner(System.in);
    Book book = new Book();
    Members user = new Members();

    private String loanId;
    private String bookId;
    private String userId;
    private Date loandate;
    private Date due;
    private String extend;


    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getBorrowdate() {
        return loandate;
    }

    public void setBorrowdate(Date borrowdate) {
        this.loandate = borrowdate;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public void loanMain() throws ParseException {
        String userinput = "";

        while (!userinput.equals("0")) {
            System.out.println("0.뒤로    1.대출하기    2.반납하기    3.연장하기    4.대출목록");
            userinput = sc.nextLine();

            switch (userinput) {
                case ("0"):
                    return;
                case ("1"):
                    loan();
                    break;
                case ("2"):
                    returnBook();
                    break;
                case ("3"):
                    extendBook();
                    break;
                case ("4"):
                    loanAvailable();
                    break;
                default:
                    System.out.println("잘못입력했습니다. 다시 입력해주세요.");
                    break;
            }
        }
    }

    public void loan() {
        while (true) {
            System.out.println("회원번호를 입력해주세요.");
            String memberId = sc.nextLine();
            String selectMemberSql = "SELECT * FROM SCOTT.MEMBERS WHERE ID = ?";
            try (PreparedStatement selectMemberPstmt = conn.prepareStatement(selectMemberSql)) {
                selectMemberPstmt.setString(1, memberId);
                ResultSet memberRs = selectMemberPstmt.executeQuery();

                if (memberRs.next()) {
                    // 입력된 회원번호가 존재하는 경우
                    Loan loan = new Loan();
                    loan.setUserId(memberId);

                    System.out.println("보유 도서 목록입니다.");
                    String selectBookSql = "SELECT * FROM SCOTT.BOOK";
                    try (PreparedStatement selectBookPstmt = conn.prepareStatement(selectBookSql)) {
                        ResultSet bookRs = selectBookPstmt.executeQuery();
                        ResultSetMetaData rsmd = bookRs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(rsmd.getColumnName(i) + "\t ");
                        }
                        System.out.println();
                        while (bookRs.next()) {
                            String id = bookRs.getString("ID");
                            String title = bookRs.getString("TITLE");
                            String writer = bookRs.getString("WRITER");
                            String publisher = bookRs.getString("PUBLISHER");
                            int available = bookRs.getInt("AVAILABLE");
                            String availableStr = (available == 1) ? "대출가능" : "대출불가능";
                            System.out.println(id + " " + title + "\t " + writer + "\t " + publisher
                                    + "\t\t " + availableStr);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("빌릴 책 번호를 입력해주세요.");
                    String loanbookid = sc.nextLine();

                    String selectBookSql1 = "SELECT * FROM BOOK WHERE ID = ?";
                    String updateBookSql = "UPDATE BOOK SET AVAILABLE = 0 WHERE ID = ?";
                    String insertLoanSql =
                            "INSERT INTO LOAN (ID, MEMBERID, BOOKID, LOANDATE, DUE, AVAILABLE) "
                                    + "VALUES (LPAD(LOAN_SEQ.NEXTVAL, 8, '0'), ?, ?, sysdate, sysdate + 7, 1)";

                    try (PreparedStatement selectPstmt = conn.prepareStatement(selectBookSql1);
                            PreparedStatement updatePstmt = conn.prepareStatement(updateBookSql);
                            PreparedStatement insertPstmt = conn.prepareStatement(insertLoanSql)) {

                        // 선택한 책 정보 조회
                        selectPstmt.setString(1, loanbookid);
                        ResultSet rs = selectPstmt.executeQuery();
                        while (rs.next()) {
                            String bookId = rs.getString("ID");

                            // 선택한 책 대여 가능 여부 확인
                            int available = rs.getInt("AVAILABLE");
                            if (available == 1) {
                                // 책 대여 가능한 경우
                                // book table의 해당 행의 available을 0으로 변경
                                updatePstmt.setString(1, bookId);
                                updatePstmt.executeUpdate();

                                // loan table에 대여 정보 저장
                                insertPstmt.setString(1, loan.getUserId()); // 대여한 회원의 아이디
                                insertPstmt.setString(2, bookId); // 대여한 책의 아이디
                                insertPstmt.executeUpdate();
                                System.out.println("대출이 완료되었습니다.");
                                break;
                            } else {
                                // 책 대여 불가능한 경우
                                System.out.println("선택하신 책은 대출이 불가능합니다.");
                                break;
                            }
                        }
                    }
                    break;
                } else {
                    // 회원번호가 존재하지 않는 경우
                    System.out.println("회원번호가 존재하지 않습니다.");
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void returnBook() {
        while (true) {
            System.out.println("대출 목록입니다.");
            String selectBookSql = "SELECT * FROM SCOTT.LOAN ORDER BY DUE ASC";
            try (PreparedStatement selectBookPstmt = conn.prepareStatement(selectBookSql)) {
                ResultSet returnRs = selectBookPstmt.executeQuery();
                ResultSetMetaData rsmd = returnRs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rsmd.getColumnName(i) + "\t\t ");
                }
                System.out.println();
                while (returnRs.next()) {
                    String id = returnRs.getString("ID");
                    String memberid = returnRs.getString("MEMBERID");
                    String bookid = returnRs.getString("BOOKID");
                    Date loandate = returnRs.getDate("LOANDATE");
                    Date due = returnRs.getDate("DUE");
                    int available = returnRs.getInt("AVAILABLE");
                    String availableStr = (available == 1) ? "연장가능" : "연장불가능";
                    System.out.println(id + "\t " + memberid + "\t\t " + bookid + "\t " + loandate
                            + "\t\t " + due + "\t " + availableStr);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("반납 할 책의 책 번호를 입력해주세요");
            String returnid = sc.nextLine();

            String selectLoanSql = "SELECT * FROM SCOTT.LOAN WHERE BOOKID = ?";
            String updateBookSql = "UPDATE SCOTT.BOOK SET AVAILABLE = 1 WHERE ID = ?";
            String deleteLoanSql = "DELETE FROM SCOTT.LOAN WHERE BOOKID = ?";

            try (PreparedStatement selectLoanPstmt = conn.prepareStatement(selectLoanSql);
                    PreparedStatement updateBookPstmt = conn.prepareStatement(updateBookSql);
                    PreparedStatement deleteLoanPstmt = conn.prepareStatement(deleteLoanSql)) {

                // 대여 목록에서 선택한 책 정보 조회
                selectLoanPstmt.setString(1, returnid);
                ResultSet loanRs = selectLoanPstmt.executeQuery();
                if (loanRs.next()) {
                    // 선택한 책이 대여 목록에 있으면 대출 정보 삭제
                    deleteLoanPstmt.setString(1, returnid);
                    deleteLoanPstmt.executeUpdate();

                    // book table의 해당 행의 available을 1로 변경
                    updateBookPstmt.setString(1, returnid);
                    updateBookPstmt.executeUpdate();
                    System.out.println("반납 완료되었습니다.");
                    break;
                } else {
                    // 선택한 책이 대출 목록에 없으면 메시지 출력
                    System.out.println("선택하신 책이 대출 목록에 없습니다.");
                    break;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void extendBook() {
        while (true) {
            System.out.println("대출 목록입니다.");
            String selectBookSql = "SELECT * FROM SCOTT.LOAN ORDER BY DUE ASC";
            try (PreparedStatement selectBookPstmt = conn.prepareStatement(selectBookSql)) {
                ResultSet returnRs = selectBookPstmt.executeQuery();
                ResultSetMetaData rsmd = returnRs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rsmd.getColumnName(i) + "\t\t ");
                }
                System.out.println();
                while (returnRs.next()) {
                    String id = returnRs.getString("ID");
                    String memberid = returnRs.getString("MEMBERID");
                    String bookid = returnRs.getString("BOOKID");
                    Date loandate = returnRs.getDate("LOANDATE");
                    Date due = returnRs.getDate("DUE");
                    int available = returnRs.getInt("AVAILABLE");
                    String availableStr = (available == 1) ? "연장가능" : "연장불가능";
                    System.out.println(id + "\t " + memberid + "\t\t " + bookid + "\t " + loandate
                            + "\t\t " + due + "\t " + availableStr);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("연장 할 책의 번호를 입력해주세요");
            String extendid = sc.nextLine();

            String selectLoanSql = "SELECT * FROM SCOTT.LOAN WHERE BOOKID = ?";
            String updateLoanSql =
                    "UPDATE SCOTT.LOAN SET DUE = DUE + 7, AVAILABLE = 0 WHERE ID = ? AND AVAILABLE = 1";

            try (PreparedStatement selectLoanPstmt = conn.prepareStatement(selectLoanSql);
                    PreparedStatement updateLoanPstmt = conn.prepareStatement(updateLoanSql);                    ) {

                selectLoanPstmt.setString(1, extendid);
                ResultSet loanRs = selectLoanPstmt.executeQuery();

                // 대출 목록이 존재하면 해당 대출 목록의 DUE 날짜를 7일 연장
                if (loanRs.next()) {
                    String loanId = loanRs.getString("ID");
                    updateLoanPstmt.setString(1, loanId);

                    int updateCount = updateLoanPstmt.executeUpdate();

                    if (updateCount > 0) {
                        System.out.println("연장이 완료되었습니다.");
                        break;
                    } else {
                        System.out.println("연장에 실패하였습니다.");
                        break;
                    }
                } else {
                    System.out.println("선택하신 책은 현재 대출 중이 아닙니다.");
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void loanAvailable() {
        while (true) {
            System.out.println("대출 목록입니다.");
            String selectBookSql = "SELECT * FROM SCOTT.LOAN ORDER BY DUE ASC";
            try (PreparedStatement selectBookPstmt = conn.prepareStatement(selectBookSql)) {
                ResultSet returnRs = selectBookPstmt.executeQuery();
                ResultSetMetaData rsmd = returnRs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rsmd.getColumnName(i) + "\t\t ");
                }
                System.out.println();
                while (returnRs.next()) {
                    String id = returnRs.getString("ID");
                    String memberid = returnRs.getString("MEMBERID");
                    String bookid = returnRs.getString("BOOKID");
                    Date loandate = returnRs.getDate("LOANDATE");
                    Date due = returnRs.getDate("DUE");
                    int available = returnRs.getInt("AVAILABLE");
                    String availableStr = (available == 1) ? "연장가능" : "연장불가능";
                    System.out.println(id + "\t " + memberid + "\t\t " + bookid + "\t " + loandate
                            + "\t\t " + due + "\t " + availableStr);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("뒤로 가기(B) 종료 하기(X)");
            String userinput = sc.nextLine();
            if (userinput.equalsIgnoreCase("b")) {
                break;
            } else if (userinput.equalsIgnoreCase("X")) {
                System.out.println("프로그램을 종료합니다.");
                System.exit(0);
            } else {
                System.out.println("잘못 입력하셨습니다. 처음으로 돌아갑니다.");
                break;
            }
        }
    }
}
