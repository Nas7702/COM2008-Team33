package UserInterface.Views;

import javax.swing.*;
import java.awt.*;
import Database.DatabaseConnectionHandler;
import Models.User;

public class ViewOrderScreen extends JFrame{
    private JButton catalogueButton;
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser; // Field to store the logged-in user

    public ViewOrderScreen(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser; // Store the logged-in user
        createUI();
    }
    private void createUI() {
        setTitle("Product Catalog - Trains of Sheffield");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 10 is the gap between buttons
        JButton catalogButton = new JButton("Return to product catalogue");
        catalogButton.addActionListener(e -> viewProductCatalog());
        buttonsPanel.add(catalogButton); // Add to the buttons panel
        JButton ConfirmOrderButton = new JButton("Confirm Order");
        ConfirmOrderButton.addActionListener(e -> confirmOrder());
        buttonsPanel.add(catalogButton); // Add to the buttons panel
        buttonsPanel.add(ConfirmOrderButton);
        add(buttonsPanel, BorderLayout.CENTER); // Add buttons panel to the center

    }
    private void styleButton(JButton button, Color color, Font font) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    private void viewProductCatalog() {
        ProductCatalogScreen catalogScreen = new ProductCatalogScreen(dbHandler, loggedInUser);
        catalogScreen.setVisible(true);
        this.dispose();
    }
    private void confirmOrder() {
        ProductCatalogScreen catalogScreen = new ProductCatalogScreen(dbHandler, loggedInUser);
        catalogScreen.setVisible(true);
        this.dispose();
    }
}
