package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.User;
import javax.swing.*;
import java.awt.*;

public class EditDetailsScreen extends JFrame{
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel roleLabel;
    private JTextField txtForename;
    private JTextField txtSurname;
    private JPasswordField txtPassword;
    private JTextField txtEmail;
    private JButton btnSubmit;
    private JLabel titleLabel;
    private JPanel mainPanel;

    public EditDetailsScreen(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        createUI();
    }

    private void createUI() {
        setTitle("Edit User Details - Trains of Sheffield");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Edit Details");
        titleLabel.setAlignmentX(Component.TOP_ALIGNMENT);

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        backButton.addActionListener(e -> goBack());
        add(backButton);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtForename = new JTextField();
        txtForename.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtForename.getPreferredSize().height));
        JLabel forenameLabel = new JLabel("Forename:");
        forenameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(forenameLabel);
        mainPanel.add(txtForename);

        txtSurname = new JTextField();
        txtSurname.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtSurname.getPreferredSize().height));
        JLabel surnameLabel = new JLabel("Surname:");
        surnameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(surnameLabel);
        mainPanel.add(txtSurname);

        txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtEmail.getPreferredSize().height));
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(emailLabel);
        mainPanel.add(txtEmail);

        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtPassword.getPreferredSize().height));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(passwordLabel);
        mainPanel.add(txtPassword);

        btnSubmit = new JButton("Submit");
        btnSubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(btnSubmit);

        add(mainPanel);
    }

    private void goBack() {
        HomePage homePage = new HomePage(dbHandler, loggedInUser);
        homePage.setVisible(true);
        dispose(); // Close the UserDetailsScreen
    }
}
