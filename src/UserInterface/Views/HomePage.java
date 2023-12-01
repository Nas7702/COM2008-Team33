package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.Cart;
import Models.User;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    private JButton loginButton;
    private JButton signupButton;
    private JButton logoutButton;
    private JButton productCatalogButton;
    private JButton manageInventoryButton;
    private JButton staffHomePage;
    private JButton manageAccountsButton;
    private JButton viewDetailsButton;
    private JButton viewOrderHistoryButton;
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private Cart cart;

    public HomePage(DatabaseConnectionHandler dbHandler, User user) {
        this.dbHandler = dbHandler;
        this.loggedInUser = user; // Store the logged-in user
        String role = loggedInUser != null ? user.getRole().toString() : null; // Get role from User
        createUI(role);
    }

    private void createUI(String role) {
        setTitle("Trains of Sheffield");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        // Styling
        Color buttonColor = new Color(100, 149, 237);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Font titleFont = new Font("Arial", Font.BOLD, 24);

        // Show login and signup buttons only if the user is not logged in
        if (loggedInUser == null) {
            setTitle("Trains of Sheffield - Login / Signup");
            JLabel titleLabel = new JLabel("Welcome to Trains of Sheffield");
            titleLabel.setFont(titleFont);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            loginButton = new JButton("Login");
            styleButton(loginButton, buttonColor, buttonFont);
            loginButton.addActionListener(e -> openLoginScreen());

            signupButton = new JButton("Sign Up");
            signupButton.addActionListener(e -> openSignupScreen());
            styleButton(signupButton, buttonColor, buttonFont);

            add(titleLabel);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(loginButton);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(signupButton);
        }

        else {
            setTitle("Trains of Sheffield - Home");
            setLayout(new BorderLayout()); // Set BorderLayout for the main frame

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centers components
            JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser.getForename());
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
            topPanel.add(welcomeLabel); // Add welcome label to the centered top panel
            add(topPanel, BorderLayout.NORTH); // Add top panel to the top of the frame

            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 10 is the gap between buttons
            viewDetailsButton = new JButton("View My Details");
            viewDetailsButton.addActionListener(e -> viewUserDetails());
            buttonsPanel.add(viewDetailsButton);

            productCatalogButton = new JButton("View Product Catalog");
            productCatalogButton.addActionListener(e -> viewProductCatalog());
            buttonsPanel.add(productCatalogButton);

            viewOrderHistoryButton = new JButton("View Order History");
            viewOrderHistoryButton.addActionListener(e -> viewOrderHistory());
            buttonsPanel.add(viewOrderHistoryButton);

//            viewOrderButton = new JButton("View your order");
//            viewOrderButton.addActionListener(e -> viewOrderScreen()); // Corrected action listener
//            buttonsPanel.add(viewOrderButton);

            add(buttonsPanel, BorderLayout.CENTER); // Add buttons panel to the center

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Last argument is vertical gap
            logoutButton = new JButton("Logout");
            logoutButton.addActionListener(e -> logout());
            bottomPanel.add(logoutButton);
            // Add the panel to the bottom of the frame
            if (!loggedInUser.getRole().equals(User.userRole.CUSTOMER)) {
                staffHomePage = new JButton("Staff Page");
                staffHomePage.addActionListener(e -> staffPage());
                bottomPanel.add(staffHomePage);
            }
            add(bottomPanel, BorderLayout.SOUTH);
        }
    }

    private void styleButton(JButton button, Color color, Font font) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }

    private void openLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(dbHandler);
        loginScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }

    private void openSignupScreen() {
        SignUp signupScreen = new SignUp(dbHandler); // Create a new SignUp screen
        signupScreen.setVisible(true); // Make the SignUp screen visible
        this.dispose(); // Close the HomePage
    }

    private void logout() {
        HomePage loginScreen = new HomePage(dbHandler, null);
        loginScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }

    private void viewUserDetails() {
        UserDetailsScreen userDetailsScreen = new UserDetailsScreen(dbHandler, loggedInUser);
        userDetailsScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }

    private void viewProductCatalog() {
        ProductCatalogScreen catalogScreen = new ProductCatalogScreen(dbHandler, loggedInUser);
        catalogScreen.setVisible(true);
        this.dispose();
    }

//    private void viewOrderScreen() {
//        ViewOrderScreen viewOrderScreen = new ViewOrderScreen(dbHandler, loggedInUser);
//        viewOrderScreen.setVisible(true);
//        this.dispose();
//    }

    private void viewOrderHistory() {
        ViewOrderHistory viewOrderHistory = new ViewOrderHistory(dbHandler, loggedInUser);
        viewOrderHistory.setVisible(true);
        this.dispose();
    }

    private void staffPage() {
        StaffHomePage staffScreen = new StaffHomePage(dbHandler, loggedInUser);
        staffScreen.setVisible(true);
        this.dispose();
    }

    private void manageInventory() {
        // Implementation for managing inventory
        // ...
    }

    private void manageSales() {
        // Implementation for managing sales
        // ...
    }

    private void manageUserAccounts() {
        ManageUsersScreen manageUsersScreen = new ManageUsersScreen(dbHandler, loggedInUser);
        manageUsersScreen.setVisible(true);
        this.dispose();
    }

    // Other methods...
    // ...
}
