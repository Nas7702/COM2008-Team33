package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.User;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    private JButton loginButton;
    private JButton signupButton;
    private JButton logoutButton;
    private JButton productCatalogButton;
    private JButton manageInventoryButton;
    private JButton manageSalesButton;
    private JButton manageAccountsButton;
    private JButton viewDetailsButton;
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser; // Field to store the logged-in user

    public HomePage(DatabaseConnectionHandler dbHandler, User user) {
        this.dbHandler = dbHandler;
        this.loggedInUser = user; // Store the logged-in user
        String role = loggedInUser != null ? user.getRole().toString() : null; // Get role from User
        createUI(role);
    }

    private void createUI(String role) {
        setTitle("Home - Trains of Sheffield");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 1));


        // Show login and signup buttons only if the user is not logged in
        if (loggedInUser == null) {
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

            productCatalogButton = new JButton("View Product Catalog");
            productCatalogButton.addActionListener(e -> viewProductCatalog());
            add(productCatalogButton);
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
            // Add staff-specific buttons
            // ...
        }
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
        LoginScreen loginScreen = new LoginScreen(dbHandler);
        loginScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }

    private void viewUserDetails() {
        UserDetailsScreen userDetailsScreen = new UserDetailsScreen(dbHandler, loggedInUser);
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
