package edu.ui;

import javax.swing.*;
import java.awt.*;
import edu.Controller.Controller;

public class eventCreateScreen extends JFrame {
    private JPanel eventCreatePanel;
    private Controller eventCreateController;
    private JPanel buttonPanel = new JPanel();
    public eventCreateScreen() {
        this.eventCreateController = new Controller();
        setTitle("Court Connect -Create Event");
        setSize(400, 400);
        eventCreatePanel = new JPanel();
        eventCreatePanel.setLayout(new BorderLayout(0,12));
        eventCreatePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Create sporting event");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        eventCreatePanel.add(titleLabel, BorderLayout.NORTH);

        eventCreatePanel = new JPanel(new GridBagLayout());
        eventCreatePanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        //To help adding labels and text fields
        int row;
        //Neds date, location, description, and sport fields
        //JTextField eventDescriptionTextField = new JTextField();
        //eventCreatePanel.add(eventDescriptionTextField, BorderLayout.CENTER);
        JTextField eventNameField = new JTextField(20);
        JTextArea eventDescription = new JTextArea(5, 15);
        JTextField eventDateField = new JTextField(10);
        JTextField eventLocationField = new JTextField(20);
        //Dropdown for sport selection, shouldnt be able to freely input sports
        String[] sports = {"Basketball", "Soccer", "Tennis", "Volleyball"};
        JComboBox<String> sportComboBox = new JComboBox<>(sports);

        //Adding labels and fields to the panel
        row = 0;
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
        JButton createButton = new JButton("Create Event");
        eventCreatePanel.add(createButton,gbc);
        JButton cancelButton = new JButton("Cancel");
        gbc.gridx = 1;
        eventCreatePanel.add(cancelButton,gbc);

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
