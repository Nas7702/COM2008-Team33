package UserInterface.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    private JButton loginButton;
    private JButton signupButton;

    public HomePage() {
        createUI();
    }

    private void createUI() {
        setTitle("Home - Trains of Sheffield");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new FlowLayout());

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginScreen();
            }
        });
        add(loginButton);

        signupButton = new JButton("Sign Up");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignupScreen();
            }
        });
        add(signupButton);
    }

    private void openLoginScreen() {
        // Open the Login Screen
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.setVisible(true);
    }

    private void openSignupScreen() {
        // Open the Signup Screen (To be implemented)
        JOptionPane.showMessageDialog(this, "Signup Screen goes here.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomePage homePage = new HomePage();
            homePage.setVisible(true);
        });
    }
}
