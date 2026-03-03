package edu.csusm.communitysporteventfinder.ui;
//Ui for registration screen of application, will have registration boxes for uusername, password, confirm password, email, and a register button. Should also bave the ability to go back to mainScreen
//Date: 2/28/26
import javax.swing.*;
import java.awt.*;

public class homeScreen extends JFrame{
    private Panel homePanel;

    public homeScreen(){
        setTitle("Court Connect - Home");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        homePanel = new Panel();
        homePanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Court Connect!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        

    }
}
