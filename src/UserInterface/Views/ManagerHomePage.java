package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

//The manager view displays a screen showing existing staff (displaying email, forename, surname)
//with options to remove a staff member, and a box to enter the email of a non-staff user (with only
//customer privileges) to promote to the staff role.
public class ManagerHomePage extends JFrame {
    private JButton pendingOrdersButton;
    private DatabaseConnectionHandler dbHandler;
    private JLabel user;
    private User loggedInUser;
    private JButton demoteStaff;

    public ManagerHomePage(DatabaseConnectionHandler dbHandler, User loggedInUser) {
        this.dbHandler = dbHandler;
        createUI(loggedInUser);
    }

    private void createUI(User loggedInUser) {
        setTitle("Manager Home");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 1));
        user = new JLabel();
        add(user);
        demoteStaff= new JButton();
        add(demoteStaff);
        String[][] users = { {"Ben","user"},{ "Usmaan","staff"},{ "Nas","manager"},{ "Oliver","manager"} };
        String [] header={"name", "role"};
        JTable table = new JTable(users, header);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.orange);
        table.setRowSelectionAllowed(true);
        add(new JScrollPane(table));
        /*
        table.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    user.setText(table.getSelectedValue().toString());
                }
            }
        });
        demoteStaff.addActionListener(e -> ));//demote staff code());
        */

    }

    private void openPendingOrdersScreen() {
        PendingOrders pendingOrdersScreen = new PendingOrders(dbHandler, loggedInUser);
        pendingOrdersScreen.setVisible(true);
        this.dispose(); // Close the HomePage
    }


}



