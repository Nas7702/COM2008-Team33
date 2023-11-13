package UserInterface.Main;

import Database.DatabaseConnectionHandler;
import UserInterface.Views.HomePage;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create a DatabaseConnectionHandler instance
            DatabaseConnectionHandler dbHandler = new DatabaseConnectionHandler();

            // Pass the dbHandler to the HomePage
            HomePage homePage = new HomePage(dbHandler);
            homePage.setVisible(true);
        });
    }
}
