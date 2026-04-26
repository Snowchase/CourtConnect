package edu.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import edu.Controller.Controller;

public class homeScreen extends JFrame {
    private JPanel homePanel;
    private Controller controller;
    private int athleteId;
    private String role;
    private JTable eventTable;
    private JTable notificationTable;

    public homeScreen(int athleteId, String role) {
        this.athleteId = athleteId;
        this.role = role;
        this.controller = new Controller();

        setTitle("Court Connect - Home");
        setSize(1200, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        homePanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Court Connect! - " + role, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton profileButton = new JButton("Profile");
        JButton createEventButton = new JButton("Create Event");
        JButton deleteEventButton = new JButton("Delete Event");
        JButton joinEventButton = new JButton("Join Event");
        JButton leaveEventButton = new JButton("Leave Event");
        JButton mapButton = new JButton("View Map");
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(profileButton);
        bottomPanel.add(createEventButton);
        bottomPanel.add(deleteEventButton);
        bottomPanel.add(joinEventButton);
        bottomPanel.add(leaveEventButton);
        bottomPanel.add(mapButton);
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

        if (role.equalsIgnoreCase("Athlete")) {
            createEventButton.setVisible(false);
            deleteEventButton.setVisible(false);
        }

        profileButton.addActionListener(e -> {
            dispose();
            new userProfileScreen(athleteId).setVisible(true);
        });

        createEventButton.addActionListener(e -> {
            dispose();
            new eventCreateScreen(athleteId, role).setVisible(true);
        });

        deleteEventButton.addActionListener(e -> {
            try {
                int selectedRow = eventTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event to delete.");
                    return;
                }

                int eventId = Integer.parseInt(eventTable.getValueAt(selectedRow, 0).toString());
                String sport = eventTable.getValueAt(selectedRow, 1).toString();
                String date = eventTable.getValueAt(selectedRow, 2).toString();

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this " + sport + " event on " + date + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    String result = controller.deleteEvent(eventId, athleteId);

                    if (result.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(this, "Event deleted successfully.");
                    } else if (result.equals("NOT_OWNER")) {
                        JOptionPane.showMessageDialog(this, "You can only delete events that you created.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete event.");
                    }

                    refreshEventTable();
                    refreshNotificationTable();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid event selection.");
            }
        });

        joinEventButton.addActionListener(e -> {
            try {
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
                } else if (result.equals("EVENT_FULL")) {
                    JOptionPane.showMessageDialog(this, "This event is already full.");
                } else if (result.equals("ALREADY_JOINED")) {
                    JOptionPane.showMessageDialog(this, "You have already joined this event.");
                } else {
                    JOptionPane.showMessageDialog(this, "Unable to join event.");
                }

                refreshEventTable();
                refreshNotificationTable();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid event selection.");
            }
        });

        leaveEventButton.addActionListener(e -> {
            try {
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
                } else if (result.equals("NOT_JOINED")) {
                    JOptionPane.showMessageDialog(this, "You have not joined this event.");
                } else {
                    JOptionPane.showMessageDialog(this, "Unable to leave event.");
                }

                refreshEventTable();
                refreshNotificationTable();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid event selection.");
            }
        });

        mapButton.addActionListener(e -> {
            try {
                int selectedRow = eventTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event first.");
                    return;
                }

                String location = eventTable.getValueAt(selectedRow, 3).toString();
                String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8.toString());
                String mapUrl = "https://www.google.com/maps/search/?api=1&query=" + encodedLocation;

                Desktop.getDesktop().browse(new URI(mapUrl));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Unable to open Google Maps.");
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