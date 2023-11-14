package Database;

import Models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseOperations {

    // Insert a new user into the database
    public void insertUser(User newUser, Connection connection) throws SQLException {
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

    // Authenticate a user based on email and password
    // Modify the authenticateUser method
    public AuthenticationResult authenticateUser(String email, String password, Connection connection) throws SQLException {
        try {
            String selectSQL = "SELECT role FROM User WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new AuthenticationResult(true, resultSet.getString("role"));
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
        private String role;

        // Constructor, getters, and setters
        public AuthenticationResult(boolean isAuthenticated, String role) {
            this.isAuthenticated = isAuthenticated;
            this.role = role;
        }

        public boolean isAuthenticated() {
            return isAuthenticated;
        }

        public String getRole() {
            return role;
        }
    }

}
