package UserInterface.Views;

import Database.DatabaseOperations;
import Database.DatabaseConnectionHandler;
import Models.User;
import com.mysql.cj.jdbc.JdbcPreparedStatement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StaffHomePage extends JFrame {
    private JButton pendingOrdersButton;
    private JButton ManagerPageButton;
    private JButton manageUserButton;
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JButton increaseButton;
    private JButton decreaseButton;
    private JButton deleteButton;
    private JButton addProductButton;

    private Database.DatabaseOperations dbOperations;



    private JTable productTable;
    private DefaultTableModel productModel;

    public StaffHomePage(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;

        createUI(loggedInUser);
        loadProducts();
    }

    private void createUI(User loggedInUser) {
        dbOperations = new Database.DatabaseOperations();
        setTitle("Staff Page");
        setSize(700, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        Color buttonColor = new Color(100, 149, 237);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));


        pendingOrdersButton = new JButton("View Pending Orders");
        pendingOrdersButton.addActionListener(e -> openPendingOrdersScreen());
        buttonsPanel.add(pendingOrdersButton);
        add(buttonsPanel, BorderLayout.EAST);
        JButton fulfilledOrdersButton = new JButton("View Fulfilled Orders");
        fulfilledOrdersButton.addActionListener(e -> openFulfilledOrdersScreen());
        buttonsPanel.add(fulfilledOrdersButton);
        add(buttonsPanel, BorderLayout.EAST);

        if (loggedInUser.getRole().equals(User.userRole.MANAGER)) {
            manageUserButton = new JButton("Manager Page");
            manageUserButton.addActionListener(e -> manageUserAccounts());
            buttonsPanel.add(manageUserButton);
        }

        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));


//        roleFilterCombo = new JComboBox<>(new String[]{"All", "CUSTOMER", "STAFF", "MANAGER"});
//        roleFilterCombo.addActionListener(e -> applyFilter());
//        filterPanel.add(roleFilterCombo);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        buttonsPanel.add(backButton);
        add(buttonsPanel, BorderLayout.SOUTH);


        productModel = new DefaultTableModel();
        productModel.addColumn("ProductID");
        productModel.addColumn("ProductCode");
        productModel.addColumn("Brand");
        productModel.addColumn("ProductName");
        productModel.addColumn("Quantity");

        increaseButton = new JButton("Quantity+1");
        increaseButton.addActionListener(e -> { //gets selected products quantity, adds 1 and passes to function
            int row = productTable.getSelectedRow();
            int quantity = (int) productModel.getValueAt(row,4)+1;
            updateQuantity(quantity);
            loadProducts();
            productTable=new JTable(productModel);
        });

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int row = productTable.getSelectedRow();
            int productID = (int) productModel.getValueAt(row,0);
            deleteProduct(productID);
        });

        decreaseButton = new JButton("Quantity-1");
        decreaseButton.addActionListener(e -> { //gets selected products quantity, adds 1 and passes to function
            if (productTable.getSelectedRow()!=-1) {
                int row = productTable.getSelectedRow();
                int quantity = (int) productModel.getValueAt(row, 4);
                if (quantity == 0) {
                    JOptionPane.showMessageDialog(this, "Cannot decrease quantity, quantity is already 0");
                } else {
                    updateQuantity(quantity - 1);
                    loadProducts();
                    productTable = new JTable(productModel);
                }
            }
//            else JOptionPane.showMessageDialog(this, "Please select a row");
//            if (loggedInUser.getRole().equals(User.userRole.MANAGER)) {
//                ManagerPageButton = new JButton("Manager Homepage");
//                ManagerPageButton.addActionListener(event -> managerPage());
//                buttonsPanel.add(ManagerPageButton);
//            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int row = productTable.getSelectedRow();
            int productID = (int) productModel.getValueAt(row,0);
            deleteProduct(productID);
        });

        addProductButton = new JButton("Add product");
        addProductButton.addActionListener(e -> addProduct());

        editPanel.add(increaseButton);
        editPanel.add(decreaseButton);
        editPanel.add(deleteButton);
        editPanel.add(addProductButton);

        mainPanel.add(editPanel, BorderLayout.NORTH);

        productTable = new JTable(productModel);
        mainPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        add(mainPanel);

    }

    public void deleteProduct(int productID){
        try {
            dbHandler.openConnection();  // Open the connection
            Connection connection = dbHandler.getConnection();  // Get the connection

            String query = "DELETE FROM Product WHERE ProductID=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, productID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting product");
        } finally {
            dbHandler.closeConnection();  // Close the connection after use
        }
    }

    private void loadProducts() {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();

            String query = "SELECT ProductID, ProductCode, BrandName, ProductName, Quantity FROM Product";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productID = (rs.getInt("ProductID"));
                String productCode = (rs.getString("ProductCode"));
                String brand = (rs.getString("BrandName"));
                String productName = (rs.getString("ProductName"));
                int quantity = (rs.getInt("Quantity"));
                productModel.addRow(new Object[]{productID,productCode,brand,productName,quantity});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products from database.");
        } finally {
            dbHandler.closeConnection();
        }
    }

    private void updateQuantity(int newQuantity) {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();

            String query = "UPDATE Product SET Quantity = ? WHERE ProductID=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, newQuantity);
            int selectedRow = productTable.getSelectedRow();
            int productID = (int) productModel.getValueAt(selectedRow, 0);
            ps.setInt(2, productID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating product quantity");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbHandler.closeConnection();
        }
    }


    private void openPendingOrdersScreen() {
        PendingOrders pendingOrdersScreen = new PendingOrders(dbHandler, loggedInUser);
        pendingOrdersScreen.setVisible(true);
        this.dispose();
    }

    private void openFulfilledOrdersScreen() {
        FulfilledOrders fulfilledOrdersScreen = new FulfilledOrders(dbHandler, loggedInUser);
        fulfilledOrdersScreen.setVisible(true);
        this.dispose();
    }


    private void goBack() {
        HomePage homePage = new HomePage(dbHandler, loggedInUser); // Pass the loggedInUser
        homePage.setVisible(true);
        dispose();
    }

    private void manageUserAccounts() {
        ManageUsersScreen manageUsersScreen = new ManageUsersScreen(dbHandler, loggedInUser);
        manageUsersScreen.setVisible(true);
        this.dispose();
    }
    private void addProduct() {
        AddProduct addProductScreen = new AddProduct(dbHandler, loggedInUser);
        addProductScreen.setVisible(true);
        this.dispose();
    }


}