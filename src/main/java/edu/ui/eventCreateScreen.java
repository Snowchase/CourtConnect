package edu.ui;

import javax.swing.*;
import java.awt.*;
import edu.Controller.Controller;

public class eventCreateScreen extends JFrame {
    private JPanel eventCreatePanel;
    private Controller eventCreateController;

    public eventCreateScreen() {
        this.eventCreateController = new Controller();

        setTitle("Court Connect - Create Event");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        eventCreatePanel = new JPanel(new GridBagLayout());
        eventCreatePanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create Sporting Event");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        eventCreatePanel.add(titleLabel, gbc);

        JTextField eventNameField = new JTextField(20);
        JTextArea eventDescription = new JTextArea(5, 15);
        JTextField eventDateField = new JTextField(10);
        JTextField eventLocationField = new JTextField(20);
        JTextField maxPlayersField = new JTextField(10);

        String[] sports = {"Basketball", "Soccer", "Tennis", "Volleyball"};
        JComboBox<String> sportComboBox = new JComboBox<>(sports);

        int row = 1;

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        eventCreatePanel.add(new JLabel("Event Name:"), gbc);
        gbc.gridx = 1;
        eventCreatePanel.add(eventNameField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        eventCreatePanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        eventCreatePanel.add(new JScrollPane(eventDescription), gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        eventCreatePanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        eventCreatePanel.add(eventDateField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        eventCreatePanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        eventCreatePanel.add(eventLocationField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        eventCreatePanel.add(new JLabel("Sport:"), gbc);
        gbc.gridx = 1;
        eventCreatePanel.add(sportComboBox, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        eventCreatePanel.add(new JLabel("Max Players:"), gbc);
        gbc.gridx = 1;
        eventCreatePanel.add(maxPlayersField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        JButton createButton = new JButton("Create Event");
        eventCreatePanel.add(createButton, gbc);

        JButton cancelButton = new JButton("Cancel");
        gbc.gridx = 1;
        eventCreatePanel.add(cancelButton, gbc);

        add(eventCreatePanel);

        createButton.addActionListener(e -> {
            try {
                String eventName = eventNameField.getText().trim();
                String description = eventDescription.getText().trim();
                String eventDate = eventDateField.getText().trim();
                String location = eventLocationField.getText().trim();
                String sport = sportComboBox.getSelectedItem().toString();
                int maxPlayers = Integer.parseInt(maxPlayersField.getText().trim());

                if (eventName.isEmpty() || eventDate.isEmpty() || location.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
                    return;
                }

                boolean created = eventCreateController.createEvent(
                        eventName, sport, eventDate, location, description, maxPlayers
                );

                if (created) {
                    JOptionPane.showMessageDialog(this, "Event created successfully.");
                    dispose();
                    new homeScreen().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create event.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Max Players must be a number.");
            }
        });

        cancelButton.addActionListener(e -> {
            dispose();
            new homeScreen().setVisible(true);
        });

        setVisible(true);
    }
}
