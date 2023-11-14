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
    private DatabaseConnectionHandler dbHandler;

    public HomePage(DatabaseConnectionHandler dbHandler, String role) {
        this.dbHandler = dbHandler;
        createUI(role);
    }

    private void createUI(String role) {
        setTitle("Home - Trains of Sheffield");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 1));

        productCatalogButton = new JButton("View Product Catalog");
        add(productCatalogButton);

        // Show login and signup buttons only if the user is not logged in
        if (role == null || role.isEmpty()) {
            loginButton = new JButton("Login");
            loginButton.addActionListener(e -> openLoginScreen());
            add(loginButton);

            signupButton = new JButton("Sign Up");
            add(signupButton);
        } else {
            // Show logout button if the user is logged in
            logoutButton = new JButton("Logout");
            logoutButton.addActionListener(e -> logout());
            add(logoutButton);
        }

        // Role-specific UI components (omitted for brevity)
        // ...
    }

    private void openLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(dbHandler);
        loginScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }

    private void logout() {
        LoginScreen loginScreen = new LoginScreen(dbHandler);
        loginScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }

}
