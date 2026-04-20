package edu.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import edu.Controller.Controller;

public class homeScreen extends JFrame {
    private JPanel homePanel;
    private Controller controller;
    private int athleteId;
    private JTable eventTable;
    private JTable notificationTable;

    public homeScreen(int athleteId) {
        this.athleteId = athleteId;
        this.controller = new Controller();

        System.out.println("Opening home screen with athleteId = " + athleteId);

        setTitle("Court Connect - Home");
        setSize(1150, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        homePanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Court Connect!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton profileButton = new JButton("Profile");
        JButton createEventButton = new JButton("Create Event");
        JButton joinEventButton = new JButton("Join Event");
        JButton leaveEventButton = new JButton("Leave Event");
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(profileButton);
        bottomPanel.add(createEventButton);
        bottomPanel.add(joinEventButton);
        bottomPanel.add(leaveEventButton);
        bottomPanel.add(refreshButton);
        bottomPanel.add(logoutButton);

        eventTable = new JTable();
        refreshEventTable();

        notificationTable = new JTable();
        refreshNotificationTable();

        JScrollPane eventScrollPane = new JScrollPane(eventTable);
        JScrollPane notificationScrollPane = new JScrollPane(notificationTable);

        JPanel notificationPanel = new JPanel(new BorderLayout());
        JLabel notificationLabel = new JLabel("My Notifications / Joined Games", SwingConstants.CENTER);
        notificationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        notificationPanel.add(notificationLabel, BorderLayout.NORTH);
        notificationPanel.add(notificationScrollPane, BorderLayout.CENTER);
        notificationPanel.setPreferredSize(new Dimension(400, 0));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, eventScrollPane, notificationPanel);
        splitPane.setResizeWeight(0.7);

        homePanel.add(welcomeLabel, BorderLayout.NORTH);
        homePanel.add(splitPane, BorderLayout.CENTER);
        homePanel.add(bottomPanel, BorderLayout.SOUTH);

        add(homePanel);

        profileButton.addActionListener(e -> {
            dispose();
            new userProfileScreen(athleteId).setVisible(true);
        });

        createEventButton.addActionListener(e -> {
            dispose();
            new eventCreateScreen(athleteId).setVisible(true);
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
                String sport = eventTable.getValueAt(selectedRow, 1).toString();

                String result = controller.joinEvent(athleteId, eventId);

                if (result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "Successfully joined the " + sport + " event.");
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
                refreshNotificationTable();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid event selection.");
            }
        });

        leaveEventButton.addActionListener(e -> {
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
                String sport = eventTable.getValueAt(selectedRow, 1).toString();

                String result = controller.leaveEvent(athleteId, eventId);

                if (result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "Successfully left the " + sport + " event.");
                } else if (result.equals("ATHLETE_NOT_FOUND")) {
                    JOptionPane.showMessageDialog(this, "Athlete ID does not exist.");
                } else if (result.equals("EVENT_NOT_FOUND")) {
                    JOptionPane.showMessageDialog(this, "Event no longer exists.");
                } else if (result.equals("NOT_JOINED")) {
                    JOptionPane.showMessageDialog(this, "You have not joined this event.");
                } else {
                    JOptionPane.showMessageDialog(this, "Unable to leave event. Please try again.");
                }

                refreshEventTable();
                refreshNotificationTable();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid event selection.");
            }
        });

        refreshButton.addActionListener(e -> {
            refreshEventTable();
            refreshNotificationTable();
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new loginScreen().setVisible(true);
        });
    }

    private void refreshEventTable() {
        String[] columnNames = {
                "Event ID", "Sport", "Date", "Location", "Current Players", "Max Players"
        };

        DefaultTableModel model = new DefaultTableModel(controller.getAllEvents(), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        eventTable.setModel(model);
    }

    private void refreshNotificationTable() {
        String[] columnNames = {
                "Event Name", "Sport", "Date", "Location"
        };

        DefaultTableModel model = new DefaultTableModel(
                controller.getJoinedEventsForAthlete(athleteId),
                columnNames
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        notificationTable.setModel(model);
    }
}