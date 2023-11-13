package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginScreen extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private DatabaseConnectionHandler dbHandler;
    private DatabaseOperations dbOperations;

    public LoginScreen(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
        this.dbOperations = new DatabaseOperations();
        createUI();
    }

    private void createUI() {
        setTitle("Login - Trains of Sheffield");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        add(loginButton);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try {
            dbHandler.openConnection(); // Open the database connection
            Connection connection = dbHandler.getConnection();

            boolean isAuthenticated = dbOperations.authenticateUser(email, password, connection);

            if (isAuthenticated) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                // Proceed with further actions post-login
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error.");
            e.printStackTrace();
        } finally {
            dbHandler.closeConnection(); // Close the database connection
        }
    }
}
