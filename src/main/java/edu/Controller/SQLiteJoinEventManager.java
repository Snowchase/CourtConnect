package edu.Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//File Name: SQLiteJoinEventManager.java
//Group: 3
//Description: Handles join event database operations

public class SQLiteJoinEventManager {

    private static final String DB_URL = "jdbc:sqlite:database.db";

    public boolean eventExists(int eventId) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT 1 FROM sporting_events WHERE sportingid = ?"
            );
            pstmt.setInt(1, eventId);

            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();

            rs.close();
            pstmt.close();
            conn.close();

            return exists;
        } catch (SQLException e) {
            System.out.println("eventExists error: " + e.getMessage());
            return false;
        }
    }

    public boolean isEventFull(int eventId) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT current_players, max_players FROM sporting_events WHERE sportingid = ?"
            );
            pstmt.setInt(1, eventId);

            ResultSet rs = pstmt.executeQuery();

            boolean full = false;
            if (rs.next()) {
                int currentPlayers = rs.getInt("current_players");
                int maxPlayers = rs.getInt("max_players");
                full = currentPlayers >= maxPlayers;
            }

            rs.close();
            pstmt.close();
            conn.close();

            return full;
        } catch (SQLException e) {
            System.out.println("isEventFull error: " + e.getMessage());
            return true;
        }
    }

    public boolean athleteExists(int athleteId) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT 1 FROM athletes WHERE athlete_id = ?"
            );
            pstmt.setInt(1, athleteId);

            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();

            rs.close();
            pstmt.close();
            conn.close();

            return exists;
        } catch (SQLException e) {
            System.out.println("athleteExists error: " + e.getMessage());
            return false;
        }
    }

    public boolean isAlreadyJoined(int athleteId, int eventId) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT 1 FROM event_participants WHERE athlete_id = ? AND sportingid = ?"
            );
            pstmt.setInt(1, athleteId);
            pstmt.setInt(2, eventId);

            ResultSet rs = pstmt.executeQuery();
            boolean alreadyJoined = rs.next();

            rs.close();
            pstmt.close();
            conn.close();

            return alreadyJoined;
        } catch (SQLException e) {
            System.out.println("isAlreadyJoined error: " + e.getMessage());
            return false;
        }
    }

    public boolean addParticipant(int athleteId, int eventId) {
        Connection conn = null;
        PreparedStatement insertParticipant = null;
        PreparedStatement updateEventCount = null;

        try {
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(false);

            insertParticipant = conn.prepareStatement(
                    "INSERT INTO event_participants (athlete_id, sportingid) VALUES (?, ?)"
            );
            insertParticipant.setInt(1, athleteId);
            insertParticipant.setInt(2, eventId);
            insertParticipant.executeUpdate();

            updateEventCount = conn.prepareStatement(
                    "UPDATE sporting_events SET current_players = current_players + 1 WHERE sportingid = ?"
            );
            updateEventCount.setInt(1, eventId);
            updateEventCount.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackException) {
                System.out.println("Rollback error: " + rollbackException.getMessage());
            }

            System.out.println("addParticipant error: " + e.getMessage());
            return false;

        } finally {
            try {
                if (insertParticipant != null) insertParticipant.close();
                if (updateEventCount != null) updateEventCount.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Closing error: " + e.getMessage());
            }
        }
    }

    public Object[][] getAllEvents() {
        List<Object[]> rows = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT sportingid, sport, event_date, location FROM sporting_events ORDER BY sportingid"
            );

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("sportingid"),
                        rs.getString("sport"),
                        rs.getString("event_date"),
                        rs.getString("location")
                });
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("getAllEvents error: " + e.getMessage());
        }

        return rows.toArray(new Object[0][]);
    }
}
