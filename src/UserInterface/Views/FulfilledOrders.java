package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;
import Models.User;
import Models.Orders;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.sql.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;import Database.DatabaseOperations;


public class FulfilledOrders extends JFrame {

    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private Database.DatabaseOperations dbOperations;

    private JTable orderTable;
    private DefaultTableModel orderModel;
    JLabel order;

    public FulfilledOrders(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        createUI(loggedInUser);
        loadOrders();
    }

    private void createUI(User loggedInUser) {
        setTitle("Fulfilled Orders");
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        orderModel = new DefaultTableModel();
        orderModel.addColumn("Order ID");
        orderModel.addColumn("Date");
        orderModel.addColumn("Name");

        // Pending Order Table
        orderTable = new JTable(orderModel);


        JPanel mainPanel= new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        mainPanel.add(new JScrollPane(orderTable));
        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel= new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        bottomPanel.add(backButton);

        JButton viewOrderButton = new JButton("View Selected Order");
        viewOrderButton.addActionListener(e -> viewOrder((int) orderModel.getValueAt(orderTable.getSelectedRow(),0)));
        bottomPanel.add(viewOrderButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }
    private void loadOrders() {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();

            String query = "SELECT OrderID, Date, UserID FROM Orders";
            PreparedStatement ps = connection.prepareStatement(query);
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
            JOptionPane.showMessageDialog(this, "Error loading fulfilled orders.");
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
    private void viewOrder(int orderID) {
        ViewOrderStaff viewOrderPage = new ViewOrderStaff(dbHandler, loggedInUser, orderID);
        viewOrderPage.setVisible(true);
        dispose();
    }
}
