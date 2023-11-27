package UserInterface.Views;


import Database.DatabaseConnectionHandler;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class ProductPage extends JFrame {
    private JButton pendingOrdersButton;
    private JButton ManagerPageButton;

    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JList<Models.Product> productList;
    private JScrollPane scrollPane;
    private String currentCategory;
    private JPanel productsPanel;
    private DefaultListModel<Models.Product> productModel;
    public ProductPage(DatabaseConnectionHandler dbHandler, User loggedInUser, String category) {
        this.loggedInUser = loggedInUser;
        this.dbHandler = dbHandler;
        this.currentCategory=category;
        loadProducts();
        createUI(currentCategory);
    }

    private void createUI(String currentCategory) {
        setTitle(currentCategory);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        // Styling
        Color buttonColor = new Color(100, 149, 237);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        setLayout(new BorderLayout()); // Set BorderLayout for the main frame

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 10 is the gap between buttons
        JPanel mainPanel= new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        productModel = new DefaultListModel<>();
        loadProducts();
        productList = new JList<>(productModel);
        mainPanel.add(new JScrollPane(productList));

        add(topPanel, BorderLayout.NORTH); // Add buttons panel to the center

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        add(backButton, BorderLayout.SOUTH);

    }

    private void loadProducts() {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();
            String query = "SELECT * FROM Product ";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Models.Product product = new Models.Product(rs.getInt("ProductID"),
                        rs.getString("BrandName"), rs.getString("ProductName"),
                        rs.getString("ProductCode"), rs.getDouble("RetailPrice"),
                        rs.getString("Gauge"), rs.getString("Era"),
                        rs.getString("DCCCode"), rs.getInt("Quantity"));;
                productModel.addElement(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products from database.");
        } finally {
            dbHandler.closeConnection();
        }
    }


    private void addProductToPanel(Models.Product product) {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.LINE_AXIS));
        JLabel label = new JLabel(product.toString());
        productPanel.add(label);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        productPanel.add(quantitySpinner);
        productsPanel.add(productPanel);
        productsPanel.revalidate(); // Update the panel with new product
    }


    private void goBack() {
        StaffHomePage staffPage = new StaffHomePage(dbHandler, loggedInUser); // Pass the loggedInUser
        staffPage.setVisible(true);
        dispose(); // Close the Manager screen
    }



}
