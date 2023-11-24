package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.Cart;
import Models.Product;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductCatalogScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private Cart cart;
    private JPanel productsPanel;

    public ProductCatalogScreen(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        this.cart = new Cart();
        createUI();
        loadProducts();
    }

    private void createUI() {
        setTitle("Product Catalog - Trains of Sheffield");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel for products
        productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(productsPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Adjust as needed

        JButton viewOrdersButton = new JButton("View Cart");
        viewOrdersButton.addActionListener(e -> viewOrders());
        buttonsPanel.add(viewOrdersButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        buttonsPanel.add(backButton);

        // Add buttons panel to the bottom of the frame
        add(buttonsPanel, BorderLayout.SOUTH);
    }


    private void loadProducts() {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();
            String query = "SELECT * FROM Product";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product(rs.getInt("ProductID"),
                        rs.getString("BrandName"), rs.getString("ProductName"),
                        rs.getString("ProductCode"), rs.getDouble("RetailPrice"),
                        rs.getString("Gauge"), rs.getString("Era"),
                        rs.getString("DCCCode"));
                addProductToPanel(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products from database.");
        } finally {
            dbHandler.closeConnection();
        }
    }

    private void addProductToPanel(Product product) {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.LINE_AXIS));
        JLabel label = new JLabel(product.toString());
        productPanel.add(label);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        productPanel.add(quantitySpinner);

        JButton addButton = new JButton("Add to Cart");
        addButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quantity = (Integer) quantitySpinner.getValue();
                addToCart(product, quantity);
            }
        });
        productPanel.add(addButton);

        productsPanel.add(productPanel);
        productsPanel.revalidate(); // Update the panel with new product
    }

    private void viewOrders() {
        // Navigate to ViewOrderScreen with the existing cart
        ViewOrderScreen viewOrderScreen = new ViewOrderScreen(dbHandler, loggedInUser, cart);
        viewOrderScreen.setVisible(true);
        dispose();
    }

    private void goBack() {
        HomePage homePage = new HomePage(dbHandler, loggedInUser);
        homePage.setVisible(true);
        dispose();
    }

    private void addToCart(Product product, int quantity) {
        cart.addItem(product, quantity);
        JOptionPane.showMessageDialog(this, "Added to Cart: " + product.getProductName());
    }
}
