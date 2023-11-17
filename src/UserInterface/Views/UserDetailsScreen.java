package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.User;
import javax.swing.*;
import java.awt.*;

public class UserDetailsScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User user;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel roleLabel;

    public UserDetailsScreen(DatabaseConnectionHandler dbHandler, User user) {
        this.dbHandler = dbHandler;
        this.user = user;
        createUI();
    }

    private void createUI() {
        setTitle("User Details - Trains of Sheffield");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Use the User object to set the text of the labels
        nameLabel = new JLabel("Name: " + user.getForename() + " " + user.getSurname());
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(nameLabel);

        emailLabel = new JLabel("Email: " + user.getEmail());
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(emailLabel);

        roleLabel = new JLabel("Role: " + user.getRole().toString());
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(roleLabel);

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> goBack());
        add(backButton);
    }

    private void goBack() {
        HomePage homePage = new HomePage(dbHandler, user);
        homePage.setVisible(true);
        dispose(); // Close the UserDetailsScreen
    }
}
