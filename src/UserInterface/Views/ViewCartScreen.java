package UserInterface.Views;

import Database.DatabaseConnectionHandler;
import Database.DatabaseOperations;
import Models.*;


import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class ViewCartScreen extends JFrame {
    private DatabaseConnectionHandler dbHandler;
    private User loggedInUser;
    private JTable orderTable;
    private JLabel totalCartPriceLabel;
    private DefaultTableModel tableModel;
    private DatabaseOperations databaseOperations;
    private Cart cart;

    public ViewCartScreen(DatabaseConnectionHandler dbHandler, User loggedInUser, Cart cart) {
        this.dbHandler = dbHandler;
        this.loggedInUser = loggedInUser;
        this.databaseOperations = new DatabaseOperations();
        this.cart = cart;
        createUI();
        loadPendingOrder();
        loadCartItems();
    }

    private void createUI() {
        setTitle("View Your Cart - Trains of Sheffield");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"Product", "Quantity", "Price per Item", "Total Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only the second column (Quantity) is editable
                return column == 1;
            }
        };
        orderTable = new JTable(tableModel);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);

        setupTableModelListener();

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> checkout());
        buttonsPanel.add(checkoutButton);

        JButton catalogButton = new JButton("Return to Product Catalogue");
        catalogButton.addActionListener(e -> viewProductCatalog());
        buttonsPanel.add(catalogButton);

        JButton deleteButton = new JButton("Delete Item");
        deleteButton.addActionListener(e -> deleteSelectedItem());
        buttonsPanel.add(deleteButton);


        totalCartPriceLabel = new JLabel("Total Cart Cost: £0.00");
        buttonsPanel.add(totalCartPriceLabel);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void loadPendingOrder() {
        try {
            dbHandler.openConnection();
            Connection connection = dbHandler.getConnection();
            databaseOperations.loadPendingOrder(loggedInUser.getUserID(), cart, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHandler.closeConnection();
        }
    }

    private void loadCartItems() {
        double totalCost = 0;
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double retailPrice = product.getRetailPrice();
            double totalPrice = retailPrice * quantity;
            totalCost += totalPrice;

            tableModel.addRow(new Object[]{product.getProductName(), quantity, retailPrice, totalPrice});

            TableColumn quantityColumn = orderTable.getColumnModel().getColumn(1);
            quantityColumn.setCellEditor(new SpinnerEditor());

        }

        totalCartPriceLabel.setText("Total Cart Price: " + String.format("%.2f", totalCost));
    }

    private void checkout() {
        List<Product> products = cart.getAllProducts();
        CheckoutScreen checkoutScreen = new CheckoutScreen(loggedInUser, dbHandler, cart);
        checkoutScreen.setVisible(true);
        this.dispose();
    }


    private void viewProductCatalog() {
        ProductCatalogScreen catalogScreen = new ProductCatalogScreen(dbHandler, loggedInUser, cart);
        catalogScreen.setVisible(true);
        dispose();
    }

    private void deleteSelectedItem() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.");
            return;
        }

        Product selectedProduct = cart.getAllProducts().get(selectedRow);
        try {
            dbHandler.openConnection();
            databaseOperations.deleteOrderItem(selectedProduct, dbHandler.getConnection());
            cart.removeItem(selectedProduct); // Assuming Cart class has this method
            ((DefaultTableModel) orderTable.getModel()).removeRow(selectedRow);
            refreshTotalCostLabel();
            JOptionPane.showMessageDialog(this, "Item deleted successfully.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting item: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            dbHandler.closeConnection();
        }
    }

    private class SpinnerEditor extends DefaultCellEditor {
        JSpinner spinner;

        public SpinnerEditor() {
            super(new JTextField());
            spinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            spinner.setValue(value);
            return spinner;
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }
    }

    private void setupTableModelListener() {
        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 1) { // Assuming quantity is in the second column
                    try {
                        dbHandler.openConnection();
                        Connection connection = dbHandler.getConnection();

                        Product product = cart.getAllProducts().get(row);
                        Integer newQuantity = (Integer) orderTable.getValueAt(row, column);

                        int orderId = databaseOperations.getPendingOrderId(loggedInUser.getUserID(), connection);
                        if (orderId != -1) {
                            databaseOperations.updateDatabaseOrderItem(orderId, product, newQuantity, connection);
                        }
                        refreshTotalCostLabel();

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                        dbHandler.closeConnection();
                    }
                }
            }
        });
    }

    private void refreshTotalCostLabel() {
        double totalCost = cart.calculateTotalCost();
        totalCartPriceLabel.setText("Total Cart Price: " + String.format("%.2f", totalCost));
    }






}
