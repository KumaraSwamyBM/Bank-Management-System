package ASimulatorSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class Conn {
    public Connection c;
    public Statement s;

    public Conn() {  
        try {  
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");  

            // Connect to the database
            c = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/BMS", // database URL
                    "root",                             // username
                    "Roshan@5390"                        // password
            );

            // Create a statement object
            s = c.createStatement(); 

            System.out.println("Database Connected Successfully!");

        } catch(ClassNotFoundException e){ 
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch(SQLException e){ 
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }  
    }  
}
