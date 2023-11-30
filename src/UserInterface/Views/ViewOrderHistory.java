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

public class ViewOrderHistory extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JTable orderTable;
    private DefaultTableModel orderModel;


    public ViewOrderHistory(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        createUI();
        loadOrderHistory();
    }

    private void createUI() {
        setTitle("Order History - Trains of Sheffield");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table Model for Order History
        orderModel = new DefaultTableModel();
        // Assuming columns like Order ID, Date, Amount, etc.
        orderModel.addColumn("Order ID");
        orderModel.addColumn("Date");
        orderModel.addColumn("Status");
        // Add more columns as needed

        // Order History Table
        orderTable = new JTable(orderModel);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        add(backButton, BorderLayout.SOUTH);
    }

    private void loadOrderHistory() {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();

            String query = "SELECT OrderID, Date, Status FROM Orders WHERE UserID = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, loggedInUser.getUserID());

            //debugging
            System.out.println(loggedInUser.getUserID());
            System.out.println(loggedInUser.getForename());
            System.out.println(loggedInUser.getSurname());


            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("OrderID");
                Date date = rs.getDate("Date");
                String status = rs.getString("Status");

                orderModel.addRow(new Object[]{orderId, date, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading order history.");
        } finally {
            dbHandler.closeConnection();
        }
    }


    private void goBack() {
        HomePage homePage = new HomePage(dbHandler, loggedInUser);
        homePage.setVisible(true);
        dispose();
    }
}
