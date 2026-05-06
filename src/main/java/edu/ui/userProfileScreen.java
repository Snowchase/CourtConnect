package edu.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
        setSize(550, 450);
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
        JTextField sexField = new JTextField(20);

        Object[] profile = controller.getAthleteProfile(athleteId);

        if (profile != null) {
            usernameField.setText(profile[0] != null ? profile[0].toString() : "");
            nameField.setText(profile[1] != null ? profile[1].toString() : "");
            ageField.setText(profile[2] != null ? profile[2].toString() : "");
            sexField.setText(profile[4] != null ? profile[4].toString() : "");
        }

        usernameField.setEditable(false);

        int row = 1;

        addTextField(panel, gbc, row++, "Username:", usernameField, headerColor);
        addTextField(panel, gbc, row++, "Name:", nameField, headerColor);
        addTextField(panel, gbc, row++, "Age:", ageField, headerColor);
        addTextField(panel, gbc, row++, "Sex:", sexField, headerColor);

        JLabel skillSectionLabel = new JLabel("Sport-Specific Skill Level");
        skillSectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        skillSectionLabel.setForeground(headerColor);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panel.add(skillSectionLabel, gbc);

        String[] sports = {"Basketball", "Soccer", "Tennis", "Volleyball", "Badminton"};
        JComboBox<String> sportBox = new JComboBox<>(sports);

        JComboBox<String> skillBox = new JComboBox<>();
        for (int i = 1; i <= 10; i++) {
            skillBox.addItem(String.valueOf(i));
        }

        sportBox.addActionListener(e -> {
            String selectedSport = sportBox.getSelectedItem().toString();
            int savedSkill = controller.getSportSkill(athleteId, selectedSport);
            skillBox.setSelectedItem(String.valueOf(savedSkill));
        });

        String firstSport = sportBox.getSelectedItem().toString();
        int savedSkill = controller.getSportSkill(athleteId, firstSport);
        skillBox.setSelectedItem(String.valueOf(savedSkill));

        addComboBox(panel, gbc, row++, "Sport:", sportBox, headerColor);
        addComboBox(panel, gbc, row++, "Skill Level (1-10):", skillBox, headerColor);

        JButton updateProfileButton = new JButton("Update Profile");
        JButton saveSkillButton = new JButton("Save Sport Skill");
        JButton backButton = new JButton("Back");

        styleButton(updateProfileButton, primaryColor);
        styleButton(saveSkillButton, primaryColor);
        styleButton(backButton, primaryColor);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(updateProfileButton, gbc);

        gbc.gridx = 1;
        panel.add(saveSkillButton, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(backButton, gbc);

        add(panel);

        updateProfileButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String sex = sexField.getText().trim();

                if (name.isEmpty() || ageField.getText().trim().isEmpty() || sex.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all profile fields.");
                    return;
                }

                int age = Integer.parseInt(ageField.getText().trim());

                boolean success = controller.updateAthleteProfile(
                        athleteId, name, age, "", sex
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

        saveSkillButton.addActionListener(e -> {
            String selectedSport = sportBox.getSelectedItem().toString();
            int selectedSkill = Integer.parseInt(skillBox.getSelectedItem().toString());

            boolean success = controller.saveSportSkill(athleteId, selectedSport, selectedSkill);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        selectedSport + " skill level saved as " + selectedSkill + ".");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save sport skill.");
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new homeScreen(athleteId, role).setVisible(true);
        });
    }

    private void addTextField(JPanel panel, GridBagConstraints gbc, int row, String labelText,
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

    private void addComboBox(JPanel panel, GridBagConstraints gbc, int row, String labelText,
                             JComboBox<String> comboBox, Color labelColor) {
        JLabel label = new JLabel(labelText);
        label.setForeground(labelColor);
        label.setFont(new Font("Arial", Font.BOLD, 14));

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