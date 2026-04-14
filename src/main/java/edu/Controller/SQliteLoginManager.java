package edu.Controller;

import java.sql.*;

public class SQliteLoginManager {

    private static final String DB_URL = "jdbc:sqlite:database.db";

    public SQliteLoginManager() {
    }

    public boolean authenticateUser(String user, String pass) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);

            System.out.println("LOGIN CHECK DB_URL = " + DB_URL);
            System.out.println("Trying login for username = " + user);

            Statement stmt = conn.createStatement();
            ResultSet allUsers = stmt.executeQuery("SELECT athlete_id, username, password FROM athletes");

            System.out.println("Current athletes in DB:");
            while (allUsers.next()) {
                System.out.println(
                        "athlete_id=" + allUsers.getInt("athlete_id") +
                        ", username=" + allUsers.getString("username") +
                        ", password=" + allUsers.getString("password")
                );
            }
            allUsers.close();
            stmt.close();

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

            System.out.println("Authenticated = " + authenticated);
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

            System.out.println("Resolved athleteId = " + athleteId);
            return athleteId;

        } catch (SQLException e) {
            System.out.println("Get athlete ID error: " + e.getMessage());
            return -1;
        }
    }
}
