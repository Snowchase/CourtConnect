package edu.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQliteRegistrationManager {

    public SQliteRegistrationManager() {
    }

    public boolean registerAthlete(String user, String pass) {
        if (user == null || user.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
            System.out.println("Username or password cannot be empty");
            return false;
        }

        String checkSql = "SELECT 1 FROM athletes WHERE username = ?";
        String insertSql = "INSERT INTO athletes (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, user.trim());
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Username already exists");
                    return false;
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, user.trim());
                pstmt.setString(2, pass.trim());
                pstmt.executeUpdate();
            }

            System.out.println("User registered successfully");
            return true;

        } catch (SQLException e) {
            System.out.println("Registration error: " + e.getMessage());
            return false;
        }
    }
}
