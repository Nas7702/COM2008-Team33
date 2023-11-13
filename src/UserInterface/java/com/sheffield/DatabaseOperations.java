package com.sheffield;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseOperations {

    // Insert a new user into the database
    public void insertUser(User newUser, Connection connection) throws SQLException {
        try {
            // Create an SQL INSERT statement for the User
            String insertSQL = "INSERT INTO Users (user_id, email, password, first_name, last_name, address, is_staff, is_manager) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            // Prepare and execute the INSERT statement for User
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, newUser.getUserID());
            preparedStatement.setString(2, newUser.getEmail());
            preparedStatement.setString(3, newUser.getPassword());
            preparedStatement.setString(4, newUser.getFirstName());
            preparedStatement.setString(5, newUser.getLastName());
            preparedStatement.setString(6, newUser.getAddress());
            preparedStatement.setBoolean(7, newUser.getIsStaff());
            preparedStatement.setBoolean(8, newUser.getIsManager());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to signal an error.
        }
    }

    // Get all users from the database
    public void getAllUsers(Connection connection) throws SQLException {
        try {
            String selectSQL = "SELECT * FROM Users";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("<=================== GET ALL USERS ====================>");
            while (resultSet.next()) {
                // Print each user's information in the specified format
                System.out.println("{" +
                        "userID='" + resultSet.getInt("user_id") + "'" +
                        ", email='" + resultSet.getString("email") + "'" +
                        ", firstName='" + resultSet.getString("first_name") + "'" +
                        ", lastName='" + resultSet.getString("last_name") + "'" +
                        ", address='" + resultSet.getString("address") + "'" +
                        ", isStaff='" + resultSet.getBoolean("is_staff") + "'" +
                        ", isManager='" + resultSet.getBoolean("is_manager") + "'" +
                        "}");
            }
            System.out.println("<======================================================>");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;// Re-throw the exception to signal an error.
        }
    }

    // Get a user by user ID
    public void getUserByID(int userID, Connection connection) throws SQLException {
        try {
            String selectSQL = "SELECT * FROM Users WHERE user_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("<==================== USER BY ID =====================>");
            if (resultSet.next()) {
                System.out.println("{" +
                        "userID='" + resultSet.getInt("user_id") + "'" +
                        ", email='" + resultSet.getString("email") + "'" +
                        ", firstName='" + resultSet.getString("first_name") + "'" +
                        ", lastName='" + resultSet.getString("last_name") + "'" +
                        ", address='" + resultSet.getString("address") + "'" +
                        ", isStaff='" + resultSet.getBoolean("is_staff") + "'" +
                        ", isManager='" + resultSet.getBoolean("is_manager") + "'" +
                        "}");
            } else {
                System.out.println("User with ID " + userID + " not found.");
            }
            System.out.println("<=======================================================>");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;// Re-throw the exception to signal an error.
        }
    }

    // Update an existing user in the database
    public void updateUser(User user, Connection connection) throws SQLException {
        try {
            String updateSQL = "UPDATE Users SET email=?, password=?, first_name=?, last_name=?, address=?, is_staff=?, is_manager=? WHERE user_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getAddress());
            preparedStatement.setBoolean(6, user.getIsStaff());
            preparedStatement.setBoolean(7, user.getIsManager());
            preparedStatement.setInt(8, user.getUserID());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " row(s) updated successfully.");
            } else {
                System.out.println("No rows were updated for User ID: " + user.getUserID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;// Re-throw the exception to signal an error.
        }
    }

    // Delete a user from the database by user ID
    public void deleteUser(int userID, Connection connection) throws SQLException {
        try {
            String deleteSQL = "DELETE FROM Users WHERE user_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, userID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " row(s) deleted successfully.");
            } else {
                System.out.println("No rows were deleted for User ID: " + userID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;// Re-throw the exception to signal an error.
        }
    }
}
