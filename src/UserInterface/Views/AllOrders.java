package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AllOrders extends JFrame {

    private DatabaseConnectionHandler dbHandler;
    JLabel order;

    public AllOrders(DatabaseConnectionHandler dbHandler, String role) {
        this.dbHandler = dbHandler;
        createUI(role);
    }

    private void createUI(String role) {
        setTitle("All Orders");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 1));
        order = new JLabel();
        add(order);
        String[] data = { "one", "two", "three", "four" };
        final JList dataList = new JList(data);

        dataList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    order.setText(dataList.getSelectedValue().toString());
                }
            }
        });

    }

}
