package ASimulatorSystem;
import ASimulatorSystem.DBConnection; // your package name
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Method to get database connection
    public static Connection getConnection() {
        Connection con = null;
        try {
            // Step 1: Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Step 2: Connect to database
            String url = "jdbc:mysql://localhost:3306/BMS"; // database URL
            String user = "root";      // your MySQL username
            String password = "Roshan@5390";  // your MySQL password
            
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Database Connected Successfully!");
            
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
        return con; // return the connection object
    }

    // Test the connection
    public static void main(String[] args) {
        getConnection();
    }
}
