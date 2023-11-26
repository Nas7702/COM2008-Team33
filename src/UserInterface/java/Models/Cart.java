package Models;

import java.util.HashMap;
import java.util.Map;
import java.time.*;

public class Cart {
    private Map<Product, Integer> items;

    public Cart() {
        items = new HashMap<>();
    }

    public void addItem(Product product, int quantity) {
        items.put(product, items.getOrDefault(product, 0) + quantity);
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    // Method to calculate the total cost of items in the cart
    public double calculateTotalCost() {
        double totalCost = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            totalCost += product.getRetailPrice() * quantity;
        }
        return totalCost;
    }

    // Method to create an Orders object (a pending order)
    public Orders createPendingOrder(int userID) {
        double totalCost = calculateTotalCost();
        // Assuming the date and status need to be set. Modify as per your requirements.
        String currentDate = String.valueOf(LocalDate.now());
        String status = "Pending";

        Orders pendingOrder = new Orders(0, userID, currentDate, status, totalCost);
        // Here you would save the pending order to the database or handle it as required

        return pendingOrder;
    }

    public void clearCart() {
        items.clear();
    }
}
