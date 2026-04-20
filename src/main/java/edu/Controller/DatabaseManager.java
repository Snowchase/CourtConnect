package edu.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:CourtConnect.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS athletes (" +
                    "athlete_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL, " +
                    "name TEXT, " +
                    "age INTEGER, " +
                    "skill_level TEXT, " +
                    "sex TEXT" +
                    ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS sporting_events (" +
                    "sportingid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "event_name TEXT NOT NULL, " +
                    "sport TEXT NOT NULL, " +
                    "event_date TEXT NOT NULL, " +
                    "location TEXT NOT NULL, " +
                    "description TEXT, " +
                    "current_players INTEGER NOT NULL DEFAULT 0, " +
                    "max_players INTEGER NOT NULL" +
                    ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS event_participants (" +
                    "athlete_id INTEGER NOT NULL, " +
                    "sportingid INTEGER NOT NULL, " +
                    "PRIMARY KEY (athlete_id, sportingid), " +
                    "FOREIGN KEY (athlete_id) REFERENCES athletes(athlete_id), " +
                    "FOREIGN KEY (sportingid) REFERENCES sporting_events(sportingid)" +
                    ")");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM sporting_events");
            int count = rs.getInt("count");
            rs.close();

            if (count == 0) {
                stmt.execute("INSERT INTO sporting_events (event_name, sport, event_date, location, description, current_players, max_players) VALUES " +
                        "('City Arena Pickup', 'Basketball', '2026-04-20', 'San Marcos Gym', 'Casual basketball run', 0, 10)," +
                        "('Campus Soccer Match', 'Soccer', '2026-04-21', 'CSUSM Field', 'Friendly soccer game', 0, 14)," +
                        "('Weekend Tennis Rally', 'Tennis', '2026-04-22', 'North County Courts', 'Practice and rally session', 0, 4)," +
                        "('Beach Volleyball Meetup', 'Volleyball', '2026-04-23', 'Carlsbad Beach Courts', 'Fun volleyball meetup', 0, 12)," +
                        "('Rec Center Badminton', 'Badminton', '2026-04-24', 'Rec Center Hall', 'Indoor badminton session', 0, 6)");
            }

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }
}