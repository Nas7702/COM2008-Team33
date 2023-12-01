package UserInterface.Views;

import Database.DatabaseOperations;
import Database.DatabaseConnectionHandler;
import Models.User;
import Models.Product;
import com.mysql.cj.jdbc.JdbcPreparedStatement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddProduct extends JFrame{
    private DatabaseConnectionHandler dbHandler;
    Models.Product product;
    User loggedInUser;
    JTextField brandNameField,productNameField,productCodeField,retailPriceField,gaugeField,eraField,DCCCodeField;
    public AddProduct(DatabaseConnectionHandler dbHandler, User loggedInUser){
        this.dbHandler = dbHandler;
        this.loggedInUser=loggedInUser;
        createUI();
    }
    private void createUI() {
        setTitle("Staff Page");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        Color buttonColor = new Color(100, 149, 237);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(9,2));

        buttonsPanel.add(new JLabel("Brand:"));
        brandNameField= new JTextField();
        buttonsPanel.add(brandNameField);

        buttonsPanel.add(new JLabel("Product name:"));
        productNameField= new JTextField();
        buttonsPanel.add(productNameField);

        buttonsPanel.add(new JLabel("Product code:"));
        productCodeField= new JTextField();
        buttonsPanel.add(productCodeField);

        buttonsPanel.add(new JLabel("Retail price:"));
        retailPriceField= new JTextField();
        buttonsPanel.add(retailPriceField);

        buttonsPanel.add(new JLabel("Gauge:"));
        gaugeField= new JTextField();
        buttonsPanel.add(gaugeField);

        buttonsPanel.add(new JLabel("Era:"));
        eraField= new JTextField();
        buttonsPanel.add(eraField);

        buttonsPanel.add(new JLabel("DCC code:"));
        DCCCodeField= new JTextField();
        buttonsPanel.add(DCCCodeField);

        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(e -> {
            String brandName= brandNameField.getText();
            String productName= productNameField.getText();
            String productCode= productCodeField.getText();
            double retailPrice= Double.parseDouble(retailPriceField.getText());
            String gauge= gaugeField.getText();
            String era= eraField.getText();
            String DCCCode= DCCCodeField.getText();
            addItem(brandName,productName,productCode,retailPrice,gauge,era,DCCCode);

            if (productCode.substring(0, 1).equals("M") || productCode.substring(0, 1).equals("P")) {
                String superPC = productCode; //old product code becomes parent code for pack
                JOptionPane.showMessageDialog(this, "You are adding a pack, please enter all components' product code and quantity per pack");
                buttonsPanel.removeAll();
                JPanel extraPanel = new JPanel();
                buttonsPanel.setLayout(new GridLayout(9, 2));

                extraPanel.add(new JLabel("Product code:"));
                JTextField newProductCode = new JTextField();
                extraPanel.add(newProductCode);

                extraPanel.add(new JLabel("Quantity per pack"));
                JTextField quantityField = new JTextField();
                extraPanel.add(quantityField);

                JButton extraSubmitButton = new JButton("Submit");
                extraSubmitButton.addActionListener(event -> {
                    String newproductcode=newProductCode.getText();
                    if (!superPC.equals(newproductcode)){
                        int newquantity=Integer.parseInt(quantityField.getText());
                        addPackComponents(newproductcode,newquantity,superPC);
                    } else {
                        JOptionPane.showMessageDialog(this, "Cannot add Parent pack to the pack");
                    }
                    newProductCode.setText("");
                    quantityField.setText("");
                });

                JButton finishedButtton = new JButton("Finished");
                extraSubmitButton.addActionListener(event -> {
                    createUI();
                });
            }

            goBack();
        });
        buttonsPanel.add(submitButton);
        add(buttonsPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBack());
        add(backButton, BorderLayout.SOUTH);
    }

    private void addPackComponents(String productCode, int quantity, String superProductCode){
        try {
            dbHandler.openConnection();  // Open the connection
            Connection connection = dbHandler.getConnection();  // Get the connection

            String query = "SELECT ProductCode FROM Product WHERE ProductCode=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, productCode);
            ResultSet rs =ps.executeQuery();
            if (rs.next()){ //checks that pack component is a real product
                query = "INSERT INTO BoxedSet (`ProductCode`, `BoxedSetProductCode`,'Quantity'" +
                        "VALUES (?,?,?)";
                ps = connection.prepareStatement(query);
                ps.setString(1, productCode);
                ps.setString(2,superProductCode);
                ps.setInt(3, quantity);
                ps.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHandler.closeConnection();  // Close the connection after use
        }
    }
    private void addItem(String brandName, String productName, String productCode, double retailPrice,String gauge,String era,String DCCCode){
        try {
            dbHandler.openConnection();  // Open the connection
            Connection connection = dbHandler.getConnection();  // Get the connection

            String query = "INSERT INTO Product (`BrandName`, `ProductName`, `ProductCode`, `RetailPrice`, `Gauge`, `Era`, `DCCCode`, `Quantity`) " +
                    "VALUES (?,?,?,?,?,?,?,0)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, brandName);
            ps.setString(2, productName);
            ps.setString(3, productCode);
            ps.setDouble(4, retailPrice);
            ps.setString(5, gauge);
            ps.setString(6, era);
            ps.setString(7, DCCCode);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Product added");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding product");
        } finally {
            dbHandler.closeConnection();  // Close the connection after use

        }
    }
    private void goBack() {
        StaffHomePage homePage = new StaffHomePage(dbHandler, loggedInUser); // Pass the loggedInUser
        homePage.setVisible(true);
        dispose();
    }


}
