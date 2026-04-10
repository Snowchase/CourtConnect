package edu.ui;
//File Name: homeScreen.java
//Group: 3
//Description: Home screen for application. Includes profile button, search field,
//search button, logout button, event table, and join event feature.
//Date: 2/21/26

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.Controller.Controller;

public class homeScreen extends JFrame {
    private Panel homePanel;
    private Controller controller;

    public homeScreen() {
        setTitle("Court Connect - Home");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        controller = new Controller();

        homePanel = new Panel();
        homePanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Court Connect!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton profileButton = new JButton("Profile");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton createEventButton = new JButton("Create Event");
        JButton joinEventButton = new JButton("Join Event");
        JButton logoutButton = new JButton("Logout");
        JButton viewButton = new JButton("View Event");
        JPanel topPanel = new JPanel();
        topPanel.add(profileButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(createEventButton);
        topPanel.add(joinEventButton);
        topPanel.add(viewButton);
        topPanel.add(logoutButton);


        JTable eventTable = new JTable(
                new Object[][]{
                        {"1", "Basketball", "2026-04-15", "City Arena"},
                        {"2", "Tennis", "2026-04-20", "Central Park"},
                        {"3", "Soccer", "2026-05-01", "Stadium A"}
                },
                new String[]{"Event ID", "Sport", "Date", "Location"}
        );

        JScrollPane tableScrollPane = new JScrollPane(eventTable);

        homePanel.add(welcomeLabel, BorderLayout.NORTH);
        homePanel.add(tableScrollPane, BorderLayout.CENTER);
        homePanel.add(topPanel, BorderLayout.SOUTH);

        add(homePanel);
        homePanel.setVisible(true);

        profileButton.addActionListener(e ->{
            dispose();
            new userProfileScreen().setVisible(true);
        });

        createEventButton.addActionListener(e ->{
                dispose();
                new eventCreateScreen().setVisible(true);
                });
        //This method takes click point of a clicked event and will show event details
        eventTable.addMouseListener(new MouseAdapter() {
            public void click(MouseEvent e){
                int row = eventTable.rowAtPoint(e.getPoint());
                if(e.getClickCount() == 2){
                    dispose();
                    new eventViewScreen();
                }
            }
        });

        joinEventButton.addActionListener(e -> {
            try {
                String athleteIdText = JOptionPane.showInputDialog(this, "Enter Athlete ID:");
                if (athleteIdText == null || athleteIdText.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Athlete ID is required.");
                    return;
                }

                String eventIdText = JOptionPane.showInputDialog(this, "Enter Event ID:");
                if (eventIdText == null || eventIdText.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Event ID is required.");
                    return;
                }

                int athleteId = Integer.parseInt(athleteIdText.trim());
                int eventId = Integer.parseInt(eventIdText.trim());

                String result = controller.joinEvent(athleteId, eventId);

                if (result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "Successfully joined the event.");
                } else if (result.equals("ATHLETE_NOT_FOUND")) {
                    JOptionPane.showMessageDialog(this, "Athlete ID does not exist.");
                } else if (result.equals("EVENT_NOT_FOUND")) {
                    JOptionPane.showMessageDialog(this, "Event no longer exists.");
                } else if (result.equals("EVENT_FULL")) {
                    JOptionPane.showMessageDialog(this, "This event is already full.");
                } else if (result.equals("ALREADY_JOINED")) {
                    JOptionPane.showMessageDialog(this, "You have already joined this event.");
                } else {
                    JOptionPane.showMessageDialog(this, "Unable to join event. Please try again.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Athlete ID and Event ID must be numbers.");
            }
        });

        viewButton.addActionListener(e -> {
            dispose();
            new eventViewScreen().setVisible(true);
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new loginScreen().setVisible(true);
        });
    }
}
