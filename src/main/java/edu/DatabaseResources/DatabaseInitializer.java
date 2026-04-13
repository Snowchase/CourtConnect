package edu.DatabaseResources;

import java.sql.*;

//File Name: DatabaseInitializer.java
//Group: 3
//Edited last: 
//Date: 3/37/2026
//Description: This class creates database and tables for athletes/sporting events if they do not previously exist
public class DatabaseInitializer {
    public void initializeDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Connection to SQLite has been established.");

            Statement stmt = conn.createStatement();

            String createAthletesTable = "CREATE TABLE IF NOT EXISTS athletes (" +
                    "athlete_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL" +
                    ");";
            stmt.execute(createAthletesTable);
            System.out.println("Athletes table created or already exists.");

            String createSportingEventsTable = "CREATE TABLE IF NOT EXISTS sporting_events (" +
                    "sportingid INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "event_name TEXT NOT NULL," +
                    "sport TEXT NOT NULL," +
                    "event_date TEXT NOT NULL," +
                    "location TEXT NOT NULL," +
                    "description TEXT," +
                    "max_players INTEGER NOT NULL DEFAULT 10," +
                    "current_players INTEGER NOT NULL DEFAULT 0" +
                    ");";
            stmt.execute(createSportingEventsTable);
            System.out.println("Sporting Events table created or already exists.");

            String createEventParticipantsTable = "CREATE TABLE IF NOT EXISTS event_participants (" +
                    "participant_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "athlete_id INTEGER NOT NULL," +
                    "sportingid INTEGER NOT NULL," +
                    "UNIQUE(athlete_id, sportingid)," +
                    "FOREIGN KEY (athlete_id) REFERENCES athletes(athlete_id)," +
                    "FOREIGN KEY (sportingid) REFERENCES sporting_events(sportingid)" +
                    ");";
            stmt.execute(createEventParticipantsTable);
            System.out.println("Event Participants table created or already exists.");

            stmt.execute("INSERT OR IGNORE INTO athletes (athlete_id, username, password) VALUES (1, 'testuser', '1234');");
            stmt.execute("INSERT OR IGNORE INTO athletes (athlete_id, username, password) VALUES (2, 'player2', 'abcd');");
            System.out.println("Sample athletes inserted.");

            stmt.execute("INSERT OR IGNORE INTO sporting_events (sportingid, event_name, sport, event_date, location, description, max_players, current_players) VALUES " +
                    "(1, 'City Arena Pickup', 'Basketball', '2026-04-15', 'City Arena', 'Casual basketball run', 10, 2);");
            stmt.execute("INSERT OR IGNORE INTO sporting_events (sportingid, event_name, sport, event_date, location, description, max_players, current_players) VALUES " +
                    "(2, 'Park Tennis Rally', 'Tennis', '2026-04-20', 'Central Park', 'Intermediate doubles practice', 4, 1);");
            stmt.execute("INSERT OR IGNORE INTO sporting_events (sportingid, event_name, sport, event_date, location, description, max_players, current_players) VALUES " +
                    "(3, 'Weekend Soccer Match', 'Soccer', '2026-05-01', 'Stadium A', 'Friendly 11v11 match', 22, 5);");
            System.out.println("Sample sporting events inserted.");

            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
