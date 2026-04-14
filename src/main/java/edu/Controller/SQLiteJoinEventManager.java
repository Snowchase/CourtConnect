package edu.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteJoinEventManager {

    public boolean eventExists(int eventId) {
        String sql = "SELECT 1 FROM sporting_events WHERE sportingid = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("eventExists error: " + e.getMessage());
            return false;
        }
    }

    public boolean isEventFull(int eventId) {
        String sql = "SELECT current_players, max_players FROM sporting_events WHERE sportingid = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int currentPlayers = rs.getInt("current_players");
                int maxPlayers = rs.getInt("max_players");
                return currentPlayers >= maxPlayers;
            }

            return true;

        } catch (SQLException e) {
            System.out.println("isEventFull error: " + e.getMessage());
            return true;
        }
    }

    public boolean athleteExists(int athleteId) {
        String sql = "SELECT 1 FROM athletes WHERE athlete_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, athleteId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("athleteExists error: " + e.getMessage());
            return false;
        }
    }

    public boolean isAlreadyJoined(int athleteId, int eventId) {
        String sql = "SELECT 1 FROM event_participants WHERE athlete_id = ? AND sportingid = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, athleteId);
            pstmt.setInt(2, eventId);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("isAlreadyJoined error: " + e.getMessage());
            return false;
        }
    }

    public boolean addParticipant(int athleteId, int eventId) {
        String insertSql = "INSERT INTO event_participants (athlete_id, sportingid) VALUES (?, ?)";
        String updateSql = "UPDATE sporting_events SET current_players = current_players + 1 WHERE sportingid = ?";

        Connection conn = null;

        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement insertParticipant = conn.prepareStatement(insertSql);
                 PreparedStatement updateEventCount = conn.prepareStatement(updateSql)) {

                insertParticipant.setInt(1, athleteId);
                insertParticipant.setInt(2, eventId);
                insertParticipant.executeUpdate();

                updateEventCount.setInt(1, eventId);
                updateEventCount.executeUpdate();

                conn.commit();
                return true;
            }

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
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Closing error: " + e.getMessage());
            }
        }
    }

    public Object[][] getAllEvents() {
        List<Object[]> rows = new ArrayList<>();

        String sql = """
            SELECT sportingid, sport, event_date, location, current_players, max_players
            FROM sporting_events
            ORDER BY sportingid
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

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

        } catch (SQLException e) {
            System.out.println("getAllEvents error: " + e.getMessage());
        }

        return rows.toArray(new Object[0][]);
    }
}
