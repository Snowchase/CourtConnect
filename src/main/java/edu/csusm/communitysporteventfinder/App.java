package edu.csusm.communitysporteventfinder;

import edu.csusm.communitysporteventfinder.ui.loginScreen;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "This is the Court Connect application." );
        //Start the application by showing the main screen with options to login or register
        new loginScreen().setVisible(true);
        //If user presses register route them to registration screen
        


        //If user presses login route them to login screen
    }
}
