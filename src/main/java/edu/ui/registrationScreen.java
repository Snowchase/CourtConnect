package edu.ui;
//File Name: registrationScreen.java
//Group: 3
//Edited last:  Chase
//Description: Ui for registration screen of application. Includes fields for username, password, confirm password, register button, and back button to login screen.
//Date: 2/28/26

import javax.swing.*;
import java.awt.*;
import edu.Controller.Controller;

public class registrationScreen extends JFrame {
    private Panel registrationPanel;
    private Controller controller;

    public registrationScreen() {
        controller = new Controller();

        setTitle("Court Connect - Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        registrationPanel = new Panel();
        registrationPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Register for Court Connect");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        registrationPanel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        registrationPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        registrationPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        registrationPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        registrationPanel.add(passwordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        registrationPanel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        registrationPanel.add(confirmPasswordField, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 4;
        registrationPanel.add(registerButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridx = 1;
        gbc.gridy = 4;
        registrationPanel.add(backButton, gbc);

        backButton.addActionListener(e -> {
            dispose();
            new loginScreen().setVisible(true);
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            boolean registered = controller.register(username, password);

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
