import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
public class InsertData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/SRMS";
        String user = "root";
        String password = "2024106262";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO students (name, age, email) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "Kamil khan");
            pstmt.setInt(2, 20);
            pstmt.setString(3, "kamil101@example.com");
            pstmt.executeUpdate();
            
            pstmt.setString(1, "Aman Sharma");
            pstmt.setInt(2, 22);
            pstmt.setString(3, "aman102@example.com");
            pstmt.executeUpdate();
            
            pstmt.setString(1, "Ravi Patel");
            pstmt.setInt(2, 21);
            pstmt.setString(3, "ravi103@example.com");
            pstmt.executeUpdate();
            
            pstmt.setString(1, "Amreen Khan");
            pstmt.setInt(2, 23);
            pstmt.setString(3, "amreen104@example.com");
            pstmt.executeUpdate();
            
            pstmt.setString(1, "Sana Ali");
            pstmt.setInt(2, 19);
            pstmt.setString(3, "sana105@example.com");
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully!");
            con.close();
        } catch (Exception e) {
            System.out.println("Error inserting data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
