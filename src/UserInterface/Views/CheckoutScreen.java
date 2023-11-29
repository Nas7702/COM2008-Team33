package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.User;
import javax.swing.*;
import java.awt.*;

public class CheckoutScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JTextField addressField;
    private JTextField cardHolderField;
    private JTextField cardNumberField;
    private JTextField expiryField;
    private JTextField securityCodeField;
    private JLabel nameLabel;

    public CheckoutScreen(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        setTitle("Checkout");
        createUI();
    }

    private void createUI() {
        setLayout(new GridLayout(0, 2, 10, 10));
        setSize(600, 400);

        // Display user's name
        nameLabel = new JLabel("Name: " + loggedInUser.getForename() + " " + loggedInUser.getSurname());
        add(nameLabel);
        add(new JLabel()); // Placeholder for grid alignment

        // Check if address and bank details are already in the database
        // If not, ask for them
        if (!hasAddress(loggedInUser.getUserID())) {
            add(new JLabel("Address:"));
            addressField = new JTextField(20);
            add(addressField);
        }

        if (!hasBankDetails(loggedInUser.getUserID())) {
            // Payment Information
            add(new JLabel("Card Holder Name:"));
            cardHolderField = new JTextField(20);
            add(cardHolderField);

            add(new JLabel("Card Number:"));
            cardNumberField = new JTextField(16);
            add(cardNumberField);

            add(new JLabel("Expiry Date (MM/YY):"));
            expiryField = new JTextField(5);
            add(expiryField);

            add(new JLabel("Security Code:"));
            securityCodeField = new JTextField(3);
            add(securityCodeField);
        }

        // Buttons
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> processCheckout());
        add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean hasAddress(int userID) {
        // Implement database query to check if the user has an address
        return false; // Replace with actual check
    }

    private boolean hasBankDetails(int userID) {
        // Implement database query to check if the user has bank details
        return false; // Replace with actual check
    }

    private void processCheckout() {
        // Validate and process checkout details
        // Save new details to the database if needed
        dispose();
    }
}
