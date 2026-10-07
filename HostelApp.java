import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
public class HostelApp {
    private static final String URL = "jdbc:mysql://localhost:3306/SRMS";
    private static final String USER = "root";
    private static final String PASS = "your-sql-password";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
    public static void main(String [] args){
        Scanner sc=new Scanner(System.in);

        while(true){
            System.out.println("\n==================================================");
            System.out.println("           HOSTEL MANAGEMENT SYSTEM  (HMS)           ");
            System.out.println("==================================================");
            System.out.println("1. New Student Admission (Allocate Room)");
            System.out.println("2. View All Hostel Records");
            System.out.println("3. Update Fee Status (Paid/Unpaid)");
            System.out.println("4. Change Student Room / Bed Type");
            System.out.println("5. Vacate Room (Delete Student Record)");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            int choice = 0;
            try{
                choice=sc.nextInt();
                sc.nextLine();
            } catch (Exception e){
                System.out.println("\n>>> [INVALID CHOICE!!!] Please input a numeric choice between 1 and 6. ");
                sc.nextLine();
                continue;
            }
            
            switch (choice) {
                case 1:
                    addStudent(sc);
                    break;

                case 2:
                    viewStudents();
                    break;

                case 3:
                    updateFeeStatus(sc);
                    break;

                case 4:
                    updateRoomDetails(sc);
                    break;

                case 5:
                    deleteStudent(sc);
                    break;

                case 6:
                    System.out.println("Exiting the application. Goodbye Have a nice day!");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again from 1-6.");
            }
        }
    }

            // todo: 1- ADD STUDENTS (ADMISSION)

    public static void addStudent(Scanner sc){
        try(Connection con= getConnection()){
            System.out.print("Enter Student Full Name: ");
            String name = sc.nextLine();
            System.out.print("Room Number: ");
            int roomNumber = sc.nextInt();
            sc.nextLine(); // Consume newline
           
            //todo:// ROOM TYPE VALIDATION
            String bedType = "";
            while (true) { 
                System.out.print("Room Type (AC / Non-AC):");
                bedType=sc.nextLine().trim();
                if(bedType.equalsIgnoreCase("AC") || bedType.equalsIgnoreCase("Non-AC")){
                    bedType = bedType.equalsIgnoreCase("AC") ? "AC" : "Non-AC";
                    break;
                } else{
                    System.out.println("[WARNING!!!] Invalid Room type! Please enter only 'AC' or 'Non-AC'.");
                }
            }

            //todo:// FEE STATUS VALIDATION
            String feeStatus = "";
            while(true){
                System.out.print("Fee Status (Paid / Unpaid):");
                feeStatus=sc.nextLine().trim();

                if (feeStatus.equalsIgnoreCase("Paid")|| feeStatus.equalsIgnoreCase("Unpaid")) {
                    feeStatus = feeStatus.equalsIgnoreCase("Paid") ? "Paid" : "Unpaid";
                    break;
                } else{
                    System.out.println("[WARNING!!] Invalid Fee Status! Please Enter Only 'Paid' or 'Unpaid'.");
                }
            }

            System.out.print("Phone Number: ");
            String phone = sc.nextLine();

            //todo:// ROOM CHECK VALIDATION (Max 2 students per room)

            String checkSql = "SELECT bed_type FROM hostel_management WHERE room_no = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setInt(1, roomNumber);
            ResultSet rs = checkStmt.executeQuery();

            int count = 0;
            String existingRoomType = null;
            while (rs.next()) {
                count++;
                existingRoomType = rs.getString("bed_type");
            }

            if (count >= 2) {
                System.out.println("\n[Error] Room " + roomNumber + " is already full! (Maximum 2 students allowed).");
                return;
            }

            if (count == 1 && existingRoomType != null) {
                if (!existingRoomType.equalsIgnoreCase(bedType)) {
                    System.out.println("\n[Error] Room " + roomNumber + " is already " + existingRoomType + ". Both students must have the same room type!");
                    return;
                }
            }

            String insertSql = "INSERT INTO hostel_management (name, room_no, bed_type, fee_status, phone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertSql);
            pstmt.setString(1, name);
            pstmt.setInt(2, roomNumber);
            pstmt.setString(3, bedType);
            pstmt.setString(4, feeStatus);
            pstmt.setString(5, phone);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("\n>>> Success: Student record added successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

            //todo: 2- VIEW ALL STUDENTS RECORDS

    public static void viewStudents(){
        try(Connection con= getConnection()){
            String sql="SELECT * FROM hostel_management";
            PreparedStatement pstmt=con.prepareStatement(sql);
            ResultSet rs=pstmt.executeQuery(); 

            System.out.println("\n-----------------------------------------------------------------------------------------------------"); 
            System.out.printf("%-6s %-18s %-10s %-18s %-12s %-15s\n", "ID", "Name", "Room No", "Bed Type", "Fee Status", "Phone");
            System.out.println("-----------------------------------------------------------------------------------------------------");

            boolean found= false;
            while(rs.next()){
                found=true;
                System.out.printf("%-6d %-18s %-10d %-18s %-12s %-15s\n",
                rs.getInt("id"),
                rs.getString("name"), 
                rs.getInt("room_no"), 
                rs.getString("bed_type"), 
                rs.getString("fee_status"), 
                rs.getString("phone"));
            }
            if(!found){
                System.out.println("No records found.");
            }
            System.out.println("-----------------------------------------------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //todo: 3- UPDATE FEE STATUS (PAID/UNPAID)

    public static void updateFeeStatus(Scanner sc){
        viewStudents(); //show list
        try(Connection con=getConnection()){
            System.out.print("Enter Student ID to update fee status: ");
            int id = sc.nextInt();
            sc.nextLine(); // Consume newline
            
            //FEE STATUS VALIDATION LOOP
            String newStatus = "";
            while (true) {
                System.out.print("Enter new Fee Status (Paid / Unpaid):");
                newStatus =sc.nextLine().trim();
                if (newStatus.equalsIgnoreCase("Paid") || newStatus.equalsIgnoreCase("Unpaid")) {
                    newStatus = newStatus.equalsIgnoreCase("Paid") ? "Paid" : "Unpaid";
                    break;
                } else{
                    System.out.println("[WARNING!!] Invalid Fee Status! Please Enter only 'Paid' or 'Unpaid'.");
                }
            }


            String sql = "UPDATE hostel_management SET fee_status = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("\n>>> Success: Fee status updated successfully.");
            } else {
                System.out.println("\n>>> Error: Student ID not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //todo: 4- CHANGE STUDENT ROOM / ROOM TYPE

    public static void updateRoomDetails(Scanner sc){
        viewStudents();
        try(Connection con=getConnection()){
            System.out.print("Enter Student ID to update room/type: ");
            int id = sc.nextInt();
            sc.nextLine(); // Consume newline
            System.out.print("Enter new Room Number: ");
            int newRoom = sc.nextInt();
            sc.nextLine(); // Consume newline
           
            //NEW ROOM TYPE VALIDATION

            String newType = "";
            while (true) { 
                System.out.print(" Enter new Room Type (AC / Non-AC):");
                newType=sc.nextLine().trim();
                if(newType.equalsIgnoreCase("AC") || newType.equalsIgnoreCase("Non-AC")){
                    newType = newType.equalsIgnoreCase("AC") ? "AC" : "Non-AC";
                    break;
                } else{
                    System.out.println("[WARNING!!!] Invalid Room type! Please enter only 'AC' or 'Non-AC'.");
                }
            }

            // Check target room capacity (Excluding this current student)
            String checkSql = "SELECT bed_type FROM hostel_management WHERE room_no = ? AND id != ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setInt(1, newRoom);
            checkStmt.setInt(2, id);
            ResultSet rs = checkStmt.executeQuery();

            int count = 0;
            String existingType = null;
            while (rs.next()) {
                count++;
                existingType = rs.getString("bed_type");
            }

            if (count >= 2) {
                System.out.println("\n[Error] Target Room " + newRoom + " is already full! (Maximum 2 students allowed).");
                return;
            }

            if (count == 1 && existingType != null) {
                if (!existingType.equalsIgnoreCase(newType)) {
                    System.out.println("\n[Error] Target Room " + newRoom + " is already " + existingType + ". Both students must have the same room type!");
                    return;
                }
            }

            String sql = "UPDATE hostel_management SET room_no = ?, bed_type = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, newRoom);
            pstmt.setString(2, newType);
            pstmt.setInt(3, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("\n>>> Success: Room and room type updated successfully.");
            } else {
                System.out.println("\n>>> Error: Student ID not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //todo: 5- VACATE ROOM (DELETE STUDENT RECORD)

    public static void deleteStudent(Scanner sc){
        viewStudents(); //list showing
        try(Connection con=getConnection()){
            System.out.print("Enter Student ID to vacate room (delete record): ");
            int id = sc.nextInt();
            sc.nextLine();//buffer clear

            String sql = "DELETE FROM hostel_management WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("\n>>> Success: Student record deleted successfully.");
            } else {
                System.out.println("\n>>> Error: Student ID not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

