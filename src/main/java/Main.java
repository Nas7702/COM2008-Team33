import com.sheffield.DatabaseConnectionHandler;
import com.sheffield.DatabaseOperations;
import com.sheffield.User;

public class Main {
    public static void main(String[] args) {
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        DatabaseOperations databaseOperations = new DatabaseOperations();

        try {
            // Open database connection
            databaseConnectionHandler.openConnection();

            // Create a user instance
            User user1 = new User(1, "john.doe@example.com", "Secure*Pass4John", "John", "Doe", "123 Main St", false, false);

            // Insert the user into the database
            databaseOperations.insertUser(user1, databaseConnectionHandler.getConnection());

            // Create another user instance
            User user2 = new User(2, "jane.doe@example.com", "Secure*Pass4Jane", "Jane", "Doe", "456 Maple St", true, false);

            // Insert the second user into the database
            databaseOperations.insertUser(user2, databaseConnectionHandler.getConnection());

            // Retrieve and display all users
            databaseOperations.getAllUsers(databaseConnectionHandler.getConnection());

            // Update a user's address
            user1.setAddress("789 Oak St");
            databaseOperations.updateUser(user1, databaseConnectionHandler.getConnection());

            // Retrieve and display user by ID to see the updated address
            databaseOperations.getUserByID(user1.getUserID(), databaseConnectionHandler.getConnection());

            // Delete a user
            databaseOperations.deleteUser(user2.getUserID(), databaseConnectionHandler.getConnection());

            // Retrieve all users to show the remaining users after deletion
            databaseOperations.getAllUsers(databaseConnectionHandler.getConnection());

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            // Close the database connection
            databaseConnectionHandler.closeConnection();
        }
    }
}
