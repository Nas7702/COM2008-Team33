//package UserInterface.Views;
//
//import Database.DatabaseConnectionHandler;
//import Models.Product;
//import Models.User;
//
//import javax.swing.*;
//import java.awt.*;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class ManageUsersScreen extends JFrame {
//    private DatabaseConnectionHandler dbHandler;
//    private User loggedInUser; // Add this line to store the logged-in user
//    private JList<User> userList;
//    private DefaultListModel<User> userModel;
//
//    public ManageUsersScreen(DatabaseConnectionHandler dbHandler, User loggedInUser) {
//        this.dbHandler = dbHandler;
//        this.loggedInUser = loggedInUser; // Store the logged-in user
//        createUI();
//        loadUsers();
//    }
//
//    private void createUI() {
//        setTitle("Product Catalog - Trains of Sheffield");
//        setSize(500, 300);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        userModel = new DefaultListModel<User>();
//        userList = new JList<>(userModel);
//        add(new JScrollPane(userList));
//
//        JButton backButton = new JButton("Back");
//        backButton.addActionListener(e -> goBack());
//        add(backButton, BorderLayout.SOUTH);
//    }
//
//    private void loadUsers() {
//        try {
//            dbHandler.openConnection();  // Open the connection
//            Connection connection = dbHandler.getConnection();  // Get the connection
//
//            String query = "SELECT * FROM User";
//            PreparedStatement ps = connection.prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                String userID = rs.getString("userID");
//                String email = rs.getString("Email");
//                String forename = rs.getString("Forename");
//                String surname = rs.getString("Surname");
//                User.userRole role = User.userRole.valueOf(rs.getString("role").toUpperCase());
//
//                User user = new User(email, forename, surname, role);
//                userModel.addElement(user);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error loading products from database.");
//        } finally {
//            dbHandler.closeConnection();  // Close the connection after use
//        }
//    }
//
//
//    private void goBack() {
//        HomePage homePage = new HomePage(dbHandler, loggedInUser); // Pass the loggedInUser
//        homePage.setVisible(true);
//        dispose(); // Close the ProductCatalogScreen
//    }
//
//}
package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageUsersScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JTable userTable;
    private DefaultTableModel userModel;

    public ManageUsersScreen(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        createUI();
        loadUsers();
    }

    private void createUI() {
        setTitle("Manage Users - Trains of Sheffield");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table Model
        userModel = new DefaultTableModel();
        userModel.addColumn("Email");
        userModel.addColumn("Forename");
        userModel.addColumn("Surname");
        userModel.addColumn("Role");

        // User Table
        userTable = new JTable(userModel);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        add(backButton, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();

            String query = "SELECT * FROM User";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String email = rs.getString("Email");
                String forename = rs.getString("Forename");
                String surname = rs.getString("Surname");
                String role = rs.getString("role").toUpperCase();

                userModel.addRow(new Object[]{email, forename, surname, role});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading users from database.");
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
