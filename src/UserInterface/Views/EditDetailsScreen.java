package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;
import Models.User;
import Models.Address;
import Models.BankDetails;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionListener;

public class EditDetailsScreen extends JFrame implements ActionListener{
    private DatabaseConnectionHandler dbHandler;
    private DatabaseOperations dbOperations;
    private User loggedInUser;
    private Address address;
    private BankDetails bankDetails;
    private JLabel titleLabel;
    private JLabel personalDetailsLabel;
    private JLabel addressLabel;
    private JLabel bankLabel;
    private JLabel forenameLabel;
    private JLabel surnameLabel;
    private JLabel emailLabel;
    private JLabel houseNumberLabel;
    private JLabel roadNameLabel;
    private JLabel cityLabel;
    private JLabel postcodeLabel;
    private JLabel cardName;
    private JLabel cardHolderName;
    private JLabel cardNumber;
    private JLabel expiryDate;
    private JLabel securityCode;
    private JTextField txtForename;
    private JTextField txtSurname;
    private JTextField txtEmail;
    private JTextField txtHouseNumber;
    private JTextField txtRoadName;
    private JTextField txtCity;
    private JTextField txtPostcode;
    private JTextField txtCardName;
    private JTextField txtCardHolderName;
    private JTextField txtCardNumber;
    private JTextField txtExpiryDate;
    private JTextField txtSecurityCode;
    private JButton btnSubmit;
    private JPanel mainPanel;
    private JPanel personalDetailsPanel;
    private JPanel addressPanel;
    private JPanel bankDetailsPanel;

    public EditDetailsScreen(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        // Initialise database connection and user
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        this.dbOperations = new DatabaseOperations();
        createUI();
    }

    private void createUI() {
        // Create base page format
        setTitle("Edit User Details - Trains of Sheffield");
        setSize(500, 800);
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

        //initialisation of main panels for presentation
        personalDetailsPanel = new JPanel();
        personalDetailsPanel.setLayout(new BoxLayout(personalDetailsPanel, BoxLayout.Y_AXIS));
        personalDetailsPanel.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10));

        addressPanel = new JPanel();
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.Y_AXIS));
        addressPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bankDetailsPanel = new JPanel();
        bankDetailsPanel.setLayout(new BoxLayout(bankDetailsPanel, BoxLayout.Y_AXIS));
        bankDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 80, 10));

        //inputs for all details
        JLabel personalDetailsLabel = new JLabel("Personal Details");
        personalDetailsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        personalDetailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        personalDetailsPanel.add(personalDetailsLabel);

        txtForename = new JTextField();
        txtForename.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtForename.getPreferredSize().height));
        JLabel forenameLabel = new JLabel("Forename:");
        forenameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        personalDetailsPanel.add(forenameLabel);
        personalDetailsPanel.add(txtForename);

        txtSurname = new JTextField();
        txtSurname.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtSurname.getPreferredSize().height));
        JLabel surnameLabel = new JLabel("Surname:");
        surnameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        personalDetailsPanel.add(surnameLabel);
        personalDetailsPanel.add(txtSurname);

        txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtEmail.getPreferredSize().height));
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        personalDetailsPanel.add(emailLabel);
        personalDetailsPanel.add(txtEmail);

        JLabel addressLabel = new JLabel("Address");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 24));
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addressPanel.add(addressLabel);

        txtHouseNumber = new JTextField();
        txtHouseNumber.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtHouseNumber.getPreferredSize().height));
        JLabel houseNumberLabel = new JLabel("House Number:");
        houseNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addressPanel.add(houseNumberLabel);
        addressPanel.add(txtHouseNumber);


        txtRoadName = new JTextField();
        txtRoadName.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtRoadName.getPreferredSize().height));
        JLabel roadNameLabel = new JLabel("Road Name:");
        roadNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addressPanel.add(roadNameLabel);
        addressPanel.add(txtRoadName);

        txtCity = new JTextField();
        txtCity.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtCity.getPreferredSize().height));
        JLabel cityLabel = new JLabel("City:");
        cityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addressPanel.add(cityLabel);
        addressPanel.add(txtCity);

        txtPostcode = new JTextField();
        txtPostcode.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtPostcode.getPreferredSize().height));
        JLabel postcodeLabel = new JLabel("Postcode:");
        postcodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addressPanel.add(postcodeLabel);
        addressPanel.add(txtPostcode);

        JLabel bankLabel = new JLabel("Bank Details");
        bankLabel.setFont(new Font("Arial", Font.BOLD, 24));
        bankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankDetailsPanel.add(bankLabel);

        txtCardName = new JTextField();
        txtCardName.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtPostcode.getPreferredSize().height));
        JLabel cardNameLabel = new JLabel("Card Name:");
        cardNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankDetailsPanel.add(cardNameLabel);
        bankDetailsPanel.add(txtCardName);

        txtCardHolderName = new JTextField();
        txtCardHolderName.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtCardHolderName.getPreferredSize().height));
        JLabel cardHolderNameLabel = new JLabel("Card Holder Name:");
        cardHolderNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankDetailsPanel.add(cardHolderNameLabel);
        bankDetailsPanel.add(txtCardHolderName);

        txtCardNumber = new JTextField();
        txtCardNumber.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtCardNumber.getPreferredSize().height));
        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankDetailsPanel.add(cardNumberLabel);
        bankDetailsPanel.add(txtCardNumber);


        txtExpiryDate = new JTextField();
        txtExpiryDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtCardNumber.getPreferredSize().height));
        JLabel expiryDateLabel = new JLabel("Expiry Month/Year (MM/YY format)");
        expiryDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankDetailsPanel.add(expiryDateLabel);
        bankDetailsPanel.add(txtExpiryDate);


        txtSecurityCode = new JTextField();
        txtSecurityCode.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtSecurityCode.getPreferredSize().height));
        JLabel securityCodeLabel = new JLabel("Security Code:");
        securityCodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankDetailsPanel.add(securityCodeLabel);
        bankDetailsPanel.add(txtSecurityCode);


        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(this);
        btnSubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(btnSubmit);
        mainPanel.add(personalDetailsPanel);
        mainPanel.add(addressPanel);
        mainPanel.add(bankDetailsPanel);
        add(mainPanel);
    }

    private void goBack() {
        HomePage homePage = new HomePage(dbHandler, loggedInUser);
        homePage.setVisible(true);
        dispose(); // Close the EditDetailsScreen
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSubmit) {
            String forename = txtForename.getText();
            String surname = txtSurname.getText();
            String email = txtEmail.getText();
            String houseNum = txtHouseNumber.getText();
            String roadName = txtRoadName.getText();
            String city = txtCity.getText();
            String postcode = txtPostcode.getText();
            String cardName = txtCardName.getText();
            String cardHolderName = txtCardHolderName.getText();
            String cardNumber = txtCardNumber.getText();
            String expiryDate = txtExpiryDate.getText();
            String securityCode = txtSecurityCode.getText();
            try {
                dbHandler.openConnection();
                Connection connection = dbHandler.getConnection();
                // Checking if email is valid
                if (!email.isEmpty()) {
                    if (!email.contains("@") || !email.contains(".")) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
                        return;
                    }
                }
                //Checking if house number is a number
                if (!houseNum.isEmpty()) {
                    if (!houseNum.matches("\\d+")) {
                        JOptionPane.showMessageDialog(this, "House number invalid: should be a number.");
                        return;
                    }
                }
                // Checking if postcode is correct length
                if (!postcode.isEmpty()) {
                    if (postcode.length() < 5 || postcode.length() > 7) {
                        JOptionPane.showMessageDialog(this, "Postcode invalid length (should be between 5 and 7 characters).");
                        return;
                    }
                }
                // Checking if card number is 16 digits
                if (!cardNumber.isEmpty()) {
                    if (!cardNumber.matches("\\d{16}")) {
                        JOptionPane.showMessageDialog(this, "Card number invalid: should only contain 16 digits.");
                        return;
                    }
                }
                // Checking if expiry date is a valid date
                if (!expiryDate.isEmpty()) {
                    if (!expiryDate.matches("\\d{2}/\\d{2}")) {
                        JOptionPane.showMessageDialog(this, "Expiry date invalid: Should be in the format MM/YY.");
                        return;
                    }
                }
                // Checking if security code is 3 digits
                if (!securityCode.isEmpty()) {
                    if (!securityCode.matches("\\d{3}")) {
                        JOptionPane.showMessageDialog(this, "Security code invalid: should only contain 3 digits.");
                        return;
                    }
                }
                if (forename.isEmpty() && surname.isEmpty() && email.isEmpty() &&
                        houseNum.isEmpty() && roadName.isEmpty() && city.isEmpty() &&
                        postcode.isEmpty() && cardName.isEmpty() && cardHolderName.isEmpty() &&
                        cardNumber.isEmpty() && expiryDate.isEmpty() && securityCode.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All boxes have been left blank");
                    return;
                }
                // Updates details and returns user to home screen
                if (connection != null) {
                    updatePersonalDetails(forename, surname, email);
                    updateAddress(houseNum, roadName, city, postcode);
                    updateBankDetails(cardName, cardHolderName, cardNumber, expiryDate, securityCode);
                    this.dispose(); // Close the edit details screen
                    HomePage homepage = new HomePage(dbHandler, loggedInUser);
                    homepage.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(this, "Database connection failed.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            } finally {
                dbHandler.closeConnection();
            }
        }
    }
    private void updatePersonalDetails(String forename, String surname, String email) {
        // insert sql query to alter user personal details
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();
            if (!forename.isBlank()) {
                String query = "UPDATE User SET Forename = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, forename);
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
                loggedInUser.setForename(forename);
            }
            if (!surname.isBlank()) {
                String query = "UPDATE User SET Surname = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, surname);
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
                loggedInUser.setSurname(surname);
            }
            if (!email.isBlank()) {
                String query = "UPDATE User SET Email = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, email);
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
                loggedInUser.setEmail(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error.");
        } finally {
            dbHandler.closeConnection();
        }
    }
    private void updateAddress(String houseNum, String roadName, String city, String postcode) {
        // insert sql query to alter user address details
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();
            if (!houseNum.isBlank()) {
                String query = "UPDATE Address SET HouseNumber = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, houseNum);
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
            }
            if (!roadName.isBlank()) {
                String query = "UPDATE Address SET RoadName = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, roadName);
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
            }
            if (!city.isBlank()) {
                String query = "UPDATE Address SET City = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, city);
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
            }
            if (!postcode.isBlank()) {
                String query = "UPDATE Address SET Postcode = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, postcode);
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error.");
        } finally {
            dbHandler.closeConnection();
        }
    }
    private void updateBankDetails(String cardName, String cardHolderName, String cardNumber,
                                   String expiryDate, String securityCode) {
        // insert sql query to alter user bank details
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();
            if (!cardName.isBlank()) {
                String query = "UPDATE BankDetails SET CardName = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, cardName);
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
            }
            if (!cardHolderName.isBlank()) {
                String query = "UPDATE BankDetails SET CardHolderName = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, cardHolderName);
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
            }
            if (!cardNumber.isBlank()) {
                String query = "UPDATE BankDetails SET CardNumber = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, cardNumber);
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
            }
            if (!expiryDate.isBlank()) {
                String query = "UPDATE BankDetails SET ExpiryDate = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, convertToMySQLDateFormat(expiryDate));
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
            }
            if (!securityCode.isBlank()) {
                String query = "UPDATE BankDetails SET SecurityCode = ? WHERE UserID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, securityCode);
                ps.setInt(2, loggedInUser.getUserID());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error.");
        } finally {
            dbHandler.closeConnection();
        }
    }
    private String convertToMySQLDateFormat(String expiryDate) {
        String[] parts = expiryDate.split("/");
        String month = parts[0];
        String year = "20" + parts[1];
        return year + "-" + month + "-01";
    }
}