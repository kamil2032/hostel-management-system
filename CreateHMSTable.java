import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
public class CreateHMSTable {
    public static void main(String[] args){
        String url = "jdbc:mysql://localhost:3306/SRMS";
        String user = "root";
        String password = "2024106262";

        String createTableSql="CREATE TABLE IF NOT EXISTS hostel_management (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(100) NOT NULL," +
                "room_no INT NOT NULL," +
                "bed_type VARCHAR(50) NOT NULL," +
                "fee_status VARCHAR(50) NOT NULL," +
                "phone VARCHAR(15) NOT NULL UNIQUE," +
                "admission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            stmt.executeUpdate(createTableSql);
            System.out.println("Table created successfully.");

            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Error creating table: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
