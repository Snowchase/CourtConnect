package edu.csusm.communitysporteventfinder.ui;
//Ui for main login screen of application, will have Title and login boxes/login button and register option
//Date: 2/28/26
import javax.swing.*;
import java.awt.*;

public class loginScreen extends JFrame{
    private Panel loginPanel;

    public loginScreen(){
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
        //Logic needs to be added here to confirm that a user is authenticated before allowing access to software
        //For now, the home screen will be avaliable immedietley
        loginButton.addActionListener(e -> {
            dispose();
            new homeScreen().setVisible(true);
        });

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        gbc.gridy = 3;
        loginPanel.add(registerButton, gbc);
        registerButton.addActionListener(e -> {
            dispose();
            new registrationScreen().setVisible(true);
        });


        add(loginPanel);
    }
}
