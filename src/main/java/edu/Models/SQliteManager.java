package edu.Models;

import java.sql.*;

//File Name: SQliteManager.java
//Group: 3
//Date: 3/37/2026
//Description: This class manages all data requests to SQlite and returns data to the controller
//Import covers all imports such as Connect, DriverManager, statements, PreparedStatementsm, ResultSet, and SQLException.
public class SQliteManager {
    //Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
    //Statement stmt = conn.createStatement();
    //PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
    //ResultSet rs = pstmt.executeQuery();
    
    public SQliteManager() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
