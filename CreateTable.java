import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
public class CreateTable {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/SRMS";
        String user = "root";
        String password = "your-sql-passowrd";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(100) NOT NULL," +
                    "age INT NOT NULL," +
                    "email VARCHAR(100) UNIQUE NOT NULL" +
                    ")";

            stmt.executeUpdate(sql);
            System.out.println("Table 'students' created successfully!");

            con.close();
        } catch (Exception e) {
            System.out.println("Error creating table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
