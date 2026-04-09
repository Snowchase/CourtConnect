package edu.ui;

import javax.swing.*;
import java.awt.*;
import edu.Controller.Controller;

public class eventCreateScreen extends JFrame {
    private JPanel eventCreatePanel;
    private Controller eventCreateController;

    public eventCreateScreen() {
        this.eventCreateController = new Controller();

        eventCreatePanel = new JPanel();
        eventCreatePanel.setLayout(new BorderLayout());
        eventCreatePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Create Event for Court Connect");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        eventCreatePanel.add(titleLabel, BorderLayout.NORTH);

        JLabel eventCreateLabel = new JLabel("Event:");
        eventCreatePanel.add(eventCreateLabel, BorderLayout.CENTER);
        JTextField eventNameTextField = new JTextField();
        eventCreatePanel.add(eventNameTextField, BorderLayout.NORTH);
        //Neds date, location, description, and sport fields
        //JTextField eventDescriptionTextField = new JTextField();
        //eventCreatePanel.add(eventDescriptionTextField, BorderLayout.CENTER);

        // Add panel to frame
        add(eventCreatePanel);

        // Configure the JFrame itself
        setTitle("Create Event");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
