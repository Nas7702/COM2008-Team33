package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.User;

import java.sql.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewPreviousOrder extends JFrame {
    private int orderId;
    private DatabaseConnectionHandler dbHandler;
    private JTable productTable;
    private DefaultTableModel productModel;
    public ViewPreviousOrder(DatabaseConnectionHandler dbHandler, int orderId) {
        this.orderId = orderId;
        this.dbHandler = dbHandler;
        createUI();
        loadOrderDetails();
    }

    private void createUI() {
        setTitle("Order Details - " + orderId);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        productModel = new DefaultTableModel();
        productModel.addColumn("Product ID");
        productModel.addColumn("Quantity");
        productModel.addColumn("Line Cost");
        productModel.addColumn("Product Name");
        productModel.addColumn("Brand Name");
        productModel.addColumn("Retail Price");

        productTable = new JTable(productModel);
        add(new JScrollPane(productTable), BorderLayout.CENTER);
    }

    private void loadOrderDetails() {
        // Database query to fetch and display order details
        loadOrderLines();
    }
    private void loadOrderLines() {
        try {dbHandler.openConnection();
             Connection connection = dbHandler.getConnection();
            String query = "SELECT ol.ProductID, ol.Quantity, ol.LineCost, p.ProductName, p.BrandName, p.RetailPrice " +
                    "FROM OrderLine ol " +
                    "JOIN Product p ON ol.ProductID = p.ProductID " +
                    "WHERE ol.OrderID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, orderId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int productId = rs.getInt("ProductID");
                        int quantity = rs.getInt("Quantity");
                        float lineCost = rs.getFloat("LineCost");
                        String productName = rs.getString("ProductName");
                        String brandName = rs.getString("BrandName");
                        float retailPrice = rs.getFloat("RetailPrice");

                        productModel.addRow(new Object[]{productId, productName, brandName, quantity, lineCost, retailPrice});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading order line details: " + e.getMessage());
        }
    }
}