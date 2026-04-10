import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Attendance System =====");
            System.out.println("1. Mark Attendance");
            System.out.println("2. View Report");
            System.out.println("3. Monthly Summary");
            System.out.println("4. Mark Leave");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    markAttendance();
                    break;
                case 2:
                    viewReport();
                    break;
                case 3:
                    monthlySummary();
                    break;
                case 4:
                    markLeave();
                    break;
            }

        } while (choice != 5);
    }

    public static void markAttendance() {
        try (Connection con = DBConnection.getConnection()) {
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter Emp ID: ");
            int empId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            System.out.print("Enter Status: ");
            String status = sc.nextLine();

            String query = "INSERT INTO attendance(emp_id, att_date, status) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, empId);
            ps.setString(2, date);
            ps.setString(3, status);

            ps.executeUpdate();
            System.out.println("Attendance marked!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void viewReport() {
        try (Connection con = DBConnection.getConnection()) {
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter Emp ID: ");
            int empId = sc.nextInt();

            String query = "SELECT * FROM attendance WHERE emp_id=? ORDER BY att_date";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, empId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getDate("att_date") + " - " + rs.getString("status"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void monthlySummary() {
        try (Connection con = DBConnection.getConnection()) {
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter Emp ID: ");
            int empId = sc.nextInt();

            System.out.print("Enter Month: ");
            int month = sc.nextInt();

            System.out.print("Enter Year: ");
            int year = sc.nextInt();

            String query = "SELECT " +
                    "COUNT(CASE WHEN status='present' THEN 1 END) AS present_days, " +
                    "COUNT(CASE WHEN status='late' THEN 1 END) AS late_days " +
                    "FROM attendance WHERE emp_id=? AND MONTH(att_date)=? AND YEAR(att_date)=?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, empId);
            ps.setInt(2, month);
            ps.setInt(3, year);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Present Days: " + rs.getInt("present_days"));
                System.out.println("Late Days: " + rs.getInt("late_days"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void markLeave() {
        try (Connection con = DBConnection.getConnection()) {
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter Emp ID: ");
            int empId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            String query = "INSERT INTO attendance(emp_id, att_date, status) VALUES (?, ?, 'leave')";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, empId);
            ps.setString(2, date);

            ps.executeUpdate();
            System.out.println("Leave marked!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
