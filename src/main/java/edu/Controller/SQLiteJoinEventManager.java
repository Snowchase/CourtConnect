package edu.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLiteJoinEventManager {

    public boolean eventExists(int eventId) {
        try {
            Connection conn = DatabaseManager.getConnection();
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
            Connection conn = DatabaseManager.getConnection();
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
            Connection conn = DatabaseManager.getConnection();
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
            Connection conn = DatabaseManager.getConnection();
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
            conn = DatabaseManager.getConnection();
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
                if (conn != null) {
                    conn.rollback();
                }
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

    public boolean removeParticipant(int athleteId, int eventId) {
        Connection conn = null;
        PreparedStatement deleteParticipant = null;
        PreparedStatement updateEventCount = null;

        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            deleteParticipant = conn.prepareStatement(
                    "DELETE FROM event_participants WHERE athlete_id = ? AND sportingid = ?"
            );
            deleteParticipant.setInt(1, athleteId);
            deleteParticipant.setInt(2, eventId);

            int rowsDeleted = deleteParticipant.executeUpdate();

            if (rowsDeleted == 0) {
                conn.rollback();
                return false;
            }

            updateEventCount = conn.prepareStatement(
                    "UPDATE sporting_events " +
                    "SET current_players = CASE WHEN current_players > 0 THEN current_players - 1 ELSE 0 END " +
                    "WHERE sportingid = ?"
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
                System.out.println("Rollback error: " + rollbackException.getMessage());
            }

            System.out.println("removeParticipant error: " + e.getMessage());
            return false;

        } finally {
            try {
                if (deleteParticipant != null) deleteParticipant.close();
                if (updateEventCount != null) updateEventCount.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Closing error: " + e.getMessage());
            }
        }
    }

    public boolean leaveEvent(int athleteId, int eventId) {
        if (!athleteExists(athleteId)) {
            return false;
        }

        if (!eventExists(eventId)) {
            return false;
        }

        if (!isAlreadyJoined(athleteId, eventId)) {
            return false;
        }

        return removeParticipant(athleteId, eventId);
    }

    public Object[][] getAllEvents() {
        List<Object[]> rows = new ArrayList<>();

        try {
            Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT sportingid, sport, event_date, location, current_players, max_players FROM sporting_events ORDER BY sportingid"
            );

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("sportingid"),
                        rs.getString("sport"),
                        rs.getString("event_date"),
                        rs.getString("location"),
                        rs.getInt("current_players"),
                        rs.getInt("max_players")
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

    public Object[][] getJoinedEventsForAthlete(int athleteId) {
        List<Object[]> rows = new ArrayList<>();

        try {
            Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT se.event_name, se.sport, se.event_date, se.location " +
                    "FROM sporting_events se " +
                    "JOIN event_participants ep ON se.sportingid = ep.sportingid " +
                    "WHERE ep.athlete_id = ? " +
                    "ORDER BY se.event_date"
            );
            pstmt.setInt(1, athleteId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getString("event_name"),
                        rs.getString("sport"),
                        rs.getString("event_date"),
                        rs.getString("location")
                });
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("getJoinedEventsForAthlete error: " + e.getMessage());
        }

        return rows.toArray(new Object[0][]);
    }
}