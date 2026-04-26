package edu.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQliteLoginManager {

    public SQliteLoginManager() {
    }

    public boolean authenticateUser(String user, String pass) {
        try {
            Connection conn = DatabaseManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT 1 FROM athletes WHERE username = ? AND password = ?"
            );
            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();
            boolean authenticated = rs.next();

            rs.close();
            pstmt.close();
            conn.close();

            return authenticated;

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    public int getAthleteId(String user, String pass) {
        try {
            Connection conn = DatabaseManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT athlete_id FROM athletes WHERE username = ? AND password = ?"
            );
            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();
            int athleteId = -1;

            if (rs.next()) {
                athleteId = rs.getInt("athlete_id");
            }

            rs.close();
            pstmt.close();
            conn.close();

            return athleteId;

        } catch (SQLException e) {
            System.out.println("Get athlete ID error: " + e.getMessage());
            return -1;
        }
    }

    public String getUserRole(String user, String pass) {
        try {
            Connection conn = DatabaseManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT role FROM athletes WHERE username = ? AND password = ?"
            );
            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();
            String role = "Athlete";

            if (rs.next()) {
                role = rs.getString("role");
            }

            rs.close();
            pstmt.close();
            conn.close();

            return role;

        } catch (SQLException e) {
            System.out.println("Get role error: " + e.getMessage());
            return "Athlete";
        }
    }
}