import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
public class ReadData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/SRMS";
        String user = "root";
        String password = "2024106262";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            String sql = "SELECT * FROM students";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("ID\tName\tAge\tEmail");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String email = rs.getString("email");
                System.out.println(id + "\t" + name + "\t" + age + "\t" + email);
            }

            con.close();
        } catch (Exception e) {
            System.out.println("Error reading data: " + e.getMessage());
            e.printStackTrace();
        }

    
    }
}
