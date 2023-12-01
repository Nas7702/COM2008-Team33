package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;
import Models.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.util.List;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class CheckoutScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JTextField houseNumberField, roadNameField, cityField, postcodeField;
    private JTextField cardHolderField, cardNumberField, expiryField, securityCodeField;
    private JLabel nameLabel;
    private JLabel totalCartPriceLabel;
    private Cart cart;
    private List<Product> orderItems;
    private JTable cartTable;
    private DefaultTableModel tableModel;


    public CheckoutScreen(User loggedInUser, DatabaseConnectionHandler dbHandler, Cart cart) {
        this.loggedInUser = loggedInUser;
        this.dbHandler = dbHandler;
        this.cart = cart;
        this.orderItems = cart.getAllProducts();
        setTitle("Checkout");
        createUI();
        refreshCartTable();
        updateTotalCostLabel();
    }

    private void createUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(800, 500);

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        setupUserInfoPanel(userInfoPanel);
        add(userInfoPanel, BorderLayout.NORTH);

        setupCartTable();
        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        setupButtons(buttonPanel);
        add(buttonPanel, BorderLayout.SOUTH);

        totalCartPriceLabel = new JLabel();
        buttonPanel.add(totalCartPriceLabel);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                refreshCartTable();
            }
        });
    }

    private void setupButtons(JPanel buttonPanel) {
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> processCheckout());
        buttonPanel.add(confirmButton);

        JButton cancelButton = new JButton("Go Back");
        cancelButton.addActionListener(e -> goBack());
        buttonPanel.add(cancelButton);

    }

    private void updateTotalCostLabel() {
        double totalCost = calculateTotalCost();
        totalCartPriceLabel.setText("Total Cart Price: £" + String.format("%.2f", totalCost));
    }


    private void setupUserInfoPanel(JPanel panel) {
        nameLabel = new JLabel("Name: " + loggedInUser.getForename() + " " + loggedInUser.getSurname());
        panel.add(nameLabel);

        try {
            dbHandler.openConnection();
            DatabaseOperations dbOps = new DatabaseOperations();

            if (!dbOps.hasAddress(loggedInUser.getUserID(), dbHandler.getConnection())) {
                addAddressFields(panel);
            }

            if (!dbOps.hasBankDetails(loggedInUser.getUserID(), dbHandler.getConnection())) {
                addPaymentFields(panel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHandler.closeConnection();
        }
    }

    private void setupCartTable() {
        String[] columnNames = {"Product", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cartTable = new JTable(tableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            Object[] row = {product.getProductName(), quantity, product.getRetailPrice() * quantity};
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane);
    }


    private void addAddressFields(JPanel panel) {
        panel.add(new JLabel("House Number:"));
        houseNumberField = new JTextField(10);
        panel.add(houseNumberField);

        panel.add(new JLabel("Road Name:"));
        roadNameField = new JTextField(20);
        panel.add(roadNameField);

        panel.add(new JLabel("City:"));
        cityField = new JTextField(15);
        panel.add(cityField);

        panel.add(new JLabel("Postcode:"));
        postcodeField = new JTextField(10);
        panel.add(postcodeField);
    }


    private void addPaymentFields(JPanel panel) {
        panel.add(new JLabel("Card Holder Name:"));
        cardHolderField = new JTextField(20);
        panel.add(cardHolderField);

        panel.add(new JLabel("Card Number:"));
        cardNumberField = new JTextField(16);
        panel.add(cardNumberField);

        panel.add(new JLabel("Expiry Date (MM/YY):"));
        expiryField = new JTextField(5);
        panel.add(expiryField);

        panel.add(new JLabel("Security Code:"));
        securityCodeField = new JTextField(3);
        panel.add(securityCodeField);
    }

    private void goBack() {
        ViewCartScreen viewCartScreen = new ViewCartScreen(dbHandler, loggedInUser, cart);
        viewCartScreen.setVisible(true);
        dispose();
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

            int orderId = dbOps.insertOrder(loggedInUser.getUserID(), calculateTotalCost(), "complete", dbHandler.getConnection());
            if (orderId != -1) {
                dbOps.insertOrderItems(orderId, orderItems, dbHandler.getConnection());
                JOptionPane.showMessageDialog(this, "Order placed successfully!");
            }

            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error processing checkout: " + e.getMessage());
            e.printStackTrace();
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

    private void refreshCartTable() {
        orderItems = cart.getAllProducts();
        tableModel.setRowCount(0); // Clear existing rows
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            Object[] row = {product.getProductName(), quantity, product.getRetailPrice() * quantity};
            tableModel.addRow(row);
        }
        updateTotalCostLabel();
    }


    private double calculateTotalCost() {
        double totalCost = 0;
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            totalCost += product.getRetailPrice() * quantity;
        }
        return totalCost;
    }


    private String convertToMySQLDateFormat(String expiryDate) {
        String[] parts = expiryDate.split("/");
        String month = parts[0];
        String year = "20" + parts[1];
        return year + "-" + month + "-01";
    }
}
