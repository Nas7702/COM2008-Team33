package UserInterface.Main;

import Database.DatabaseConnectionHandler;
import UserInterface.Views.LoginScreen;
import UserInterface.Views.SignUp;

public class Main {
    public static void main(String[] args) {
        DatabaseConnectionHandler dbHandler = new DatabaseConnectionHandler();
        LoginScreen loginScreen = new LoginScreen(dbHandler);
        loginScreen.setVisible(true);

//        SignUp signup = new SignUp(dbHandler);
//        signup.setVisible(true);
    }
}
