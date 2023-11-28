package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.User;
import javax.swing.*;
import java.awt.*;

public class UserDetailsScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel roleLabel;

    public UserDetailsScreen(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        createUI();
    }

    private void createUI() {
        setTitle("User Details - Trains of Sheffield");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        nameLabel = new JLabel("Name: " + loggedInUser.getForename() + " " + loggedInUser.getSurname());
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(nameLabel);

        emailLabel = new JLabel("Email: " + loggedInUser.getEmail());
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(emailLabel);

        roleLabel = new JLabel("Role: " + loggedInUser.getRole().toString());
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(roleLabel);

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> goBack());
        add(backButton);

        JButton editDetailsButton = new JButton("Edit Details");
        editDetailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editDetailsButton.addActionListener(e -> editDetails());
        add(editDetailsButton);
    }

    private void goBack() {
        HomePage homePage = new HomePage(dbHandler, loggedInUser);
        homePage.setVisible(true);
        dispose();
    }
    private void editDetails() {
        EditDetailsScreen editDetailsScreen = new EditDetailsScreen(dbHandler, loggedInUser);
        editDetailsScreen.setVisible(true);
        dispose();
    }
}
