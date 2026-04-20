package edu.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteProfileManager {

    public Object[] getAthleteProfile(int athleteId) {
        String sql = "SELECT username, name, age, skill_level, sex FROM athletes WHERE athlete_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, athleteId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Object[] {
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getObject("age"),
                        rs.getString("skill_level"),
                        rs.getString("sex")
                };
            }

        } catch (SQLException e) {
            System.out.println("getAthleteProfile error: " + e.getMessage());
        }

        return null;
    }

    public boolean updateAthleteProfile(int athleteId, String name, int age, String skillLevel, String sex) {
        String sql = "UPDATE athletes SET name = ?, age = ?, skill_level = ?, sex = ? WHERE athlete_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, skillLevel);
            pstmt.setString(4, sex);
            pstmt.setInt(5, athleteId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("updateAthleteProfile error: " + e.getMessage());
            return false;
        }
    }
}