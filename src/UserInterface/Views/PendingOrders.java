package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;
import Models.User;
import Models.Orders;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class PendingOrders extends JFrame {

    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JTable orderTable;
    private DefaultTableModel orderModel;
    JLabel order;

    public PendingOrders(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        createUI(loggedInUser);
        loadOrders();
    }

    private void createUI(User loggedInUser) {
        setTitle("Pending Orders");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        orderModel = new DefaultTableModel();
        orderModel.addColumn("Order ID");
        orderModel.addColumn("Date");
        orderModel.addColumn("Name");

        // Pending Order Table
        orderTable = new JTable(orderModel);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        add(backButton, BorderLayout.SOUTH);

    }
    private void loadOrders() {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();

            String query = "SELECT OrderID, Date, UserID FROM Orders WHERE Status = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "confirmed");
            ResultSet rs = ps.executeQuery();
            int userID;
            while (rs.next()) {
                int orderId = rs.getInt("OrderID");
                Date date = rs.getDate("Date");
                userID = rs.getInt("UserID");

                String query2 = "SELECT Forename, Surname FROM User WHERE UserID = ?";
                PreparedStatement ps2 = connection.prepareStatement(query2);
                ps2.setInt(1, userID);
                ResultSet rs2 = ps2.executeQuery();
                String userName="Temp";
                if (rs2.next())
                    userName = rs2.getString("Forename")+" "+rs2.getString("Surname");
                orderModel.addRow(new Object[]{orderId, date, userName});
            }
        }
        catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading pending orders.");
        goBack();
        } finally {
            dbHandler.closeConnection();
        }
    }
    private void goBack() {
        StaffHomePage staffHomePage = new StaffHomePage(dbHandler, loggedInUser);
        staffHomePage.setVisible(true);
        dispose();
    }
}
