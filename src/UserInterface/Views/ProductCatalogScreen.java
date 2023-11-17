package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.Product;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCatalogScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser; // Add this line to store the logged-in user
    private JList<Product> productList;
    private DefaultListModel<Product> productModel;

    public ProductCatalogScreen(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser; // Store the logged-in user
        createUI();
        loadProducts();
    }

    private void createUI() {
        setTitle("Product Catalog - Trains of Sheffield");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        productModel = new DefaultListModel<>();
        productList = new JList<>(productModel);
        add(new JScrollPane(productList));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        add(backButton, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        try {
            dbHandler.openConnection();  // Open the connection
            Connection connection = dbHandler.getConnection();  // Get the connection

            String query = "SELECT * FROM Product";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String brandName = rs.getString("BrandName");
                String productName = rs.getString("ProductName");
                String productCode = rs.getString("ProductCode");
                double retailPrice = rs.getDouble("RetailPrice");
                String gauge = rs.getString("Gauge");
                String era = rs.getString("Era");
                String dccCode = rs.getString("DCCCode");

                Product product = new Product(productID, brandName, productName, productCode,
                        retailPrice, gauge, era, dccCode);
                productModel.addElement(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products from database.");
        } finally {
            dbHandler.closeConnection();  // Close the connection after use
        }
    }


    private void goBack() {
        HomePage homePage = new HomePage(dbHandler, loggedInUser); // Pass the loggedInUser
        homePage.setVisible(true);
        dispose(); // Close the ProductCatalogScreen
    }

}
