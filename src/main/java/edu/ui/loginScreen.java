package edu.ui;

import javax.swing.*;
import java.awt.*;
import edu.Controller.Controller;

public class loginScreen extends JFrame {
    private JPanel loginPanel;
    private Controller controller;

    public loginScreen() {
        controller = new Controller();

        Color backgroundColor = new Color(245, 247, 250);
        Color primaryColor = new Color(52, 152, 219);
        Color headerColor = new Color(44, 62, 80);

        setTitle("Court Connect");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(backgroundColor);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Welcome to Court Connect");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(headerColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 12, 12, 12);
        loginPanel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(headerColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(headerColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(primaryColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 3;
        loginPanel.add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(primaryColor);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);

        gbc.gridx = 1;
        loginPanel.add(registerButton, gbc);

        JLabel versionLabel = new JLabel("Version 1.0");
        versionLabel.setForeground(headerColor);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        loginPanel.add(versionLabel, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password are required.");
                return;
            }

            int athleteId = controller.login(username, password);

            if (athleteId != -1) {
                String role = controller.getUserRole(username, password);
                dispose();
                new homeScreen(athleteId, role).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        });

        registerButton.addActionListener(e -> {
            dispose();
            new registrationScreen().setVisible(true);
        });

        add(loginPanel);
    }
}