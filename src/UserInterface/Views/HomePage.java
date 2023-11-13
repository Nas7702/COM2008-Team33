package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    private JButton loginButton;
    private JButton signupButton;
    private DatabaseConnectionHandler dbHandler;

    // Constructor that takes a DatabaseConnectionHandler instance
    public HomePage(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;  // Initialize the dbHandler
        createUI();                 // Set up the user interface
    }

    // Method to set up the UI components
    private void createUI() {
        setTitle("Home - Trains of Sheffield");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new FlowLayout());

        // Login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginScreen(); // Open the Login Screen when clicked
            }
        });
        add(loginButton);

        // Signup button
        signupButton = new JButton("Sign Up");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignupScreen(); // Open the Signup Screen when clicked
            }
        });
        add(signupButton);
    }

    // Method to open the Login Screen
    private void openLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(this.dbHandler); // Use the dbHandler from this instance
        loginScreen.setVisible(true);
    }

    // Method to open the Signup Screen
    private void openSignupScreen() {
        // Placeholder for opening the signup screen
        JOptionPane.showMessageDialog(this, "Signup Screen goes here.");
    }
}
