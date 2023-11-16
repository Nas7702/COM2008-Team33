package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginScreen extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel titleLabel;
    private JPanel mainPanel;
    private DatabaseConnectionHandler dbHandler;
    private DatabaseOperations dbOperations;

    public LoginScreen(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
        this.dbOperations = new DatabaseOperations();
        createUI();
    }

    private void createUI() {
        setTitle("Login - Trains of Sheffield");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel("Login to your account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, emailField.getPreferredSize().height));
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(emailLabel);
        mainPanel.add(emailField);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this::handleLogin);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(loginButton);
//        mainPanel.add(new JLabel("<html><body style='width: 200px;'>TESTING: test@gmail.com, 123 - Make sure you're connected to the DB</body></html>")); // delete
        add(mainPanel);
    }

    private void handleLogin(ActionEvent e) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();

            DatabaseOperations.AuthenticationResult authResult =
                    dbOperations.authenticateUser(email, password, connection);

            if (authResult.isAuthenticated()) {
                User user = dbOperations.getUserDetails(email, connection);
                if (user != null) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    this.dispose(); // Close the login window

                    HomePage homePage = new HomePage(dbHandler, user);
                    homePage.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "User not found!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error.");
            ex.printStackTrace();
        } finally {
            dbHandler.closeConnection();
        }
    }

}
