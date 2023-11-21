package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//The manager view displays a screen showing existing staff (displaying email, forename, surname)
//with options to remove a staff member, and a box to enter the email of a non-staff user (with only
//customer privileges) to promote to the staff role.
public class ManagerHomePage extends JFrame {
    private JButton pendingOrdersButton;
    private JPanel rightPanel;
    private DatabaseConnectionHandler dbHandler;
    private JLabel user;
    private JTextField promoteField;
    private User loggedInUser;
    private JButton demoteStaff;
    private JList<Models.User> staffList;
    private DefaultListModel<Models.User> staffModel;

    public ManagerHomePage(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser=loggedInUser;
        createUI();
        loadStaff();
    }

    private void createUI() {
        setTitle("Manager Homepage");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        staffModel = new DefaultListModel<>();
        staffList = new JList<>(staffModel);
        add(new JScrollPane(staffList));

        JButton demoteStaff = new JButton("Demote Selected User");
        demoteStaff.addActionListener(e -> {
            demoteUser(staffList.getSelectedValue());
            staffModel.clear();
            loadStaff();
        });
        rightPanel.add(demoteStaff);

        promoteField = new JTextField();
        rightPanel.add(promoteField, BorderLayout.EAST);

        JButton promoteButton = new JButton("Promote entered user");
        promoteButton.addActionListener(e -> {
            promoteUser(promoteField.getText());
            staffModel.clear();
            loadStaff();
        });
        rightPanel.add(promoteButton, BorderLayout.EAST);
        add(rightPanel, BorderLayout.EAST);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        add(backButton, BorderLayout.SOUTH);

    }

    private void loadStaff() {
        try {
            dbHandler.openConnection();  // Open the connection
            Connection connection = dbHandler.getConnection();  // Get the connection

            String query = "SELECT * FROM User WHERE Role=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,"staff");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String email = rs.getString("Email");
                String password=rs.getString("Password");
                String forename=rs.getString("Forename");
                String surname=rs.getString("Surname");
                User.userRole role;
                Models.User user = new Models.User(email, password, forename, surname, User.userRole.STAFF);
                staffModel.addElement(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading staff.");
        } finally {
            dbHandler.closeConnection();  // Close the connection after use
        }
    }

    private void demoteUser(User user) {
        try {
            dbHandler.openConnection();  // Open the connection
            Connection connection = dbHandler.getConnection();  // Get the connection

            String query = "UPDATE User SET Role = 'customer' WHERE Email =?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error demoting staff.");
        } finally {
            dbHandler.closeConnection();  // Close the connection after use
        }
    }
    private void promoteUser(String email) {
        try {
            dbHandler.openConnection();  // Open the connection
            Connection connection = dbHandler.getConnection();  // Get the connection

            String query = "UPDATE User SET Role = 'staff' WHERE Email =?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error promoting staff.");
        } finally {
            dbHandler.closeConnection();  // Close the connection after use
        }
    }


    private void openPendingOrdersScreen() {
        PendingOrders pendingOrdersScreen = new PendingOrders(dbHandler, loggedInUser);
        pendingOrdersScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }
    private void goBack() {
        StaffHomePage staffPage = new StaffHomePage(dbHandler, loggedInUser); // Pass the loggedInUser
        staffPage.setVisible(true);
        dispose(); // Close the Manager screen
    }



}



