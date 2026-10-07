import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ClearData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/SRMS";
        String user = "root";
        String password = "your_mysql_pass";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            Statement stmt = con.createStatement();
            stmt.executeUpdate("TRUNCATE TABLE students"); // TRUNCATE is used to clean all the data that are present in the table

            System.out.println("Data cleared!");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
