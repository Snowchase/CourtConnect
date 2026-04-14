package edu.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQliteLoginManager {

    public SQliteLoginManager() {
    }

    public boolean authenticateUser(String user, String pass) {
        String sql = "SELECT 1 FROM athletes WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    public int getAthleteId(String user, String pass) {
        String sql = "SELECT athlete_id FROM athletes WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("athlete_id");
            }

        } catch (SQLException e) {
            System.out.println("Get athlete ID error: " + e.getMessage());
        }

        return -1;
    }
}
