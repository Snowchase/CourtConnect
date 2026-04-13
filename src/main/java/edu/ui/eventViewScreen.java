//File Name: eventViewScreen.java
//Group: 3
//Edited last:  Chase
//Description: Event view screen for application. Includes event details and view options.
//Date: 2/21/26
package edu.ui;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.Controller.Controller;

public class eventViewScreen extends JFrame{

    private JPanel eventViewPanel;
    private Controller eventViewController;

    public eventViewScreen() {
        this.eventViewController = new Controller();
        setTitle("Court Connect -View Event");
        setSize(400, 400);
        eventViewPanel = new JPanel();
        eventViewPanel.setLayout(new BorderLayout(0,12));
        eventViewPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("View sporting event");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        eventViewPanel.add(titleLabel, BorderLayout.NORTH);

        eventViewPanel = new JPanel(new GridBagLayout());
        eventViewPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        //To help adding labels and text fields
        int row;
        //Dropdown for sport selection, shouldnt be able to freely input sports
        String[] sports = {"Basketball", "Soccer", "Tennis", "Volleyball"};
        JComboBox<String> sportComboBox = new JComboBox<>(sports);

        //Adding labels and fields to the panel
        row = 0;
        eventViewPanel.add(new JLabel("Event Name:"), gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        eventViewPanel.add(new JLabel("Description:"), gbc);
        row++;
        gbc.gridy = row;
        eventViewPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        row++;
        gbc.gridy = row;
        eventViewPanel.add(new JLabel("Location:"), gbc);
        row++;
        gbc.gridy = row;
        eventViewPanel.add(new JLabel("Sport:"), gbc);
        eventViewPanel.add(sportComboBox, gbc);
        row++;
        gbc.gridy = row;
        JButton backButton = new JButton("back");
        gbc.gridx = 1;
        eventViewPanel.add(backButton,gbc);

        // Add panel to frame
        add(eventViewPanel);

        // Configure the JFrame itself
        setTitle("View Event");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
