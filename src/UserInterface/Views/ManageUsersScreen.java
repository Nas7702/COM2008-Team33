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
import javax.swing.table.TableRowSorter;

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
    private JButton promoteButton;
    private JButton demoteButton;
    private JComboBox<String> roleFilterCombo;

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

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Role:"));

        roleFilterCombo = new JComboBox<>(new String[]{"All", "CUSTOMER", "STAFF", "MANAGER"});
        roleFilterCombo.addActionListener(e -> applyFilter());
        filterPanel.add(roleFilterCombo);

        add(filterPanel, BorderLayout.NORTH);

        userModel = new DefaultTableModel();
        userModel.addColumn("Email");
        userModel.addColumn("Forename");
        userModel.addColumn("Surname");
        userModel.addColumn("Role");

        promoteButton = new JButton("Promote");
        promoteButton.addActionListener(e -> promoteUser());
        filterPanel.add(promoteButton);

        demoteButton = new JButton("Demote");
        demoteButton.addActionListener(e -> demoteUser());
        filterPanel.add(demoteButton);

        userTable = new JTable(userModel);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

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

    private void applyFilter() {
        if (roleFilterCombo == null) {
            return;
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(userModel);
        userTable.setRowSorter(sorter);

        String selectedRole = (String) roleFilterCombo.getSelectedItem();
        if (selectedRole != null && !selectedRole.equals("All")) {
            sorter.setRowFilter(RowFilter.regexFilter(selectedRole, 3));
        }
        else {
            sorter.setRowFilter(null);
        }
    }

    private void promoteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            String email = (String) userModel.getValueAt(selectedRow, 0);
            updateRole(email, "staff");
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to promote.");
        }
    }

    private void demoteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            String email = (String) userModel.getValueAt(selectedRow, 0);
            updateRole(email, "customer");
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to demote.");
        }
    }

    private void updateRole(String email, String newRole) {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();

            String query = "UPDATE User SET Role = ? WHERE Email = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newRole);
            ps.setString(2, email);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "User role updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error updating user role.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error.");
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
