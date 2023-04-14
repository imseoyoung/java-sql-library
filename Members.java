package Library;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Members implements Controller {
    // id, 이름, 주소, 전화번호, 생일, 가입일
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
        }
        // userMain();
    }

    @Override
    public void add() {
        while (true) {
        }
        // userMain();
    }

    @Override
    public void update() {
        while (true) {           
        }
        // userMain();
    }

    @Override
    public void delete() {
        while (true) {            
        }
        // userMain();
    }


    @Override
    public void deleteBack() {
        while (true) {
        }
        // userMain();
    }
}
