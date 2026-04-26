package edu.ui;

import javax.swing.*;
import java.awt.*;
import edu.Controller.Controller;

public class registrationScreen extends JFrame {
    private JPanel registrationPanel;
    private Controller controller;

    public registrationScreen() {
        controller = new Controller();

        setTitle("Court Connect - Registration");
        setSize(450, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        registrationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Register for Court Connect");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 12, 12, 12);
        registrationPanel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        registrationPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        registrationPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        registrationPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        registrationPanel.add(passwordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        registrationPanel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        registrationPanel.add(confirmPasswordField, gbc);

        JLabel roleLabel = new JLabel("Role:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        registrationPanel.add(roleLabel, gbc);

        String[] roles = {"Athlete", "Organizer"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        gbc.gridx = 1;
        registrationPanel.add(roleComboBox, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 5;
        registrationPanel.add(registerButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridx = 1;
        registrationPanel.add(backButton, gbc);

        backButton.addActionListener(e -> {
            dispose();
            new loginScreen().setVisible(true);
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
            String role = roleComboBox.getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            boolean registered = controller.register(username, password, role);

            if (registered) {
                JOptionPane.showMessageDialog(this, "Registration successful. Please log in.");
                dispose();
                new loginScreen().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Username may already exist.");
            }
        });

        add(registrationPanel);
    }
}