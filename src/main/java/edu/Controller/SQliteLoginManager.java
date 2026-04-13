package edu.Controller;

import java.sql.*;

//File Name: SQliteLoginManager.java
//Group: 3
//Date: 3/37/2026
//Description: This class manages all data requests to SQlite regarding login and returns data to the controller
public class SQliteLoginManager {

    private static final String DB_URL = "jdbc:sqlite:database.db";

    public SQliteLoginManager() {
    }

    public boolean authenticateUser(String user, String pass) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);

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

            if (authenticated) {
                System.out.println("User authenticated");
            } else {
                System.out.println("User not found");
            }

            return authenticated;

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    public int getAthleteId(String user, String pass) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);

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
}
