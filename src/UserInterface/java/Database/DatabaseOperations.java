package Database;

import Models.Cart;
import Models.Product;
import Models.User;

import javax.swing.*;
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
            String insertSQL = "INSERT INTO User (email, password, forename, surname, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

            // Set the prepared statement parameters
            preparedStatement.setString(1, newUser.getEmail());
            preparedStatement.setString(2, newUser.getPassword());
            preparedStatement.setString(3, newUser.getForename());
            preparedStatement.setString(4, newUser.getSurname());
            preparedStatement.setString(5, newUser.getRole().toString());

            // Execute the update and get the number of rows affected
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
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
                    String password = resultSet.getString("Password"); // Password handling should be secure
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


    public AuthenticationResult authenticateUser(String email, String password, Connection connection) throws SQLException {
        try {
            String selectSQL = "SELECT role FROM User WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User.userRole role = User.userRole.valueOf(resultSet.getString("Role").toUpperCase()); // Convert to enum
                return new AuthenticationResult(true, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
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

    public int getPendingOrderId(int userID, Connection connection) throws SQLException {
        String query = "SELECT OrderID FROM Orders WHERE UserID = ? AND Status = 'pending'";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("OrderID");
                }
            }
        }
        return -1;
    }

    public void loadPendingOrder(int userID, Cart cart, Connection connection) throws SQLException {
        int orderId = getPendingOrderId(userID, connection);
        if (orderId != -1) {
            cart.clearCart();
            String query = "SELECT p.*, ol.Quantity FROM OrderLine ol JOIN Product p ON ol.ProductID = p.ProductID WHERE ol.OrderID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, orderId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Product product = new Product(rs.getInt("ProductID"),
                            rs.getString("BrandName"), rs.getString("ProductName"),
                            rs.getString("ProductCode"), rs.getDouble("RetailPrice"),
                            rs.getString("Gauge"), rs.getString("Era"),
                            rs.getString("DCCCode"), rs.getInt("Quantity"));
                    int quantity = rs.getInt("ol.Quantity");
                    cart.addItem(product, quantity);
                }
            }
        }
    }


    public void updateOrderItems(int orderId, int productId, int quantity, double lineCost, Connection connection) throws SQLException {
        // Check if the item already exists in the order
        String checkSQL = "SELECT Quantity FROM OrderLine WHERE OrderID = ? AND ProductID = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSQL)) {
            checkStmt.setInt(1, orderId);
            checkStmt.setInt(2, productId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                // Update the existing item
                int existingQuantity = rs.getInt("Quantity");
                String updateSQL = "UPDATE OrderLine SET Quantity = ?, LineCost = ? WHERE OrderID = ? AND ProductID = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {
                    updateStmt.setInt(1, existingQuantity + quantity);
                    updateStmt.setDouble(2, lineCost);
                    updateStmt.setInt(3, orderId);
                    updateStmt.setInt(4, productId);
                    updateStmt.executeUpdate();
                }
            } else {
                // Insert a new item
                String insertSQL = "INSERT INTO OrderLine (OrderID, ProductID, Quantity, LineCost) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
                    insertStmt.setInt(1, orderId);
                    insertStmt.setInt(2, productId);
                    insertStmt.setInt(3, quantity);
                    insertStmt.setDouble(4, lineCost);
                    insertStmt.executeUpdate();
                }
            }
        }
    }

    // This method updates the quantity of a specific product in the order
    public void updateDatabaseOrderItem(int orderId, Product product, int newQuantity, Connection connection) throws SQLException {
        String updateSQL = "UPDATE OrderLine SET Quantity = ?, LineCost = ? WHERE OrderID = ? AND ProductID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            double newLineCost = product.getRetailPrice() * newQuantity;

            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setDouble(2, newLineCost);
            preparedStatement.setInt(3, orderId);
            preparedStatement.setInt(4, product.getProductID());

            preparedStatement.executeUpdate();
        }
    }



    public void deleteOrderItem(Product item, Connection connection) throws SQLException {
        String deleteSQL = "DELETE FROM OrderLine WHERE ProductID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, item.getProductID());
            preparedStatement.executeUpdate();
        }
    }


}
