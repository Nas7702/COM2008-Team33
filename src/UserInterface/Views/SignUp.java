package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class SignUp extends JFrame implements ActionListener {

    private JTextField txtForename;
    private JTextField txtSurname;
    private JPasswordField txtPassword;
    private JTextField txtEmail;
    private JButton btnSubmit;

    private DatabaseConnectionHandler dbHandler;
    private DatabaseOperations dbOperations;

    public SignUp(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
        this.dbOperations = new DatabaseOperations();
        createUI();
    }

    private void createUI() {
        setTitle("Sign Up");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2)); // Adjusted for the number of components

        addComponents();

        setVisible(true);
    }

    private void addComponents() {
        add(new JLabel("Forename:"));
        txtForename = new JTextField();
        add(txtForename);

        add(new JLabel("Surname:"));
        txtSurname = new JTextField();
        add(txtSurname);

        add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        add(new JLabel("Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(this);
        add(btnSubmit);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSubmit) {
            String forename = txtForename.getText();
            String surname = txtSurname.getText();
            String password = new String(txtPassword.getPassword()); // It's safer to use getPassword()
            String email = txtEmail.getText();

            try {
                dbHandler.openConnection(); // Open database connection
                Connection connection = dbHandler.getConnection();

                if (connection != null) {
                    Models.User newUser = new Models.User(email, password, forename, surname, User.userRole.CUSTOMER);
                    dbOperations.insUser(newUser, connection);
                    JOptionPane.showMessageDialog(this, "Sign Up Successful for " + forename);
                } else {
                    JOptionPane.showMessageDialog(this, "Database connection failed.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error during sign up: " + ex.getMessage());
            } finally {
                dbHandler.closeConnection(); // Close database connection
            }
        }
    }
}