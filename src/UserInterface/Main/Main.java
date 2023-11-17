package UserInterface.Main;

import Database.DatabaseConnectionHandler;
import UserInterface.Views.HomePage;
import UserInterface.Views.LoginScreen;
import UserInterface.Views.SignUp;

public class Main {
    public static void main(String[] args) {
        DatabaseConnectionHandler dbHandler = new DatabaseConnectionHandler();

        // Open the HomePage, passing null for the user role since the user is not logged in yet
        HomePage homePage = new HomePage(dbHandler, null);
        homePage.setVisible(true);

    }
}
