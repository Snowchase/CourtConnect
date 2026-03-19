package edu;
//Import databaiseInitializer to create tables
import edu.DatabaseResources.DatabaseInitializer;
import edu.ui.loginScreen;
//File Name: App.java

//Group: 3
//Date: 3/37/2026
//Description: This class is for launching application and routing to login or registration screen
public class App
{
    public static void main( String[] args )
    {
        //Initialize Database values and tables if not already created
        DatabaseInitializer databaseinit = new DatabaseInitializer();
        databaseinit.initializeDatabase();
        
        System.out.println( "This is the Court Connect application." );
        //Start the application by showing the main screen with options to login or register
        new loginScreen().setVisible(true);
        //If user presses register routesthem to registration screen outlined in loginScreen.java
        //If user presses login routes them to login screen outlined in loginScreen.java


    }
}
