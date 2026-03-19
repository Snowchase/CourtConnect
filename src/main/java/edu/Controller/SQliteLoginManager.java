package edu.Controller;

import java.sql.*;

//File Name: SQliteLoginManager.java
//Group: 3
//Date: 3/37/2026
//Description: This class manages all data requests to SQlite regarding login and returns data to the controller
//Import covers all imports such as Connect, DriverManager, statements, PreparedStatementsm, ResultSet, and SQLException.
public class SQliteLoginManager {
    //Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
    //Statement stmt = conn.createStatement();
    //PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
    //ResultSet rs = pstmt.executeQuery();

    //Managing login process, returns true if user is authenticated
    public SQliteLoginManager (String user, String pass) {
        try {
            //Create Connection to databas
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Connection to SQLite has been established.");
            //Create statement for Athlete validation
            Statement stmt = conn.createStatement();
            //Prepared statement to authenticate login info
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM athletes WHERE username = ? AND password = ?");
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            //Return results in resultset from searching table for user
            ResultSet rs = pstmt.executeQuery();
            //If the resultSet has information in it, user was found
            //Otherwise User was not found 
            if(rs.next()){
                System.out.println("User authenticated");
            } else {
                System.out.println("User not found");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
