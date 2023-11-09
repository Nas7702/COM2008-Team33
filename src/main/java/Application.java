//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.HashMap;
//import java.util.Map;
//
//// Application class that extends JFrame to create the main application window
//public class Application extends JFrame {
//
//    // Layout to switch between different panels like login, registration, and customer panel
//    private CardLayout cardLayout;
//    private JPanel cardPanel;
//    // Components for login panel
//    private JTextField usernameField;
//    private JPasswordField passwordField;
//    // Components for registration panel
//    private JTextField regUsernameField;
//    private JPasswordField regPasswordField;
//    private JPasswordField regConfirmPasswordField;
//    // Simulated database for user credentials
//    private Map<String, String> userDatabase = new HashMap<>();
//    private final JFrame frame;
//
//    // Constructor for Application class
//    public static void main(String[] args) {
//
//        frame = new JFrame("Trains of Sheffield");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(500, 350);
//
//        cardLayout = new CardLayout();
//        cardPanel = new JPanel(cardLayout);
//
//        // Create panels for login, registration and customer interface
//        JPanel loginPanel = createLoginPanel();
//        JPanel registrationPanel = createRegistrationPanel();
//        JPanel customerPanel = createCustomerPanel();
//
//        // Add panels to cardPanel with a String identifier
//        cardPanel.add(loginPanel, "LoginPanel");
//        cardPanel.add(registrationPanel, "RegistrationPanel");
//        cardPanel.add(customerPanel, "CustomerPanel");
//
//        // Show the login panel initially
//        cardLayout.show(cardPanel, "LoginPanel");
//
//        frame.add(cardPanel);
//        frame.setVisible(true);
//    }
//
//    // Method to create the login panel with form fields and buttons
//    private JPanel createLoginPanel() {
//        JPanel loginPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.insets = new Insets(10, 10, 10, 10);
//
//        // Username label and text field
//        constraints.gridx = 0;
//        constraints.gridy = 0;
//        loginPanel.add(new JLabel("Username:"), constraints);
//
//        usernameField = new JTextField(20);
//        constraints.gridx = 1;
//        loginPanel.add(usernameField, constraints);
//
//        // Password label and password field
//        constraints.gridx = 0;
//        constraints.gridy = 1;
//        loginPanel.add(new JLabel("Password:"), constraints);
//
//        passwordField = new JPasswordField(20);
//        constraints.gridx = 1;
//        loginPanel.add(passwordField, constraints);
//
//        // Login button with action to authenticate
//        JButton loginButton = new JButton("Login");
//        loginButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (authenticate(usernameField.getText(), new String(passwordField.getPassword()))) {
//                    // On success, switch to the customer panel
//                    cardLayout.show(cardPanel, "CustomerPanel");
//                } else {
//                    // On failure, show an error message
//                    JOptionPane.showMessageDialog(frame, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//
//        constraints.gridy = 2;
//        constraints.gridwidth = 2;
//        loginPanel.add(loginButton, constraints);
//
//        // Register button to switch to the registration panel
//        JButton registerButton = new JButton("Register");
//        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "RegistrationPanel"));
//
//        constraints.gridy = 3;
//        loginPanel.add(registerButton, constraints);
//
//        return loginPanel;
//    }
//
//    // Method to create the registration panel with form fields and buttons
//    private JPanel createRegistrationPanel() {
//        JPanel registrationPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.insets = new Insets(10, 10, 10, 10);
//
//        // Username label and text field for registration
//        constraints.gridx = 0;
//        constraints.gridy = 0;
//        registrationPanel.add(new JLabel("Username:"), constraints);
//
//        regUsernameField = new JTextField(20);
//        constraints.gridx = 1;
//        registrationPanel.add(regUsernameField, constraints);
//
//        // Password label and password field for registration
//        constraints.gridx = 0;
//        constraints.gridy = 1;
//        registrationPanel.add(new JLabel("Password:"), constraints);
//
//        regPasswordField = new JPasswordField(20);
//        constraints.gridx = 1;
//        registrationPanel.add(regPasswordField, constraints);
//
//        // Confirm password label and password field for registration
//        constraints.gridx = 0;
//        constraints.gridy = 2;
//        registrationPanel.add(new JLabel("Confirm Password:"), constraints);
//
//        regConfirmPasswordField = new JPasswordField(20);
//        constraints.gridx = 1;
//        registrationPanel.add(regConfirmPasswordField, constraints);
//
//        // Button for creating an account
//        JButton regButton = new JButton("Create Account");
//        regButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String username = regUsernameField.getText();
//                String password = new String(regPasswordField.getPassword());
//                String confirmPassword = new String(regConfirmPasswordField.getPassword());
//
//                // Check if the passwords match
//                if (!password.equals(confirmPassword)) {
//                    JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Registration Error", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//
//                // Check if the username already exists
//                if (userDatabase.containsKey(username)) {
//                    JOptionPane.showMessageDialog(frame, "Username already exists!", "Registration Error", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//
//                // Store new user credentials in the simulated database
//                userDatabase.put(username, password);
//                // Show a success message
//                JOptionPane.showMessageDialog(frame, "User registered successfully!", "Registration", JOptionPane.INFORMATION_MESSAGE);
//                // Go back to the login screen
//                cardLayout.show(cardPanel, "LoginPanel");
//            }
//        });
//
//        constraints.gridy = 3;
//        constraints.gridwidth = 2;
//        registrationPanel.add(regButton, constraints);
//
//        // Button to go back to the login screen
//        JButton backButton = new JButton("Back to Login");
//        backButton.addActionListener(e -> cardLayout.show(cardPanel, "LoginPanel"));
//
//        constraints.gridy = 4;
//        registrationPanel.add(backButton, constraints);
//
//        return registrationPanel;
//    }
//
//    // Method to create the customer panel with a welcome message and logout button
//    private JPanel createCustomerPanel() {
//        JPanel customerPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.insets = new Insets(10, 10, 10, 10);
//
//        // Welcome label
//        JLabel welcomeLabel = new JLabel("Welcome to the Customer Panel");
//        constraints.gridx = 0;
//        constraints.gridy = 0;
//        constraints.gridwidth = 2;
//        customerPanel.add(welcomeLabel, constraints);
//
//        // Logout button to return to the login screen
//        JButton logoutButton = new JButton("Logout");
//        logoutButton.addActionListener(e -> cardLayout.show(cardPanel, "LoginPanel"));
//        constraints.gridy = 1;
//        customerPanel.add(logoutButton, constraints);
//
//        return customerPanel;
//    }
//
//    // Helper method to authenticate a user
//    private boolean authenticate(String username, String password) {
//        String storedPassword = userDatabase.get(username);
//        return storedPassword != null && storedPassword.equals(password);
//    }
//
//    // Main method to run the application
//    public static void main(String[] args) {
//        // Schedule a job for the event-dispatching thread to create and show the application's GUI
//        SwingUtilities.invokeLater(Application::new);
//    }
//}
