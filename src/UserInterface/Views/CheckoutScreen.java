package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;
import Models.Product;
import Models.User;

import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckoutScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JTextField houseNumberField;
    private JTextField roadNameField;
    private JTextField cityField;
    private JTextField postcodeField;
    private JTextField cardHolderField;
    private JTextField cardNumberField;
    private JTextField expiryField;
    private JTextField securityCodeField;
    private JLabel nameLabel;
    private List<Product> orderItems;

    public CheckoutScreen(User loggedInUser, DatabaseConnectionHandler dbHandler, List<Product> orderItems) {
        this.loggedInUser = loggedInUser;
        this.dbHandler = dbHandler;
        this.orderItems = orderItems;
        setTitle("Checkout");
        createUI();
    }

    private void createUI() {
        setLayout(new GridLayout(0, 2, 10, 10));
        setSize(600, 400);

        nameLabel = new JLabel("Name: " + loggedInUser.getForename() + " " + loggedInUser.getSurname());
        add(nameLabel);
        add(new JLabel());

        try {
            dbHandler.openConnection();
            DatabaseOperations dbOps = new DatabaseOperations();

            if (!dbOps.hasAddress(loggedInUser.getUserID(), dbHandler.getConnection())) {
                addAddressFields();
            }

            if (!dbOps.hasBankDetails(loggedInUser.getUserID(), dbHandler.getConnection())) {
                addPaymentFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHandler.closeConnection();
        }

        addButtons();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addAddressFields() {
        add(new JLabel("House Number:"));
        houseNumberField = new JTextField(10);
        add(houseNumberField);

        add(new JLabel("Road Name:"));
        roadNameField = new JTextField(20);
        add(roadNameField);

        add(new JLabel("City:"));
        cityField = new JTextField(15);
        add(cityField);

        add(new JLabel("Postcode:"));
        postcodeField = new JTextField(10);
        add(postcodeField);
    }

    private void addPaymentFields() {
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

    private void addButtons() {
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> processCheckout());
        add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);
    }

    private void processCheckout() {
        try {
            dbHandler.openConnection();
            DatabaseOperations dbOps = new DatabaseOperations();

            // Save address and bank details if they are new
            if (!dbOps.hasAddress(loggedInUser.getUserID(), dbHandler.getConnection())) {
                dbOps.saveAddress(loggedInUser.getUserID(),
                        houseNumberField.getText(),
                        roadNameField.getText(),
                        cityField.getText(),
                        postcodeField.getText(),
                        dbHandler.getConnection());
            }

            if (!dbOps.hasBankDetails(loggedInUser.getUserID(), dbHandler.getConnection())) {
                String expiryDateInput = expiryField.getText(); // in MM/YY format
                String expiryDateMySQLFormat = convertToMySQLDateFormat(expiryDateInput); // convert to YYYY-MM-01 format

                dbOps.saveBankDetails(loggedInUser.getUserID(),
                        cardHolderField.getText(), cardNumberField.getText(),
                        expiryDateMySQLFormat, securityCodeField.getText(),
                        dbHandler.getConnection());
            }

            insertOrder();

            dispose();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        } finally {
            dbHandler.closeConnection();
        }
    }

    private void insertOrder() {
        double totalCost = calculateTotalCost();
        String status = "complete";

        String insertOrderSQL = "INSERT INTO Orders (UserID, Date, Status, TotalCost) VALUES (?, CURDATE(), ?, ?)";

        try (PreparedStatement preparedStatement = dbHandler.getConnection().prepareStatement(insertOrderSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, loggedInUser.getUserID());
            preparedStatement.setString(2, status);
            preparedStatement.setDouble(3, totalCost);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    insertOrderLines(orderId); // Insert items into OrderLine table
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        }
    }

    private void insertOrderLines(int orderId) {
        DatabaseOperations dbOps = new DatabaseOperations();
        try {
            dbOps.insertOrderItems(orderId, orderItems, dbHandler.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private double calculateTotalCost() {
        return orderItems.stream().mapToDouble(item -> item.getRetailPrice() * item.getQuantity()).sum();
    }

    private String convertToMySQLDateFormat(String expiryDate) {
        String[] parts = expiryDate.split("/");
        String month = parts[0];
        String year = "20" + parts[1];
        return year + "-" + month + "-01";
    }


}
