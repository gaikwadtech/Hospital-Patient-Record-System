// JDBC Project: Hospital Patient Records (CRUD Operations)

import java.sql.*;
import java.util.Scanner;

public class HospitalPatientRecords {
    static final String DB_URL = "jdbc:mysql://localhost:3306/hospitaldb";
    static final String USER = "root";
    static final String PASS = "Pratik@2006";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // ✅ Load MySQL JDBC Driver first
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ Now establish connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("✅ Connected to database.");

            // Create table if not exists
            String createTable = "CREATE TABLE IF NOT EXISTS patients (" +
                    "patient_id INT PRIMARY KEY, " +
                    "name VARCHAR(50), " +
                    "age INT, " +
                    "gender VARCHAR(10), " +
                    "diagnosis VARCHAR(100), " +
                    "admission_date DATE)";

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTable);
            }

            int choice;
            do {
                System.out.println("\n--- Hospital Patient Records ---");
                System.out.println("1. Insert Patient Record");
                System.out.println("2. View Patient by ID");
                System.out.println("3. Update Diagnosis by ID");
                System.out.println("4. Delete Patient by ID");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1 -> insertPatient(conn, sc);
                    case 2 -> viewPatient(conn, sc);
                    case 3 -> updateDiagnosis(conn, sc);
                    case 4 -> deletePatient(conn, sc);
                    case 5 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } while (choice != 5);

            conn.close(); // ✅ Close connection

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }

    private static void insertPatient(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Patient ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Age: ");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Gender: ");
        String gender = sc.nextLine();
        System.out.print("Enter Diagnosis: ");
        String diagnosis = sc.nextLine();
        System.out.print("Enter Admission Date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        String sql = "INSERT INTO patients VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setInt(3, age);
        ps.setString(4, gender);
        ps.setString(5, diagnosis);
        ps.setDate(6, Date.valueOf(date));

        int rows = ps.executeUpdate();
        System.out.println(rows + " patient(s) inserted.");
    }

    private static void viewPatient(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Patient ID to view: ");
        int id = sc.nextInt();

        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("\nPatient ID: " + rs.getInt("patient_id"));
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Age: " + rs.getInt("age"));
            System.out.println("Gender: " + rs.getString("gender"));
            System.out.println("Diagnosis: " + rs.getString("diagnosis"));
            System.out.println("Admission Date: " + rs.getDate("admission_date"));
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static void updateDiagnosis(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Patient ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new Diagnosis: ");
        String newDiagnosis = sc.nextLine();

        String sql = "UPDATE patients SET diagnosis = ? WHERE patient_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newDiagnosis);
        ps.setInt(2, id);

        int rows = ps.executeUpdate();
        System.out.println(rows + " patient(s) updated.");
    }

    private static void deletePatient(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Patient ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM patients WHERE patient_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        int rows = ps.executeUpdate();
        System.out.println(rows + " patient(s) deleted.");
    }
}
