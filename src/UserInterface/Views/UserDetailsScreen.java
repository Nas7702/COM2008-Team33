package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import javax.swing.*;
import java.awt.*;

public class UserDetailsScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private String userRole;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel roleLabel;

    // Constructor takes in the DatabaseHandler and the user's role
    public UserDetailsScreen(DatabaseConnectionHandler dbHandler, String userRole) {
        this.dbHandler = dbHandler;
        this.userRole = userRole;
        createUI();
    }

    private void createUI() {
        setTitle("User Details - Trains of Sheffield");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        nameLabel = new JLabel("Name: John Doe");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(nameLabel);

        emailLabel = new JLabel("Email: johndoe@example.com");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(emailLabel);

        roleLabel = new JLabel("Role: " + userRole);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(roleLabel);

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> goBack());
        add(backButton);
    }

    private void goBack() {
        HomePage homePage = new HomePage(dbHandler, userRole);
        homePage.setVisible(true);
        dispose(); // Close the UserDetailsScreen
    }

    // In a real scenario, you'd have a method here to fetch user details from the database
    // and populate the labels accordingly.
}
