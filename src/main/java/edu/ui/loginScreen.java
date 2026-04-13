package edu.ui;
//File Name: loginScreen.java
//Group: 3
//Edited last:  Chase
//Description: Initial Ui for login screen of application, Title and login boxes/login button and register option
//Date: 2/28/26

import javax.swing.*;
import edu.Controller.Controller;
import java.awt.*;

public class loginScreen extends JFrame {
    private Panel loginPanel;
    private Controller controller;

    public loginScreen() {
        controller = new Controller();

        setTitle("Court Connect");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loginPanel = new Panel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Welcome to Court Connect");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        loginPanel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password are required.");
                return;
            }

            final int athleteId = controller.login(username, password);

            if (athleteId != -1) {
                dispose();
                new homeScreen(athleteId).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        });

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        gbc.gridy = 3;
        loginPanel.add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            dispose();
            new registrationScreen().setVisible(true);
        });

        JLabel versionLabel = new JLabel("Version 1.0");
        gbc.gridx = 1;
        gbc.gridy = 4;
        loginPanel.add(versionLabel, gbc);

        add(loginPanel);
    }
}
