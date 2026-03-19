package edu.Controller;

import java.sql.*;

//File Name: SQliteRegistrationManager.java
//Group: 3
//Date: 3/37/2026
//Description: This class manages all data requests to SQlite regarding registrationand returns data to the controller
//Import covers all imports such as Connect, DriverManager, statements, PreparedStatementsm, ResultSet, and SQLException.
public class SQliteRegistrationManager {
    public SQliteRegistrationManager (String user, String pass) {
        try {
            //Create Connection to databas
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Connection to SQLite has been established.");
            //Create statement for Athlete validation
            Statement stmt = conn.createStatement();
            //Prepared statement to authenticate login info
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO athletes (username, password) VALUES (?, ?)");
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            //Execute update to insert new user into database
            pstmt.executeUpdate();
            System.out.println("User registered successfully");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}