package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.Cart;
import Models.Product;
import Models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class ViewOrderScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private Cart cart;
    private JTable orderTable;
    private DefaultTableModel tableModel;

    public ViewOrderScreen(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        createUI();
        loadOrders();
    }

    private void createUI() {
        setTitle("View Order - Trains of Sheffield");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize table model and table for order display
        tableModel = new DefaultTableModel(new String[]{"Product", "Quantity", "Price"}, 0);
        orderTable = new JTable(tableModel);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton catalogButton = new JButton("Return to Product Catalogue");
        catalogButton.addActionListener(e -> viewProductCatalog());
        buttonsPanel.add(catalogButton);

        JButton confirmOrderButton = new JButton("Confirm Order");
        confirmOrderButton.addActionListener(e -> confirmOrder());
        buttonsPanel.add(confirmOrderButton);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();
            String query = "SELECT * FROM Orders WHERE UserID = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, loggedInUser.getUserID()); // Set the user ID in the prepared statement
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int orderID = rs.getInt("OrderID");
                String date = rs.getString("Date");
                String status = rs.getString("Status");
                float totalCost = rs.getFloat("TotalCost");
                // Add the order to the table model
                tableModel.addRow(new Object[]{orderID, date, status, totalCost});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading orders from database.");
        } finally {
            dbHandler.closeConnection();
        }
    }



    private void viewProductCatalog() {
        ProductCatalogScreen catalogScreen = new ProductCatalogScreen(dbHandler, loggedInUser);
        catalogScreen.setVisible(true);
        dispose(); // Close the ViewOrderScreen
    }

    private void confirmOrder() {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();

            // TODO: Implement the logic to insert the order into the database

            JOptionPane.showMessageDialog(this, "Order Confirmed!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error confirming the order.");
        } finally {
            dbHandler.closeConnection();
        }

        // After confirming, return to catalog or home page
        viewProductCatalog();
    }

    // Additional methods...
}
