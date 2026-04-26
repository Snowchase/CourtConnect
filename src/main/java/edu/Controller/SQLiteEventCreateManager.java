package edu.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteEventCreateManager {

    public boolean createEvent(String eventName, String sport, String eventDate, String location,
                               String description, int maxPlayers, int createdBy) {
        String sql = "INSERT INTO sporting_events " +
                "(event_name, sport, event_date, location, description, current_players, max_players, created_by) " +
                "VALUES (?, ?, ?, ?, ?, 0, ?, ?)";

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
}