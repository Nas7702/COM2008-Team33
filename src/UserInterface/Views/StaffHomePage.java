package UserInterface.Views;


import Database.DatabaseConnectionHandler;
import Models.User;

import javax.swing.*;
import java.awt.*;

public class StaffHomePage extends JFrame {
    private JButton pendingOrdersButton;
    private JButton ManagerPageButton;

    private JButton manageUserButton;

    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;

    public StaffHomePage(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        createUI(loggedInUser);
    }

    private void createUI(User loggedInUser) {
        setTitle("Staff Page");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        // Styling
        Color buttonColor = new Color(100, 149, 237);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        setLayout(new BorderLayout()); // Set BorderLayout for the main frame

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 10 is the gap between buttons

        pendingOrdersButton = new JButton("View Pending Orders");
        pendingOrdersButton.addActionListener(e -> openPendingOrdersScreen());
        buttonsPanel.add(pendingOrdersButton); // Add to the buttons panel
        add(buttonsPanel, BorderLayout.EAST); // Add buttons panel to the center

        manageUserButton = new JButton("Manage Users Details");
        manageUserButton.addActionListener(e -> manageUserAccounts());
        buttonsPanel.add(manageUserButton); // Add to the buttons panel
        add(buttonsPanel, BorderLayout.CENTER); // Add buttons panel to the center

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        add(backButton, BorderLayout.SOUTH);


        if (loggedInUser.getRole().equals(User.userRole.MANAGER)) {
            ManagerPageButton = new JButton("Manager Homepage");
            ManagerPageButton.addActionListener(e -> managerPage());
            buttonsPanel.add(ManagerPageButton);
        }

    }

    private void managerPage() {
        ManagerHomePage managerScreen = new ManagerHomePage(dbHandler, loggedInUser);
        managerScreen.setVisible(true);
        this.dispose();
    }


    private void openPendingOrdersScreen() {
        PendingOrders pendingOrdersScreen = new PendingOrders(dbHandler, loggedInUser);
        pendingOrdersScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }

    private void goBack() {
        HomePage homePage = new HomePage(dbHandler, loggedInUser); // Pass the loggedInUser
        homePage.setVisible(true);
        dispose(); // Close the ProductCatalogScreen
    }

    private void manageUserAccounts() {
        ManageUsersScreen manageUsersScreen = new ManageUsersScreen(dbHandler, loggedInUser);
        manageUsersScreen.setVisible(true);
        this.dispose();
    }


}
