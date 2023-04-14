package Library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {

    public static void main(String[] args) {
        Connection conn = JDBCUtil.getConnection();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SCOTT.EMP");

            while (rs.next()) {
                System.out.println(rs.getString("EMPNO") + " ");
            }

        } catch (SQLException e) {
            System.out.println("쿼리 실행 실패");
            e.printStackTrace();
        } 
    }
}