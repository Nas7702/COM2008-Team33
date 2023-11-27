package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Models.User;
import javax.swing.*;
import java.awt.*;

public class EditDetailsScreen extends JFrame{
    private JComboBox<String> monthBox;
    private JComboBox<String> yearBox;
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel roleLabel;
    private JLabel titleLabel;
    private JLabel houseNumberLabel;
    private JLabel roadNameLabel;
    private JLabel bankLabel;
    private JTextField txtForename;
    private JTextField txtSurname;
    private JPasswordField txtPassword;
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
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        createUI();
    }

    private void createUI() {
        setTitle("Edit User Details - Trains of Sheffield");
        setSize(800, 800);
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


        personalDetailsPanel = new JPanel();
        personalDetailsPanel.setLayout(new BoxLayout(personalDetailsPanel, BoxLayout.Y_AXIS));
        personalDetailsPanel.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10));

        addressPanel = new JPanel();
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.Y_AXIS));
        addressPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bankDetailsPanel = new JPanel();
        bankDetailsPanel.setLayout(new BoxLayout(bankDetailsPanel, BoxLayout.Y_AXIS));
        bankDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 80, 10));


        JLabel personalDetailsLabel = new JLabel("Personal Details");
        personalDetailsLabel.setFont(new Font("Arial", Font.BOLD, 20));
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
        addressLabel.setFont(new Font("Arial", Font.BOLD, 20));
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
        bankLabel.setFont(new Font("Arial", Font.BOLD, 20));
        bankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankDetailsPanel.add(bankLabel);

        txtCardName = new JTextField();
        txtCardName.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtPostcode.getPreferredSize().height));
        JLabel postcodeField = new JLabel("Postcode:");
        postcodeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankDetailsPanel.add(postcodeField);
        bankDetailsPanel.add(txtPostcode);

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

        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        monthBox = new JComboBox<>(months);
        bankDetailsPanel.add(monthBox);

        // Year ComboBox
        String[] years = {"2023", "2024", "2025", "2026", "2027"}; // You can populate this dynamically if needed
        yearBox = new JComboBox<>(years);
        bankDetailsPanel.add(yearBox);

        txtSecurityCode = new JTextField();
        txtSecurityCode.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtSecurityCode.getPreferredSize().height));
        JLabel securityCodeLabel = new JLabel("Security Code:");
        securityCodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankDetailsPanel.add(securityCodeLabel);
        bankDetailsPanel.add(txtSecurityCode);







        btnSubmit = new JButton("Submit");
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
        dispose(); // Close the UserDetailsScreen
    }
}
