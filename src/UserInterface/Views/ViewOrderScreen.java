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
    private Cart cart;
    private JTable orderTable;
    private DefaultTableModel tableModel;

    public ViewOrderScreen(DatabaseConnectionHandler dbHandler, User loggedInUser, Cart cart) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        this.cart = cart;
        createUI();
        loadCartItems();  // Load cart items instead of orders
    }

    private void createUI() {
        setTitle("View Cart - Trains of Sheffield");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize table model and table for cart display
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

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void loadCartItems() {
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            double totalPrice = product.getRetailPrice() * quantity;
            tableModel.addRow(new Object[]{product.getProductName(), quantity, product.getRetailPrice(), totalPrice});
        }
    }

    private void checkout() {
        // Implement the checkout process
        // This could involve creating an order in the database and clearing the cart
        // For now, just show a confirmation message
        JOptionPane.showMessageDialog(this, "Checkout feature not yet implemented.");
    }

    private void viewProductCatalog() {
        ProductCatalogScreen catalogScreen = new ProductCatalogScreen(dbHandler, loggedInUser);
        catalogScreen.setVisible(true);
        dispose();
    }
}
