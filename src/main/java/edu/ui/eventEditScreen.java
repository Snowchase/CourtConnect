package edu.ui;

import javax.swing.*;
import java.awt.*;
import edu.Controller.Controller;

public class eventEditScreen extends JFrame {
    private JPanel eventEditPanel;
    private Controller controller;
    private int athleteId;
    private String role;
    private int eventId;

    public eventEditScreen(int athleteId, String role, int eventId) {
        this.athleteId = athleteId;
        this.role = role;
        this.eventId = eventId;
        this.controller = new Controller();

        Color backgroundColor = new Color(245, 247, 250);
        Color primaryColor = new Color(52, 152, 219);
        Color headerColor = new Color(44, 62, 80);

        Object[] eventDetails = controller.getEventDetails(eventId, athleteId);

        if (eventDetails == null) {
            JOptionPane.showMessageDialog(this, "You can only edit events that you created.");
            dispose();
            new homeScreen(athleteId, role).setVisible(true);
            return;
        }

        setTitle("Court Connect - Edit Event");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        eventEditPanel = new JPanel(new GridBagLayout());
        eventEditPanel.setBackground(backgroundColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Edit Sporting Event");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(headerColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        eventEditPanel.add(titleLabel, gbc);

        JTextField eventNameField = new JTextField(20);
        JTextArea descriptionArea = new JTextArea(5, 15);
        JTextField eventDateField = new JTextField(10);
        JTextField locationField = new JTextField(20);
        JTextField maxPlayersField = new JTextField(10);

        String[] sports = {"Basketball", "Soccer", "Tennis", "Volleyball", "Badminton"};
        JComboBox<String> sportComboBox = new JComboBox<>(sports);

        eventNameField.setText(eventDetails[1] != null ? eventDetails[1].toString() : "");
        sportComboBox.setSelectedItem(eventDetails[2] != null ? eventDetails[2].toString() : "Basketball");
        eventDateField.setText(eventDetails[3] != null ? eventDetails[3].toString() : "");
        locationField.setText(eventDetails[4] != null ? eventDetails[4].toString() : "");
        descriptionArea.setText(eventDetails[5] != null ? eventDetails[5].toString() : "");
        maxPlayersField.setText(eventDetails[7] != null ? eventDetails[7].toString() : "");

        int currentPlayers = Integer.parseInt(eventDetails[6].toString());

        int row = 1;

        addTextField(eventEditPanel, gbc, row++, "Event Name:", eventNameField, headerColor);
        addComboBox(eventEditPanel, gbc, row++, "Sport:", sportComboBox, headerColor);
        addTextField(eventEditPanel, gbc, row++, "Date (YYYY-MM-DD):", eventDateField, headerColor);
        addTextField(eventEditPanel, gbc, row++, "Location:", locationField, headerColor);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descriptionLabel.setForeground(headerColor);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        eventEditPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        eventEditPanel.add(new JScrollPane(descriptionArea), gbc);
        row++;

        addTextField(eventEditPanel, gbc, row++, "Max Players:", maxPlayersField, headerColor);

        JLabel currentPlayersLabel = new JLabel("Current Players: " + currentPlayers);
        currentPlayersLabel.setFont(new Font("Arial", Font.BOLD, 14));
        currentPlayersLabel.setForeground(headerColor);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        eventEditPanel.add(currentPlayersLabel, gbc);

        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");

        styleButton(saveButton, primaryColor);
        styleButton(cancelButton, primaryColor);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        eventEditPanel.add(saveButton, gbc);

        gbc.gridx = 1;
        eventEditPanel.add(cancelButton, gbc);

        add(eventEditPanel);

        saveButton.addActionListener(e -> {
            try {
                String eventName = eventNameField.getText().trim();
                String sport = sportComboBox.getSelectedItem().toString();
                String eventDate = eventDateField.getText().trim();
                String location = locationField.getText().trim();
                String description = descriptionArea.getText().trim();

                if (eventName.isEmpty() || eventDate.isEmpty() || location.isEmpty() || description.isEmpty()
                        || maxPlayersField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
                    return;
                }

                int maxPlayers = Integer.parseInt(maxPlayersField.getText().trim());

                String result = controller.updateEvent(
                        eventId, athleteId, eventName, sport, eventDate, location, description, maxPlayers
                );

                if (result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "Event updated successfully.");
                    dispose();
                    new homeScreen(athleteId, role).setVisible(true);
                } else if (result.equals("MAX_TOO_LOW")) {
                    JOptionPane.showMessageDialog(this,
                            "Max players cannot be lower than the current number of joined players.");
                } else if (result.equals("NOT_OWNER")) {
                    JOptionPane.showMessageDialog(this, "You can only edit events that you created.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update event.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Max Players must be a number.");
            }
        });

        cancelButton.addActionListener(e -> {
            dispose();
            new homeScreen(athleteId, role).setVisible(true);
        });
    }

    private void addTextField(JPanel panel, GridBagConstraints gbc, int row, String labelText,
                              JTextField field, Color labelColor) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(labelColor);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void addComboBox(JPanel panel, GridBagConstraints gbc, int row, String labelText,
                             JComboBox<String> comboBox, Color labelColor) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(labelColor);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(comboBox, gbc);
    }

    private void styleButton(JButton button, Color primaryColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
}