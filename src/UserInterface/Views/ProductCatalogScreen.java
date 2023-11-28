package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.Cart;
import Models.Orders;
import Models.Product;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.LocalDate;

public class ProductCatalogScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private Cart cart;
    private JPanel productsPanel;
    private JLabel confirmationLabel;
    private JComboBox<String> categoryComboBox;

    public ProductCatalogScreen(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        this.cart = new Cart();
        createUI();
    }
    public ProductCatalogScreen(DatabaseConnectionHandler dbHandler, User loggedInUser, Cart cart) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        this.cart = cart;
        createUI();
    }

    private void createUI() {
        setTitle("Product Catalog - Trains of Sheffield");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        categoryComboBox = new JComboBox<>(new String[]{"Train Sets", "Train Packs", "Track Packs", "Locomotives", "Carriages", "Wagons", "Track", "Scenery"});
        categoryComboBox.addActionListener(e -> loadProducts(categoryComboBox.getSelectedItem().toString()));
        add(categoryComboBox, BorderLayout.NORTH);

        // Main panel
        productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(productsPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        confirmationLabel = new JLabel(" ");
        confirmationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(confirmationLabel, BorderLayout.SOUTH);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton viewOrdersButton = new JButton("View Cart");
        viewOrdersButton.addActionListener(e -> viewOrders());
        buttonsPanel.add(viewOrdersButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        buttonsPanel.add(backButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        loadProducts(categoryComboBox.getSelectedItem().toString());
    }


    private void loadProducts(String category) {
        productsPanel.removeAll();

        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();
            String query = "SELECT * FROM Product WHERE ProductCode LIKE ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, categoryToCodePrefix(category) + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product(rs.getInt("ProductID"),
                        rs.getString("BrandName"), rs.getString("ProductName"),
                        rs.getString("ProductCode"), rs.getDouble("RetailPrice"),
                        rs.getString("Gauge"), rs.getString("Era"),
                        rs.getString("DCCCode"), rs.getInt("Quantity"));
                addProductToPanel(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products from database.");
        } finally {
            dbHandler.closeConnection();
        }

        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private String categoryToCodePrefix(String category) {
        return switch (category) {
            case "Train Sets" -> "M";
            case "Train Packs" -> "N";
            case "Track Packs" -> "P";
            case "Locomotives" -> "L";
            case "Carriages" -> "C";
            case "Wagons" -> "W";
            case "Track" -> "R";
            case "Scenery" -> "S";
            default -> "";
        };
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
        productsPanel.revalidate();
    }

    private void viewOrders() {
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
        confirmationLabel.setText("Added to Cart: " + product.getProductName() + " (Quantity: " + quantity + ")");
    }

    private void createPendingOrderInDatabase(int userID, Product product, int quantity) {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();

            String checkOrderQuery = "SELECT OrderID FROM Orders WHERE UserID = ? AND Status = 'pending'";
            PreparedStatement checkOrderPs = connection.prepareStatement(checkOrderQuery);
            checkOrderPs.setInt(1, userID);
            ResultSet orderRs = checkOrderPs.executeQuery();

            int orderID;
            if (orderRs.next()) {
                orderID = orderRs.getInt("OrderID");
            } else {
                String insertOrderQuery = "INSERT INTO Orders (UserID, Date, Status, TotalCost) VALUES (?, ?, ?, ?)";
                PreparedStatement insertOrderPs = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
                insertOrderPs.setInt(1, userID);
                insertOrderPs.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                insertOrderPs.setString(3, "pending");
                insertOrderPs.setDouble(4, 0.0);
                insertOrderPs.executeUpdate();

                ResultSet generatedKeys = insertOrderPs.getGeneratedKeys();
                generatedKeys.next();
                orderID = generatedKeys.getInt(1);
            }

            String insertOrderLineQuery = "INSERT INTO OrderLine (OrderID, ProductID, Quantity, LineCost) VALUES (?, ?, ?, ?)";
            PreparedStatement insertOrderLinePs = connection.prepareStatement(insertOrderLineQuery);
            insertOrderLinePs.setInt(1, orderID);
            insertOrderLinePs.setInt(2, product.getProductID());
            insertOrderLinePs.setInt(3, quantity);
            insertOrderLinePs.setDouble(4, product.getRetailPrice() * quantity);
            insertOrderLinePs.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHandler.closeConnection();
        }
    }


}
