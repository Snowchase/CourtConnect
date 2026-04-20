package edu.ui;

import edu.Controller.Controller;
import java.awt.*;
import javax.swing.*;

public class userProfileScreen extends JFrame {
    private JPanel userProfilePanel;
    private Controller userProfileController;
    private int athleteId;

    public userProfileScreen(int athleteId) {
        this.athleteId = athleteId;
        this.userProfileController = new Controller();

        System.out.println("Opening profile screen with athleteId = " + athleteId);

        setTitle("Court Connect - Athlete Profile");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        userProfilePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Athlete Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        Object[] profile = null;
        if (athleteId != -1) {
            profile = userProfileController.getAthleteProfile(athleteId);
        }

        String username = "";
        String name = "";
        String ageText = "";
        String skillLevel = "";
        String sex = "";

        if (profile != null) {
            username = profile[0] != null ? profile[0].toString() : "";
            name = profile[1] != null ? profile[1].toString() : "";
            ageText = profile[2] != null ? profile[2].toString() : "";
            skillLevel = profile[3] != null ? profile[3].toString() : "";
            sex = profile[4] != null ? profile[4].toString() : "";
        } else {
            System.out.println("Profile lookup returned null for athleteId = " + athleteId);
        }

        JTextField usernameField = new JTextField(username, 20);
        usernameField.setEditable(false);

        JTextField nameField = new JTextField(name, 20);
        JTextField ageField = new JTextField(ageText, 20);
        JTextField skillLevelField = new JTextField(skillLevel, 20);
        JTextField sexField = new JTextField(sex, 20);

        JButton updateButton = new JButton("Update Profile");
        JButton backButton = new JButton("Back");

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        userProfilePanel.add(titleLabel, gbc);

        row++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        userProfilePanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        userProfilePanel.add(usernameField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        userProfilePanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        userProfilePanel.add(nameField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        userProfilePanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        userProfilePanel.add(ageField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        userProfilePanel.add(new JLabel("Skill Level:"), gbc);
        gbc.gridx = 1;
        userProfilePanel.add(skillLevelField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        userProfilePanel.add(new JLabel("Sex:"), gbc);
        gbc.gridx = 1;
        userProfilePanel.add(sexField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        userProfilePanel.add(updateButton, gbc);
        gbc.gridx = 1;
        userProfilePanel.add(backButton, gbc);

        updateButton.addActionListener(e -> {
            try {
                if (athleteId == -1) {
    JOptionPane.showMessageDialog(this, "No valid athlete profile is loaded.");
    return;
}

                String updatedName = nameField.getText().trim();
                String updatedSkillLevel = skillLevelField.getText().trim();
                String updatedSex = sexField.getText().trim();

                if (updatedName.isEmpty() || ageField.getText().trim().isEmpty()
                        || updatedSkillLevel.isEmpty() || updatedSex.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all profile fields.");
                    return;
                }

                int updatedAge = Integer.parseInt(ageField.getText().trim());

                boolean updated = userProfileController.updateAthleteProfile(
                        athleteId, updatedName, updatedAge, updatedSkillLevel, updatedSex
                );

                System.out.println("Update attempted for athleteId = " + athleteId + ", success = " + updated);

                if (updated) {
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
            new homeScreen(athleteId).setVisible(true);
        });

        add(userProfilePanel);
    }
}