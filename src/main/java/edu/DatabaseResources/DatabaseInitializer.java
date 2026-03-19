package edu.DatabaseResources;

import java.sql.*;
//File Name: DatabaseInitializer.java
//Group: 3
//Date: 3/37/2026
//Description: This class creates database and tables for athletes/sporting Events if  dont previously exist
public class DatabaseInitializer {
    public void initializeDatabase() {
        // Implementation for initializing the athletes/sporting events database and tables
        try {
            // Create connection to SQLite database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Connection to SQLite has been established.");

            // Create statement for creating tables
            Statement stmt = conn.createStatement();

            // SQL statement to create athletes table if it doesn't exist
            String createAthletesTable = "CREATE TABLE IF NOT EXISTS athletes (" +
                    "username TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL" +
                    ");";
            stmt.execute(createAthletesTable);
            System.out.println("Athletes table created or already exists.");

            // SQL statement to create sporting_events table if it doesn't exist
            String createSportingEventsTable = "CREATE TABLE IF NOT EXISTS sporting_events (" +
                    "sportingid INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "event_name TEXT NOT NULL," +
                    "event_date TEXT NOT NULL" +
                    ");";
            stmt.execute(createSportingEventsTable);
            System.out.println("Sporting Events table created or already exists.");
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
