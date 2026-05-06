package edu.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteSportSkillManager {

    public boolean saveSportSkill(int athleteId, String sport, int skillLevel) {
        String sql = "INSERT OR REPLACE INTO athlete_sport_skills " +
                "(athlete_id, sport, skill_level) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, athleteId);
            pstmt.setString(2, sport);
            pstmt.setInt(3, skillLevel);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("saveSportSkill error: " + e.getMessage());
            return false;
        }
    }

    public int getSportSkill(int athleteId, String sport) {
        String sql = "SELECT skill_level FROM athlete_sport_skills WHERE athlete_id = ? AND sport = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, athleteId);
            pstmt.setString(2, sport);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("skill_level");
            }

        } catch (SQLException e) {
            System.out.println("getSportSkill error: " + e.getMessage());
        }

        return 1;
    }
}