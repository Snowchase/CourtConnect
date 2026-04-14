package edu.ui;

import edu.Controller.Controller;
import java.awt.*;
import javax.swing.*;

public class userProfileScreen extends JFrame {
    private JPanel userProfilePanel;
    private Controller userProfileController;

    public userProfileScreen() {
        this.userProfileController = new Controller();

        setTitle("Court Connect - Profile");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        userProfilePanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("User profile screen coming soon.", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));

        userProfilePanel.add(label, BorderLayout.CENTER);
        add(userProfilePanel);
    }
}
