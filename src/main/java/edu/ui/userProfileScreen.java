package edu.ui;
//File Name: userProfileScreen.java
//Group: 3
//Edited last:  Chase
//Description: User profile screen for application. Includes user details and profile options.
//Date: 2/21/26
import edu.Controller.Controller;
import java.awt.*;
import javax.swing.*;

public class userProfileScreen extends JFrame{
    private Panel userProfilePanel;
    private Controller userProfileController;

    public userProfileScreen() {
        this.userProfileController = userProfileController;
        userProfilePanel = new Panel();

        userProfilePanel.setLayout(new GridLayout(1,1));
        userProfilePanel.setVisible(true);
    }
}
