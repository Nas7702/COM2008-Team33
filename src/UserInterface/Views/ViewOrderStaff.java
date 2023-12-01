package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.User;
import com.mysql.cj.x.protobuf.MysqlxCrud;

import java.sql.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewOrderStaff extends JFrame {
    private int orderId;
    private DatabaseConnectionHandler dbHandler;
    private JTable productTable;
    private DefaultTableModel productModel;
    private User loggedInUser;
    public ViewOrderStaff(DatabaseConnectionHandler dbHandler, User loggedInUser, int orderId) {
        this.orderId = orderId;
        this.dbHandler = dbHandler;
        this.loggedInUser=loggedInUser;
        createUI();
        loadOrderLines(orderId);
    }

    private void createUI() {
        setTitle("Order Details - " + orderId);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(7,2));
        JLabel orderID= new JLabel("orderID");
        JLabel personal= new JLabel("Personal");
        JLabel email= new JLabel("Email");
        JLabel address= new JLabel("Address");
        JLabel totalCost= new JLabel("Order cost");
        JLabel orderStatus= new JLabel("Order status");
        JLabel bankExist= new JLabel("Does bank card exist");
        String[] details= loadDetails(orderId);
        JLabel orderIDDDetail= new JLabel(String.valueOf(orderId));
        JLabel personalDetail= new JLabel(details[3]);
        JLabel emailDetail= new JLabel(details[4]);
        JLabel addressDetail= new JLabel(details[3]);
        JLabel totalCostDetail= new JLabel(details[4]);
        JLabel orderStatusDetail= new JLabel(details[2]);
        JLabel bankExistDetail= new JLabel(details[6]);
        infoPanel.add(orderID);
        infoPanel.add(orderIDDDetail);
        infoPanel.add(personal);
        infoPanel.add(personalDetail);

        infoPanel.add(email);
        infoPanel.add(emailDetail);
        infoPanel.add(address);
        infoPanel.add(addressDetail);

        infoPanel.add(totalCost);
        infoPanel.add(totalCostDetail);

        infoPanel.add(orderStatus);
        infoPanel.add(orderStatusDetail);

        infoPanel.add(bankExist);
        infoPanel.add(bankExistDetail);

        add(infoPanel, BorderLayout.NORTH);
        productModel = new DefaultTableModel();
        productModel.addColumn("Product name");
        productModel.addColumn("Product code");
        productModel.addColumn("Orderline");
        productModel.addColumn("Orderline cost");


        productTable = new JTable(productModel);
        add(new JScrollPane(productTable), BorderLayout.CENTER);
    }
    private String[] loadDetails(int orderId) {
        try {dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();
            // and postal address,
            // and the fact that a valid bank card exists.
            //orderline contents, cost of line
            String query = "SELECT UserID, Date, Status FROM Orders Where OrderID = ?";
            int userID=-1;
            String date="";
            String status="";

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {// each order line
                    while (rs.next()) {
                        userID = (rs.getInt("UserID"));
                        date = rs.getString("Date");
                        status = rs.getString("Status");
                    }
                }
            }
            String personal="";
            String email="";
            query = "SELECT Forename, Surname, Email FROM User Where UserID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, (userID));
                try (ResultSet rs = ps.executeQuery()) {// each order line
                    while (rs.next()) {
                        personal = rs.getString("Forename")+" "+rs.getString("Surname");
                        email = rs.getString("Email");
                    }
                }
            }
            String isBank="no";
            query = "SELECT UserID FROM BankDetails Where UserID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, (userID));
                try (ResultSet rs = ps.executeQuery()) {// each order line
                    if(rs.next())
                        isBank="yes";
                }
            }
            String address="";
            query = "SELECT HouseNumber, RoadName, Postcode FROM Address Where UserID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1,(userID));
                try (ResultSet rs = ps.executeQuery()) {// each order line
                    while (rs.next()) {
                        address = rs.getString("HouseNumber")+" "+rs.getString("RoadName")+" "+rs.getString("Postcode");
                    }
                }
            }
            String[] detailsArr= {String.valueOf(userID),date,status,personal,email,isBank,address};
            return detailsArr;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading order line details: " + e.getMessage());
        }
        return null;
    }
    private void loadOrderLines(int orderId) {
        try {dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();
            String query = "SELECT OrderLineID, ProductID, Quantity, Linecost FROM OrderLine Where OrderID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {// each order line
                    while (rs.next()) {
                        int productId = rs.getInt("ProductID");
                        int quantity = rs.getInt("Quantity");
                        float lineCost = rs.getFloat("LineCost");
                        int OrderlineID = rs.getInt("OrderLineID");
                        productModel.addRow(new Object[]{productId, quantity, OrderlineID, lineCost});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading order line details: " + e.getMessage());
        }
    }
}