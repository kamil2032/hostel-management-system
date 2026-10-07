import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/SRMS";
        String user = "root";
        String password = "2024106262";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("DATABASE IS SUCCESSFULLY IS CONNECTED!");
            con.close();
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}