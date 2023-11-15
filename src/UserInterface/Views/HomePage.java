package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    private JButton loginButton;
    private JButton signupButton;
    private JButton logoutButton; // New logout button
    private JButton productCatalogButton;
    private JButton manageInventoryButton;
    private JButton manageSalesButton;
    private JButton manageAccountsButton;
    private JButton viewDetailsButton; // Button to view user details
    private DatabaseConnectionHandler dbHandler;
    private String userRole; // Added field to store the user's role

    public HomePage(DatabaseConnectionHandler dbHandler, String role) {
        this.dbHandler = dbHandler;
        this.userRole = role; // Store the user's role
        createUI(role);
    }

    private void createUI(String role) {
        setTitle("Home - Trains of Sheffield");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 1));

        productCatalogButton = new JButton("View Product Catalog");
        productCatalogButton.addActionListener(e -> viewProductCatalog());
        add(productCatalogButton);

        // Show login and signup buttons only if the user is not logged in
        if (role == null || role.isEmpty()) {
            loginButton = new JButton("Login");
            loginButton.addActionListener(e -> openLoginScreen());
            add(loginButton);

            signupButton = new JButton("Sign Up");
            signupButton.addActionListener(e -> openSignupScreen());
            add(signupButton);
        } else {
            // Show logout button and view details button if the user is logged in
            logoutButton = new JButton("Logout");
            logoutButton.addActionListener(e -> logout());
            add(logoutButton);

            viewDetailsButton = new JButton("View My Details");
            viewDetailsButton.addActionListener(e -> viewUserDetails());
            add(viewDetailsButton);
        }

        // Role-specific UI components
        if ("manager".equals(role)) {
            manageInventoryButton = new JButton("Manage Inventory");
            manageInventoryButton.addActionListener(e -> manageInventory());
            add(manageInventoryButton);

            manageSalesButton = new JButton("Manage Sales");
            manageSalesButton.addActionListener(e -> manageSales());
            add(manageSalesButton);

            manageAccountsButton = new JButton("Manage User Accounts");
            manageAccountsButton.addActionListener(e -> manageUserAccounts());
            add(manageAccountsButton);
        } else if ("staff".equals(role)) {
            // Add staff-specific buttons if necessary
            // ...
        }

        // Other UI initialization as necessary
        // ...
    }

    private void openLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(dbHandler);
        loginScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }

    private void openSignupScreen() {
        // Implementation for signup screen
        // ...
    }

    private void logout() {
        LoginScreen loginScreen = new LoginScreen(dbHandler);
        loginScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }

    private void viewUserDetails() {
        UserDetailsScreen userDetailsScreen = new UserDetailsScreen(dbHandler, userRole);
        userDetailsScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }

    private void viewProductCatalog() {
        // Implementation for viewing the product catalog
        // ...
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
        // Implementation for managing user accounts
        // ...
    }

    // Other methods...
    // ...
}
