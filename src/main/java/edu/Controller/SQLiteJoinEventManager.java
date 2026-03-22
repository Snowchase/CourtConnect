package edu.Controller;

import java.sql.*;

// File Name: SQLiteJoinEventManager.java
// Group: 3
// Description: Handles join event database operations

public class SQLiteJoinEventManager {

    private static final String DB_URL = "jdbc:sqlite:database.db";

    // Change these table names / column names if your DB uses different names
    private static final String EVENTS_TABLE = "sporting_events";
    private static final String PARTICIPANTS_TABLE = "event_participants";

    public boolean eventExists(int eventId) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT 1 FROM " + EVENTS_TABLE + " WHERE event_id = ?"
            );
            pstmt.setInt(1, eventId);

            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();

            rs.close();
            pstmt.close();
            conn.close();

            return exists;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean isEventFull(int eventId) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT current_players, max_players FROM " + EVENTS_TABLE + " WHERE event_id = ?"
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
            System.out.println(e.getMessage());
            return true;
        }
    }

    public boolean isAlreadyJoined(int athleteId, int eventId) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT 1 FROM " + PARTICIPANTS_TABLE + " WHERE athlete_id = ? AND event_id = ?"
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
            System.out.println(e.getMessage());
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
                    "INSERT INTO " + PARTICIPANTS_TABLE + " (athlete_id, event_id) VALUES (?, ?)"
            );
            insertParticipant.setInt(1, athleteId);
            insertParticipant.setInt(2, eventId);
            insertParticipant.executeUpdate();

            updateEventCount = conn.prepareStatement(
                    "UPDATE " + EVENTS_TABLE + " SET current_players = current_players + 1 WHERE event_id = ?"
            );
            updateEventCount.setInt(1, eventId);
            updateEventCount.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackException) {
                System.out.println(rollbackException.getMessage());
            }

            System.out.println(e.getMessage());
            return false;

        } finally {
            try {
                if (insertParticipant != null) insertParticipant.close();
                if (updateEventCount != null) updateEventCount.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
