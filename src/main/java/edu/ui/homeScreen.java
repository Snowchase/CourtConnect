package edu.ui;

import javax.swing.*;
import java.awt.*;
import edu.Controller.Controller;

public class homeScreen extends JFrame {
    private Panel homePanel;
    private Controller controller;
    private int athleteId;
    private JTable eventTable;

    public homeScreen() {
        this(-1);
    }

    public homeScreen(int athleteId) {
        this.athleteId = athleteId;
        this.controller = new Controller();

        setTitle("Court Connect - Home");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        homePanel = new Panel();
        homePanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Court Connect!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton profileButton = new JButton("Profile");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton joinEventButton = new JButton("Join Event");
        JButton logoutButton = new JButton("Logout");

        JPanel topPanel = new JPanel();
        topPanel.add(profileButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(joinEventButton);
        topPanel.add(logoutButton);

        eventTable = new JTable(
                controller.getAllEvents(),
                new String[]{"Event ID", "Sport", "Date", "Location"}
        );

        JScrollPane tableScrollPane = new JScrollPane(eventTable);

        homePanel.add(welcomeLabel, BorderLayout.NORTH);
        homePanel.add(tableScrollPane, BorderLayout.CENTER);
        homePanel.add(topPanel, BorderLayout.SOUTH);

        add(homePanel);
        homePanel.setVisible(true);

        profileButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Profile screen not added yet.");
        });

        joinEventButton.addActionListener(e -> {
            try {
                if (athleteId == -1) {
                    JOptionPane.showMessageDialog(this, "No logged-in athlete found. Please log in again.");
                    return;
                }

                int selectedRow = eventTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event from the table first.");
                    return;
                }

                int eventId = Integer.parseInt(eventTable.getValueAt(selectedRow, 0).toString());

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

                refreshEventTable();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid event selection.");
            }
        });

        searchButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Search feature not fully implemented yet.");
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new loginScreen().setVisible(true);
        });
    }

    private void refreshEventTable() {
        eventTable.setModel(new javax.swing.table.DefaultTableModel(
                controller.getAllEvents(),
                new String[]{"Event ID", "Sport", "Date", "Location"}
        ));
    }
}
