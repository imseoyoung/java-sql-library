package Library;

import java.text.ParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ParseException {
        Scanner sc = new Scanner(System.in);
        Book book = new Book();
        Members member = new Members();
        Loan borrow = new Loan();

        while (true) {
            System.out.println("서영도서관이지렁이");
            System.out.println("1. 회원관리    2. 책관리    3. 대출관리    4.종료");
            String userInput = sc.nextLine();

            switch (userInput) {
                case ("1"):
                    member.memberMain();
                    break;
                case ("2"):
                    book.bookMain();
                    break;
                case ("3"):
                    borrow.borrowMain();
                    break;
                case ("4"):
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                default:
                    System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
                    break;
            }
        }
    }
}
