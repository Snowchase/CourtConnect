package edu.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLiteEventCreateManager {

    public boolean createEvent(String eventName, String sport, String eventDate, String location, String description, int maxPlayers) {
        String sql = "INSERT INTO sporting_events (event_name, sport, event_date, location, description, current_players, max_players) VALUES (?, ?, ?, ?, ?, 0, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, eventName);
            pstmt.setString(2, sport);
            pstmt.setString(3, eventDate);
            pstmt.setString(4, location);
            pstmt.setString(5, description);
            pstmt.setInt(6, maxPlayers);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("createEvent error: " + e.getMessage());
            return false;
        }
    }
}