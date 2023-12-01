package Database;

import Models.Product;
import Models.User;
import UserInterface.Views.HashedPasswordGenerator;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseOperations {


    // Insert a new user into the database
    public void insUser(User newUser, Connection connection) throws SQLException {
        try {
            // SQL statement for inserting a new user
            String hashedPassword = HashedPasswordGenerator.generateHash(newUser.getPassword().toCharArray());

            String insertSQL = "INSERT INTO User (email, password, forename, surname, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            // Set the prepared statement parameters
            preparedStatement.setString(1, newUser.getEmail());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, newUser.getForename());
            preparedStatement.setString(4, newUser.getSurname());
            preparedStatement.setString(5, newUser.getRole().toString());

            // Execute the update and get the number of rows affected
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully.");
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new SQLException("Error during user insertion", e);
        }
    }
    // Get all users from the database
    public void getAllUsers(Connection connection) throws SQLException {
        try {
            // SQL statement for selecting all users
            String selectSQL = "SELECT * FROM User";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);

            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Print user details for each row in the result set
                System.out.println("User{" +
                        "UserID=" + resultSet.getInt("UserID") +
                        ", Email='" + resultSet.getString("Email") + '\'' +
                        ", Forename='" + resultSet.getString("Forename") + '\'' +
                        ", Surname='" + resultSet.getString("Surname") + '\'' +
                        ", Role='" + resultSet.getString("Role") + '\'' +
                        '}');
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Retrieve user details by email
    public User getUserDetails(String email, Connection connection) throws SQLException {
        User user = null;
        String selectSQL = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int userID = resultSet.getInt("UserID");
                    String password = resultSet.getString("Password");
                    String forename = resultSet.getString("Forename");
                    String surname = resultSet.getString("Surname");
                    User.userRole role = User.userRole.valueOf(resultSet.getString("Role").toUpperCase());
                    user = new User(userID, email, password, forename, surname, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return user;
    }


    public AuthenticationResult authenticateUser(String email, String plainPassword, Connection connection) throws SQLException {
        try {
            String selectSQL = "SELECT role, password FROM User WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String storedHash = resultSet.getString("password");
                String inputHash = HashedPasswordGenerator.generateHash(plainPassword.toCharArray());
                if (inputHash.equals(storedHash)) {
                    User.userRole role = User.userRole.valueOf(resultSet.getString("Role").toUpperCase());
                    return new AuthenticationResult(true, role);
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new SQLException("Authentication failed", e);
        }
        return new AuthenticationResult(false, null);
    }

    // A helper class to hold authentication result and user role
    public class AuthenticationResult {
        private boolean isAuthenticated;
        private User.userRole role;

        // Constructor
        public AuthenticationResult(boolean isAuthenticated, User.userRole role) {
            this.isAuthenticated = isAuthenticated;
            this.role = role;
        }

        // Getters
        public boolean isAuthenticated() {
            return isAuthenticated;
        }

        public User.userRole getRole() {
            return role;
        }
    }

    public boolean hasAddress(int userID, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM Address WHERE UserID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;

                }
            }
        }
        return false;
    }

    public boolean hasBankDetails(int userID, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM BankDetails WHERE UserID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public int getLastUserId(Connection connection) {
        int lastUserId = -1; // Initialize with a default value indicating not found/error
        String query = "SELECT MAX(UserID) AS LastUserID FROM User";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                lastUserId = resultSet.getInt("LastUserID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastUserId;
    }

    public void saveAddress(int userID, String houseNumber, String roadName, String city, String postcode, Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO Address (UserID, HouseNumber, RoadName, City, Postcode) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, houseNumber);
            preparedStatement.setString(3, roadName);
            preparedStatement.setString(4, city);
            preparedStatement.setString(5, postcode);
            preparedStatement.executeUpdate();
        }
    }

    public void saveBankDetails(int userID, String cardHolderName, String cardNumber, String expiryDate, String securityCode, Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO BankDetails (UserID, CardHolderName, CardNumber, ExpiryDate, SecurityCode) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, cardHolderName);
            preparedStatement.setString(3, cardNumber);
            preparedStatement.setString(4, expiryDate);
            preparedStatement.setString(5, securityCode);
            preparedStatement.executeUpdate();
        }
    }

    public int insertOrder(int userID, double totalCost, String status, Connection connection) throws SQLException {
        String insertOrderSQL = "INSERT INTO Orders (UserID, Date, Status, TotalCost) VALUES (?, CURDATE(), ?, ?)";
        int orderId = -1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrderSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, status);
            preparedStatement.setDouble(3, totalCost);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
        return orderId;
    }

    public void insertOrderItems(int orderId, List<Product> items, Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO OrderLine (OrderID, ProductID, Quantity, LineCost) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            for (Product item : items) {
                preparedStatement.setInt(1, orderId);
                preparedStatement.setInt(2, item.getProductID());
                preparedStatement.setInt(3, item.getQuantity());
                preparedStatement.setDouble(4, item.getRetailPrice() * item.getQuantity());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
    public void deleteOrder(Connection connection,int orderID)throws SQLException{
        //DELETES ORDER AND ORDERLINE
        String query = "DELETE FROM Orders WHERE OrderID=?";
        try (PreparedStatement ps = connection.prepareStatement(query);) {
            ps.setInt(1, orderID);
            ps.executeUpdate();
        }
        query = "DELETE FROM OrderLine WHERE OrderID=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderID);
            ps.executeUpdate();
        }
    }

    public String getAddress(User loggedInUser, Connection connection) throws SQLException {

        String addressString = "";
        String selectSQL = "SELECT HouseNumber, RoadName, City, Postcode FROM Address WHERE UserID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, loggedInUser.getUserID());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String houseNumber = resultSet.getString("HouseNumber");
                    String roadName = resultSet.getString("RoadName");
                    String city = resultSet.getString("City");
                    String postcode = resultSet.getString("Postcode");

                    // Create the address string
                    addressString = houseNumber + " " + roadName + ", " + city + ", " + postcode;
                } else {
                    return(" ");
                }
            }
        }
        return addressString;
    }

}
