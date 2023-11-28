package UserInterface.Views;

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

public class SignUp extends JFrame implements ActionListener {

    private JTextField txtForename;
    private JTextField txtSurname;
    private JPasswordField txtPassword;
    private JTextField txtEmail;
    private JButton btnSubmit;
    private JLabel titleLabel;
    private JPanel mainPanel;

    private DatabaseConnectionHandler dbHandler;
    private DatabaseOperations dbOperations;

    public SignUp(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
        this.dbOperations = new DatabaseOperations();
        createUI();
    }

    private void createUI() {
        setTitle("Sign Up - Trains of Sheffield");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel("Create a New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

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
        btnSubmit.addActionListener(this);
        btnSubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(btnSubmit);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSubmit) {
            String forename = txtForename.getText();
            String surname = txtSurname.getText();
            String password = new String(txtPassword.getPassword());
            String email = txtEmail.getText();
            try {
                dbHandler.openConnection();
                Connection connection = dbHandler.getConnection();

                if (forename.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in each field.");
                    return;
                }
                if (!email.contains("@") || !email.contains(".")) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
                    return;
                }
                if (!isEmailUnique(email, connection)) {
                    JOptionPane.showMessageDialog(this, "Email already exists. Please use a different email.");
                    return;
                }
                if (password.length() < 8) {
                    JOptionPane.showMessageDialog(this, "Password must contain at least 8 characters.");
                    return;
                }

                if (connection != null) {
                    Models.User newUser = new Models.User(email, password, forename, surname, User.userRole.CUSTOMER);
                    dbOperations.insUser(newUser, connection);
                    JOptionPane.showMessageDialog(this, "Sign Up Successful for " + forename);
                    this.dispose();
                     LoginScreen loginScreen = new LoginScreen(dbHandler);
                     loginScreen.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(this, "Database connection failed.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error during sign up: " + ex.getMessage());
            } finally {
                dbHandler.closeConnection();
            }
        }
    }
    private boolean isEmailUnique(String email, Connection connection) {
        try {
            String query = "SELECT COUNT(*) FROM User WHERE Email = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt(1) == 0;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

}