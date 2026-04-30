package edu.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

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

        Color backgroundColor = new Color(245, 247, 250);
        Color primaryColor = new Color(52, 152, 219);
        Color headerColor = new Color(44, 62, 80);
        Color gridColor = new Color(220, 220, 220);

        setTitle("Court Connect - Home");
        setSize(1400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        homePanel = new JPanel(new BorderLayout(10, 10));
        homePanel.setBackground(backgroundColor);

        JLabel welcomeLabel = new JLabel("Welcome to Court Connect! - " + role, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(headerColor);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));

        JButton profileButton = new JButton("Profile");
        JButton createEventButton = new JButton("Create Event");
        JButton deleteEventButton = new JButton("Delete Event");
        JButton joinEventButton = new JButton("Join Event");
        JButton leaveEventButton = new JButton("Leave Event");
        JButton mapButton = new JButton("View Map");
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");

        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        JButton[] buttons = {
                profileButton, createEventButton, deleteEventButton,
                joinEventButton, leaveEventButton, mapButton,
                refreshButton, logoutButton
        };

        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(new Dimension(140, 42));
            btn.setBackground(primaryColor);
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        bottomPanel.setBackground(backgroundColor);

        bottomPanel.add(profileButton);
        bottomPanel.add(createEventButton);
        bottomPanel.add(deleteEventButton);
        bottomPanel.add(joinEventButton);
        bottomPanel.add(leaveEventButton);
        bottomPanel.add(mapButton);
        bottomPanel.add(refreshButton);
        bottomPanel.add(logoutButton);

        eventTable = new JTable();
        eventTable.setRowHeight(32);
        eventTable.setFont(new Font("Arial", Font.PLAIN, 15));
        eventTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        eventTable.setBackground(Color.WHITE);
        eventTable.setGridColor(gridColor);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshEventTable();

        notificationTable = new JTable();
        notificationTable.setRowHeight(32);
        notificationTable.setFont(new Font("Arial", Font.PLAIN, 15));
        notificationTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        notificationTable.setBackground(Color.WHITE);
        notificationTable.setGridColor(gridColor);
        notificationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshNotificationTable();

        JScrollPane eventScrollPane = new JScrollPane(eventTable);
        JScrollPane notificationScrollPane = new JScrollPane(notificationTable);

        JPanel notificationPanel = new JPanel(new BorderLayout());
        notificationPanel.setBackground(backgroundColor);

        JLabel notificationLabel = new JLabel("My Notifications / Joined Games", SwingConstants.CENTER);
        notificationLabel.setFont(new Font("Arial", Font.BOLD, 18));
        notificationLabel.setForeground(headerColor);
        notificationLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        notificationPanel.add(notificationLabel, BorderLayout.NORTH);
        notificationPanel.add(notificationScrollPane, BorderLayout.CENTER);
        notificationPanel.setPreferredSize(new Dimension(450, 0));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, eventScrollPane, notificationPanel);
        splitPane.setResizeWeight(0.75);
        splitPane.setDividerSize(8);

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
            new userProfileScreen(athleteId, role).setVisible(true);;
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