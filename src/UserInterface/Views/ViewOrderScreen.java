package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.Cart;
import Models.Product;
import Models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Map;

public class ViewOrderScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JTable orderTable;
    private JLabel totalCartPriceLabel;
    private DefaultTableModel tableModel;
    private Cart cart;

    public ViewOrderScreen(DatabaseConnectionHandler dbHandler, User loggedInUser, Cart cart) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        this.cart = cart;
        createUI();
        loadCartItems();
    }

    private void createUI() {
        setTitle("View Pending Orders - Trains of Sheffield");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"Product", "Quantity", "Price per Item", "Total Price"}, 0);
        orderTable = new JTable(tableModel);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> checkout());
        buttonsPanel.add(checkoutButton);

        JButton catalogButton = new JButton("Return to Product Catalogue");
        catalogButton.addActionListener(e -> viewProductCatalog());
        buttonsPanel.add(catalogButton);

        totalCartPriceLabel = new JLabel();
        buttonsPanel.add(totalCartPriceLabel);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void loadCartItems() {
        double totalCost = 0;
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double retailPrice = product.getRetailPrice();
            double totalPrice = retailPrice * quantity;
            totalCost += totalPrice;

            tableModel.addRow(new Object[]{product.getProductName(), quantity, retailPrice, totalPrice});
        }

        totalCartPriceLabel.setText("Total Cart Price: " + String.format("%.2f", totalCost));
    }

    private void checkout() {
        // Implement checkout functionality
        JOptionPane.showMessageDialog(this, "Checkout feature not yet implemented.");
    }

    private void viewProductCatalog() {
        ProductCatalogScreen catalogScreen = new ProductCatalogScreen(dbHandler, loggedInUser, cart);
        catalogScreen.setVisible(true);
        dispose();
    }
}
