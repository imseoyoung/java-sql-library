package Library;

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

    public void borrowMain() throws ParseException {
        String userinput = "";

        while (!userinput.equals("0")) {
            System.out.println("0.뒤로    1.대출하기    2.반납하기    3.연장하기    4.대출목록");
            userinput = sc.nextLine();

            switch (userinput) {
                case ("0"):
                    return;
                case ("1"):
                    borrow();
                    break;
                case ("2"):
                    returnBook();
                    break;
                case ("3"):
                    extendBook();
                    break;
                case ("4"):
                    viewAvailable();
                    break;
                default:
                    System.out.println("잘못입력했습니다. 다시 입력해주세요.");
                    break;
            }
        }
    }

    public void borrow() {
        while (true) {
        }
        // borrowMain();
    }

    public void returnBook() {
        while (true) {
        }
        // borrowMain();   
    }

    public void extendBook() throws ParseException {
        while (true) {
        }
        // borrowMain();
    }


    public void viewAvailable() throws ParseException {
        while (true) { 
        }
        // borrowMain();
    }

}
