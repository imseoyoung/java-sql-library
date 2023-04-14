package Library;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Book implements Controller {
    // id, 제목, 저자, 출판사, 대출가능여부
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
        }
        // bookMain();
    }

    @Override
    public void add() {
        while (true) {
            // bookId는 중복되면 안 됨          
        }
        // bookMain();
    }

    @Override
    public void update() {
        while (true) {
        }
        // bookMain();
    }


    @Override
    public void delete() {
        while (true) {
        }
        // bookMain();
    }

    @Override
    public void deleteBack() {
        while (true) {
        }
        // bookMain();
    }
}


