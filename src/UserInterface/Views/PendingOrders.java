package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;
import Models.User;
import Models.Orders;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PendingOrders extends JFrame {

    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JList<Models.Orders> orderList;
    private DefaultListModel<Models.Orders> orderModel;
    JLabel order;

    public PendingOrders(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser; // Store the logged-in user
        createUI(loggedInUser);
        loadOrders();
    }

    private void createUI(User loggedInUser) {
        setTitle("Pending Orders");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        orderModel = new DefaultListModel<>();
        orderList = new JList<>(orderModel);
        add(new JScrollPane(orderList));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        add(backButton, BorderLayout.SOUTH);

    }
    private void loadOrders() {
        try {
            dbHandler.openConnection();  // Open the connection
            Connection connection = dbHandler.getConnection();  // Get the connection

            String query = "SELECT * FROM Orders";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderID = rs.getInt("OrderID");
                int userID = rs.getInt("UserID");
                String date = rs.getString("Date");
                String status = rs.getString("Status");
                float totalCost = rs.getFloat("TotalCost");

                Models.Orders order = new Models.Orders(orderID, userID, date, status,
                        totalCost);
                orderModel.addElement(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading pending orders.");
        } finally {
            dbHandler.closeConnection();  // Close the connection after use
        }
    }
    private void goBack() {
        StaffHomePage staffHomePage = new StaffHomePage(dbHandler, loggedInUser); // Pass the loggedInUser
        staffHomePage.setVisible(true);
        dispose(); // Close the ProductCatalogScreen
    }
}
