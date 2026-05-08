package edu.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteEventCreateManager {

    public boolean createEvent(String eventName, String sport, String eventDate, String location,
                               String description, int maxPlayers, int createdBy) {
        String sql = "INSERT INTO sporting_events " +
                "(event_name, sport, event_date, location, description, current_players, max_players, created_by, status) " +
                "VALUES (?, ?, ?, ?, ?, 0, ?, ?, 'Active')";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, eventName);
            pstmt.setString(2, sport);
            pstmt.setString(3, eventDate);
            pstmt.setString(4, location);
            pstmt.setString(5, description);
            pstmt.setInt(6, maxPlayers);
            pstmt.setInt(7, createdBy);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("createEvent error: " + e.getMessage());
            return false;
        }
    }

    public boolean isEventCreatedByOrganizer(int eventId, int organizerId) {
        String sql = "SELECT 1 FROM sporting_events WHERE sportingid = ? AND created_by = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, eventId);
            pstmt.setInt(2, organizerId);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("isEventCreatedByOrganizer error: " + e.getMessage());
            return false;
        }
    }

    public Object[] getEventDetails(int eventId, int organizerId) {
        String sql = "SELECT sportingid, event_name, sport, event_date, location, description, current_players, max_players " +
                "FROM sporting_events WHERE sportingid = ? AND created_by = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, eventId);
            pstmt.setInt(2, organizerId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Object[] {
                        rs.getInt("sportingid"),
                        rs.getString("event_name"),
                        rs.getString("sport"),
                        rs.getString("event_date"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getInt("current_players"),
                        rs.getInt("max_players")
                };
            }

        } catch (SQLException e) {
            System.out.println("getEventDetails error: " + e.getMessage());
        }

        return null;
    }

    public String updateEvent(int eventId, int organizerId, String eventName, String sport,
                              String eventDate, String location, String description, int maxPlayers) {
        String checkSql = "SELECT current_players FROM sporting_events WHERE sportingid = ? AND created_by = ?";
        String updateSql = "UPDATE sporting_events SET event_name = ?, sport = ?, event_date = ?, location = ?, " +
                "description = ?, max_players = ? WHERE sportingid = ? AND created_by = ?";

        try (Connection conn = DatabaseManager.getConnection()) {

            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, eventId);
            checkStmt.setInt(2, organizerId);

            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                rs.close();
                checkStmt.close();
                return "NOT_OWNER";
            }

            int currentPlayers = rs.getInt("current_players");

            rs.close();
            checkStmt.close();

            if (maxPlayers < currentPlayers) {
                return "MAX_TOO_LOW";
            }

            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, eventName);
            updateStmt.setString(2, sport);
            updateStmt.setString(3, eventDate);
            updateStmt.setString(4, location);
            updateStmt.setString(5, description);
            updateStmt.setInt(6, maxPlayers);
            updateStmt.setInt(7, eventId);
            updateStmt.setInt(8, organizerId);

            int rowsUpdated = updateStmt.executeUpdate();
            updateStmt.close();

            return rowsUpdated > 0 ? "SUCCESS" : "UPDATE_FAILED";

        } catch (SQLException e) {
            System.out.println("updateEvent error: " + e.getMessage());
            return "UPDATE_FAILED";
        }
    }

    public boolean deleteEvent(int eventId, int organizerId) {
        Connection conn = null;
        PreparedStatement checkOwner = null;
        PreparedStatement deleteParticipants = null;
        PreparedStatement deleteEvent = null;

        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            checkOwner = conn.prepareStatement(
                    "SELECT 1 FROM sporting_events WHERE sportingid = ? AND created_by = ?"
            );
            checkOwner.setInt(1, eventId);
            checkOwner.setInt(2, organizerId);

            ResultSet ownerResult = checkOwner.executeQuery();
            boolean ownsEvent = ownerResult.next();
            ownerResult.close();

            if (!ownsEvent) {
                conn.rollback();
                return false;
            }

            deleteParticipants = conn.prepareStatement(
                    "DELETE FROM event_participants WHERE sportingid = ?"
            );
            deleteParticipants.setInt(1, eventId);
            deleteParticipants.executeUpdate();

            deleteEvent = conn.prepareStatement(
                    "DELETE FROM sporting_events WHERE sportingid = ? AND created_by = ?"
            );
            deleteEvent.setInt(1, eventId);
            deleteEvent.setInt(2, organizerId);

            int rowsDeleted = deleteEvent.executeUpdate();

            if (rowsDeleted > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackException) {
                System.out.println("Rollback error: " + rollbackException.getMessage());
            }

            System.out.println("deleteEvent error: " + e.getMessage());
            return false;

        } finally {
            try {
                if (checkOwner != null) checkOwner.close();
                if (deleteParticipants != null) deleteParticipants.close();
                if (deleteEvent != null) deleteEvent.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Closing error: " + e.getMessage());
            }
        }
    }

    public boolean markEventCompleted(int eventId, int organizerId) {
        String sql = "UPDATE sporting_events SET status = 'Completed' WHERE sportingid = ? AND created_by = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, eventId);
            pstmt.setInt(2, organizerId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("markEventCompleted error: " + e.getMessage());
            return false;
        }
    }
}