package UserInterface.Views;

import javax.swing.*;
import java.awt.*;

public class CheckoutDialog extends JDialog {
    private JTextField nameField;
    private JTextField addressField;
    private JTextField contactField;
    // Add more fields for payment details as needed

    public CheckoutDialog(Frame parent) {
        super(parent, "Checkout", true);
        createUI();
    }

    private void createUI() {
        setLayout(new GridLayout(0, 2, 10, 10));
        setSize(300, 200);

        add(new JLabel("Name:"));
        nameField = new JTextField(20);
        add(nameField);

        add(new JLabel("Address:"));
        addressField = new JTextField(20);
        add(addressField);

        add(new JLabel("Contact Number:"));
        contactField = new JTextField(20);
        add(contactField);

        // Add more fields for payment details as needed

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> processCheckout());
        add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        pack();
        // Use the parent frame passed to the constructor
        setLocationRelativeTo(getOwner());
    }


    private void processCheckout() {
        // Validate and process checkout details
        // Close dialog after processing
        dispose();
    }

    // Validation and getters for entered data
}
