package Library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {

    public static void main(String[] args) {
        Connection conn = JDBCUtil.getConnection();

        String title = "어린민정";
        String writer = "베리베리";
        String publisher = "민정출판";
        int available = 1;

        String sql = "INSERT INTO SCOTT.BOOK(ID, TITLE, WRITER, PUBLISHER, AVAILABLE) "
                + "VALUES (LPAD(BOOK_SEQ.NEXTVAL, 8, '0'), ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, title);
            pstmt.setString(2, writer);
            pstmt.setString(3, publisher);
            pstmt.setInt(4, available);

            int result = pstmt.executeUpdate();
            System.out.println(result + " rows inserted.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
