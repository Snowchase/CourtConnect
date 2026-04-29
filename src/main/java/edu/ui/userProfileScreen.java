package edu.ui;

import javax.swing.*;
import java.awt.*;
import edu.Controller.Controller;

public class userProfileScreen extends JFrame {

    private Controller controller;
    private int athleteId;
    private String role;

    public userProfileScreen(int athleteId, String role) {
        this.athleteId = athleteId;
        this.role = role;
        this.controller = new Controller();

        Color backgroundColor = new Color(245, 247, 250);
        Color primaryColor = new Color(52, 152, 219);
        Color headerColor = new Color(44, 62, 80);

        setTitle("Court Connect - Athlete Profile");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(backgroundColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Athlete Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(headerColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JTextField usernameField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField skillField = new JTextField(20);
        JTextField sexField = new JTextField(20);

        Object[] profile = controller.getAthleteProfile(athleteId);

        if (profile != null) {
            usernameField.setText(profile[0] != null ? profile[0].toString() : "");
            nameField.setText(profile[1] != null ? profile[1].toString() : "");
            ageField.setText(profile[2] != null ? profile[2].toString() : "");
            skillField.setText(profile[3] != null ? profile[3].toString() : "");
            sexField.setText(profile[4] != null ? profile[4].toString() : "");
        }

        usernameField.setEditable(false);

        int row = 1;
        addField(panel, gbc, row++, "Username:", usernameField, headerColor);
        addField(panel, gbc, row++, "Name:", nameField, headerColor);
        addField(panel, gbc, row++, "Age:", ageField, headerColor);
        addField(panel, gbc, row++, "Skill Level:", skillField, headerColor);
        addField(panel, gbc, row++, "Sex:", sexField, headerColor);

        JButton updateButton = new JButton("Update Profile");
        JButton backButton = new JButton("Back");

        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        updateButton.setFont(buttonFont);
        updateButton.setBackground(primaryColor);
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);

        backButton.setFont(buttonFont);
        backButton.setBackground(primaryColor);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(updateButton, gbc);

        gbc.gridx = 1;
        panel.add(backButton, gbc);

        add(panel);

        updateButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String skill = skillField.getText().trim();
                String sex = sexField.getText().trim();

                if (name.isEmpty() || ageField.getText().trim().isEmpty()
                        || skill.isEmpty() || sex.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all profile fields.");
                    return;
                }

                int age = Integer.parseInt(ageField.getText().trim());

                boolean success = controller.updateAthleteProfile(
                        athleteId, name, age, skill, sex
                );

                if (success) {
                    JOptionPane.showMessageDialog(this, "Profile updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update profile.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age must be a number.");
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new homeScreen(athleteId, role).setVisible(true);
        });
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String labelText,
                          JTextField field, Color labelColor) {
        JLabel label = new JLabel(labelText);
        label.setForeground(labelColor);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }
}