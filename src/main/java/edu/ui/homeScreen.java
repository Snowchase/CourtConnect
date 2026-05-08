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

        Color backgroundColor = new Color(245, 247, 250);
        Color primaryColor = new Color(52, 152, 219);
        Color headerColor = new Color(44, 62, 80);
        Color gridColor = new Color(220, 220, 220);

        setTitle("Court Connect - Home");
        setSize(1700, 740);
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
        JButton editEventButton = new JButton("Edit Event");
        JButton deleteEventButton = new JButton("Delete Event");
        JButton completeEventButton = new JButton("Mark Completed");
        JButton viewParticipantsButton = new JButton("View Participants");
        JButton removeAthleteButton = new JButton("Remove Athlete");
        JButton joinEventButton = new JButton("Join Event");
        JButton leaveEventButton = new JButton("Leave Event");
        JButton mapButton = new JButton("View Map");
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");

        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        JButton[] buttons = {
                profileButton, createEventButton, editEventButton, deleteEventButton,
                completeEventButton, viewParticipantsButton, removeAthleteButton,
                joinEventButton, leaveEventButton, mapButton, refreshButton, logoutButton
        };

        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(new Dimension(150, 42));
            btn.setBackground(primaryColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        JPanel bottomPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.setBackground(backgroundColor);

        bottomPanel.add(profileButton);
        bottomPanel.add(createEventButton);
        bottomPanel.add(editEventButton);
        bottomPanel.add(deleteEventButton);
        bottomPanel.add(completeEventButton);
        bottomPanel.add(viewParticipantsButton);
        bottomPanel.add(removeAthleteButton);
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
        notificationPanel.setPreferredSize(new Dimension(500, 0));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, eventScrollPane, notificationPanel);
        splitPane.setResizeWeight(0.72);
        splitPane.setDividerSize(8);

        homePanel.add(welcomeLabel, BorderLayout.NORTH);
        homePanel.add(splitPane, BorderLayout.CENTER);
        homePanel.add(bottomPanel, BorderLayout.SOUTH);

        add(homePanel);

        if (role.equalsIgnoreCase("Athlete")) {
            createEventButton.setVisible(false);
            editEventButton.setVisible(false);
            deleteEventButton.setVisible(false);
            completeEventButton.setVisible(false);
            viewParticipantsButton.setVisible(false);
            removeAthleteButton.setVisible(false);
        }

        profileButton.addActionListener(e -> {
            dispose();
            new userProfileScreen(athleteId, role).setVisible(true);
        });

        createEventButton.addActionListener(e -> {
            dispose();
            new eventCreateScreen(athleteId, role).setVisible(true);
        });

        editEventButton.addActionListener(e -> {
            try {
                int selectedRow = eventTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event to edit.");
                    return;
                }

                int eventId = Integer.parseInt(eventTable.getValueAt(selectedRow, 0).toString());

                Object[] eventDetails = controller.getEventDetails(eventId, athleteId);

                if (eventDetails == null) {
                    JOptionPane.showMessageDialog(this, "You can only edit events that you created.");
                    return;
                }

                dispose();
                new eventEditScreen(athleteId, role, eventId).setVisible(true);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid event selection.");
            }
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

        completeEventButton.addActionListener(e -> {
            try {
                int selectedRow = eventTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event to mark completed.");
                    return;
                }

                int eventId = Integer.parseInt(eventTable.getValueAt(selectedRow, 0).toString());
                String sport = eventTable.getValueAt(selectedRow, 1).toString();
                String date = eventTable.getValueAt(selectedRow, 2).toString();

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Mark this " + sport + " event on " + date + " as completed?",
                        "Confirm Completion",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    String result = controller.completeEvent(eventId, athleteId);

                    if (result.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(this, "Event marked as completed.");
                    } else if (result.equals("NOT_OWNER")) {
                        JOptionPane.showMessageDialog(this, "You can only complete events that you created.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to complete event.");
                    }

                    refreshEventTable();
                    refreshNotificationTable();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid event selection.");
            }
        });

        viewParticipantsButton.addActionListener(e -> {
            try {
                int selectedRow = eventTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select one of your events first.");
                    return;
                }

                int eventId = Integer.parseInt(eventTable.getValueAt(selectedRow, 0).toString());
                String sport = eventTable.getValueAt(selectedRow, 1).toString();

                Object[][] participants = controller.getParticipantsForEvent(eventId, athleteId);

                if (participants == null) {
                    JOptionPane.showMessageDialog(this, "You can only view participants for events that you created.");
                    return;
                }

                if (participants.length == 0) {
                    JOptionPane.showMessageDialog(this, "No athletes have joined this event yet.");
                    return;
                }

                String[] columns = {"Athlete ID", "Username"};

                JTable participantTable = new JTable(participants, columns);
                participantTable.setRowHeight(28);
                participantTable.setEnabled(false);
                participantTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
                participantTable.setFont(new Font("Arial", Font.PLAIN, 14));

                JScrollPane scrollPane = new JScrollPane(participantTable);
                scrollPane.setPreferredSize(new Dimension(380, 220));

                JOptionPane.showMessageDialog(
                        this,
                        scrollPane,
                        "Participants for " + sport,
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading participants.");
            }
        });

        removeAthleteButton.addActionListener(e -> {
            try {
                int selectedRow = eventTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select one of your events first.");
                    return;
                }

                int eventId = Integer.parseInt(eventTable.getValueAt(selectedRow, 0).toString());
                String sport = eventTable.getValueAt(selectedRow, 1).toString();

                Object[][] participants = controller.getParticipantsForEvent(eventId, athleteId);

                if (participants == null) {
                    JOptionPane.showMessageDialog(this, "You can only remove athletes from events that you created.");
                    return;
                }

                if (participants.length == 0) {
                    JOptionPane.showMessageDialog(this, "No athletes have joined this event yet.");
                    return;
                }

                String[] participantOptions = new String[participants.length];

                for (int i = 0; i < participants.length; i++) {
                    participantOptions[i] = participants[i][0] + " - " + participants[i][1];
                }

                String selectedParticipant = (String) JOptionPane.showInputDialog(
                        this,
                        "Select an athlete to remove from the " + sport + " event:",
                        "Remove Athlete",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        participantOptions,
                        participantOptions[0]
                );

                if (selectedParticipant == null) {
                    return;
                }

                int athleteToRemoveId = Integer.parseInt(selectedParticipant.split(" - ")[0]);

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to remove this athlete from the event?",
                        "Confirm Remove",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    String result = controller.removeAthleteFromEvent(eventId, athleteId, athleteToRemoveId);

                    if (result.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(this, "Athlete removed successfully.");
                    } else if (result.equals("NOT_OWNER")) {
                        JOptionPane.showMessageDialog(this, "You can only remove athletes from events that you created.");
                    } else if (result.equals("ATHLETE_NOT_IN_EVENT")) {
                        JOptionPane.showMessageDialog(this, "This athlete is not currently joined to the event.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to remove athlete.");
                    }

                    refreshEventTable();
                    refreshNotificationTable();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error removing athlete.");
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
                "Event Name", "Sport", "Date", "Location", "Status"
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