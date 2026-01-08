import java.sql.*;
import java.util.*;

public class CollegeFeeManagement {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n===== College Fee Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. Pay Fee");
            System.out.println("3. View Fee Details");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> payFee();
                case 3 -> viewStudent();
                case 4 -> System.out.println("Thank you!");
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 4);
    }

    static void addStudent() {
        try (Connection con = DBConnection.getConnection()) {

            System.out.print("Student ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Student Name: ");
            String name = sc.nextLine();

            System.out.print("Course: ");
            String course = sc.nextLine();

            System.out.print("Total Fee: ");
            double totalFee = sc.nextDouble();

            String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, course);
            ps.setDouble(4, totalFee);
            ps.setDouble(5, 0);

            ps.executeUpdate();
            System.out.println("Student added successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void payFee() {
        try (Connection con = DBConnection.getConnection()) {

            System.out.print("Student ID: ");
            int id = sc.nextInt();

            System.out.print("Amount to Pay: ");
            double amount = sc.nextDouble();

            String sql = "UPDATE students SET paid_fee = paid_fee + ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setInt(2, id);

            int result = ps.executeUpdate();
            if (result > 0)
                System.out.println("Fee payment successful!");
            else
                System.out.println("Student not found!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewStudent() {
        try (Connection con = DBConnection.getConnection()) {

            System.out.print("Student ID: ");
            int id = sc.nextInt();

            String sql = "SELECT * FROM students WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double total = rs.getDouble("total_fee");
                double paid = rs.getDouble("paid_fee");

                System.out.println("\n----- Fee Details -----");
                System.out.println("Name       : " + rs.getString("name"));
                System.out.println("Course     : " + rs.getString("course"));
                System.out.println("Total Fee  : RS" + total);
                System.out.println("Paid Fee   : RS" + paid);
                System.out.println("Due Fee    : RS" + (total - paid));
            } else {
                System.out.println("Student not found!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
