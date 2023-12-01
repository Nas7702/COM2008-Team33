package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.User;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private class NonEditableModel extends DefaultTableModel {
        NonEditableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // This will make all cells in the table non-editable
        }
    }

    private void createUI() {
        setTitle("Order History - Trains of Sheffield");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table Model for Order History
        orderModel = new NonEditableModel(new Object[][]{}, new String[]{"Order ID", "Date", "Status"});
        orderTable = new JTable(orderModel);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);
        orderTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click detection
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // Get the selected row index
                    int orderId = (int) orderModel.getValueAt(row, 0);
                    System.out.println(orderId);
                    new ViewPreviousOrder(dbHandler, orderId).setVisible(true);
                }
            }
        });

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
