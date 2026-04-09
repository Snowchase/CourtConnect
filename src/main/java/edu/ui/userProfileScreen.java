package edu.ui;

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
