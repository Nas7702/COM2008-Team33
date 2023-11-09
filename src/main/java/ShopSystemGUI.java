import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// ... (Other imports)

public class ShopSystemGUI {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel; // This will hold the different "screens" of the application

    // Components for the login screen
    private JTextField loginEmailTextField;
    private JPasswordField loginPasswordField;

    // Components for the registration screen
    private JTextField registerNameTextField;
    private JTextField registerEmailTextField;
    private JPasswordField registerPasswordField;
    private JButton registerSubmitButton;

    // ... (Fields for other components)

    public ShopSystemGUI() {
        initialize();
    }

    private void initialize() {
        // Initialize the main frame
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // CardLayout to switch between different panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Add login panel to the card panel
        cardPanel.add(createLoginPanel(), "Login");

        // Add registration panel to the card panel
        cardPanel.add(createRegistrationPanel(), "Register");

        // Add other panels here...

        frame.getContentPane().add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Email field for login
        loginEmailTextField = new JTextField(20);
        loginPanel.add(createLabel("Email:"), constraints);
        loginPanel.add(loginEmailTextField, constraints);

        // Password field for login
        loginPasswordField = new JPasswordField(20);
        loginPanel.add(createLabel("Password:"), constraints);
        loginPanel.add(loginPasswordField, constraints);

        // Login button with action listener to handle login
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle login logic here
            }
        });
        loginPanel.add(loginButton, constraints);

        // Button to switch to the registration panel
        JButton switchToRegisterButton = new JButton("Register New Account");
        switchToRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Register");
            }
        });
        loginPanel.add(switchToRegisterButton, constraints);

        return loginPanel;
    }

    private JPanel createRegistrationPanel() {
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Name field for registration
        registerNameTextField = new JTextField(20);
        registerPanel.add(createLabel("Name:"), constraints);
        registerPanel.add(registerNameTextField, constraints);

        // Email field for registration
        registerEmailTextField = new JTextField(20);
        registerPanel.add(createLabel("Email:"), constraints);
        registerPanel.add(registerEmailTextField, constraints);

        // Password field for registration
        registerPasswordField = new JPasswordField(20);
        registerPanel.add(createLabel("Password:"), constraints);
        registerPanel.add(registerPasswordField, constraints);

        // Submit button for registration
        registerSubmitButton = new JButton("Register");
        registerSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle registration logic here
            }
        });
        registerPanel.add(registerSubmitButton, constraints);

        // Button to switch back to the login panel
        JButton switchToLoginButton = new JButton("Back to Login");
        switchToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Login");
            }
        });
        registerPanel.add(switchToLoginButton, constraints);

        return registerPanel;
    }

    // Utility method to create labels
    private JLabel createLabel(String text) {
        return new JLabel(text);
    }

    // Method to show the GUI
    public void showGUI() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    ShopSystemGUI application = new ShopSystemGUI();
                    application.showGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
