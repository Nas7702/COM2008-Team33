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


public class PendingOrders extends JFrame {

    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private Database.DatabaseOperations dbOperations;

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
        setSize(700, 300);
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

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete order");
        deleteButton.addActionListener(e -> deleteOrder());
        buttonPanel.add(deleteButton);
        JButton fulfillButton = new JButton("Fulfill order");
        fulfillButton.addActionListener(e -> fulfillOrder());
        buttonPanel.add(fulfillButton);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        add(buttonPanel, BorderLayout.EAST);

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
    private void deleteOrder(){
        try {
            int orderID=(int) orderModel.getValueAt(orderTable.getSelectedRow(),0);
            dbOperations = new Database.DatabaseOperations();
            dbHandler.openConnection();  // Open the connection
            Connection connection = dbHandler.getConnection();  // Get the connection
            dbOperations.deleteOrder(connection,orderID);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting order");
        } finally {
            dbHandler.closeConnection();  // Close the connection after use
        }

    }
    private void fulfillOrder(){
        try {
            dbHandler.openConnection();  // Open the connection
            Connection connection = dbHandler.getConnection();  // Get the connection
            int orderID=(int) orderModel.getValueAt(orderTable.getSelectedRow(),0);
            String query = "SELECT ProductID,Quantity FROM OrderLine WHERE OrderID = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, orderID);
            ResultSet rs =ps.executeQuery();

            ArrayList<String> productID=new ArrayList<String>();
            ArrayList<Integer> newQuantity=new ArrayList<Integer>();
            int counter=0;
            while (rs.next()) {
                String currentProductID=rs.getString("ProductID");
                int quantityToRemove =rs.getInt("Quantity");

                String query2 = "SELECT Quantity FROM Product WHERE ProductCode = ?";
                PreparedStatement ps2 = connection.prepareStatement(query2);
                ps2.setString(1, currentProductID);

                ResultSet rs2 = ps2.executeQuery();
                rs2.next();
                System.out.print(rs2.getInt("Quantity"));
                int currentQuantity=rs2.getInt("Quantity");
                if (currentQuantity-quantityToRemove<0){
                    JOptionPane.showMessageDialog(this,
                            "Insufficient quantity of product - productID = "+productID);
                    throw new Exception(""); //stop all order fulfilling, quantity too low
                }
                productID.add(currentProductID);
                newQuantity.add(currentQuantity-quantityToRemove);
                counter++;
            }

            for(int i=0;i<productID.size();i++){
                query = "UPDATE Product SET Quantity = ? WHERE ProductCode=?";
                ps = connection.prepareStatement(query);
                ps.setInt(1, newQuantity.get(i));
                ps.setString(2, productID.get(i));
                System.out.println(productID.get(i));
                ps.executeUpdate();
            }

            query = "UPDATE Orders SET Status = 'fulfilled' WHERE OrderID=?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, orderID);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fulfilling order");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbHandler.closeConnection();  // Close the connection after use
        }

    }
    private void goBack() {
        StaffHomePage staffHomePage = new StaffHomePage(dbHandler, loggedInUser);
        staffHomePage.setVisible(true);
        dispose();
    }
}
