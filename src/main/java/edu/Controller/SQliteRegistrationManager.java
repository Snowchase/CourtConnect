package edu.Controller;

import java.sql.*;
import edu.DatabaseResources.DatabaseConnection;

public class SQliteRegistrationManager {

    public SQliteRegistrationManager() {
    }

    public boolean registerAthlete(String user, String pass) {
        try {
            Connection conn = DriverManager.getConnection(DatabaseConnection.DB_URL);

            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT 1 FROM athletes WHERE username = ?"
            );
            checkStmt.setString(1, user);

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                rs.close();
                checkStmt.close();
                conn.close();
                System.out.println("Username already exists");
                return false;
            }

            rs.close();
            checkStmt.close();

            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO athletes (username, password) VALUES (?, ?)"
            );
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

            System.out.println("User registered successfully");
            return true;

        } catch (SQLException e) {
            System.out.println("Registration error: " + e.getMessage());
            return false;
        }
    }
}
