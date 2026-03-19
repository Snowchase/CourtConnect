package edu.ui;
//File Name: homeScreen.java
//Group: 3
//Description: Ui for registration screen of application, will have registration boxes for uusername, password, confirm password, email, and a register button. Should also bave the ability to go back to mainScreen
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
        
        //Main screen will consist of profile button, serach text box,
        //Search button, logout button, and Sport Event Table
        JButton profileButton = new JButton("Profile");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton logoutButton = new JButton("Logout");
        JPanel topPanel = new JPanel();

        //UI buttons and search box at top of screen
        topPanel.add(profileButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(logoutButton);

        //Event table will be in center of screen below buttons
        JTable eventTable = new JTable(new Object[][] {
            {"Basketball", "2024-03-15", "City Arena"},
            {"Tennis", "2024-04-10", "Central Park"},
            {"Soccer", "2024-05-20", "Stadium A"}
        }, new String[] {"Sport", "Date", "Location"});
        topPanel.add(new JScrollPane(eventTable));

        homePanel.add(welcomeLabel, BorderLayout.NORTH);
        homePanel.add(topPanel, BorderLayout.CENTER);
        add(homePanel);
        homePanel.setVisible(true);

        logoutButton.addActionListener(e -> {
            dispose();
            new loginScreen().setVisible(true);
        });
    }
}
