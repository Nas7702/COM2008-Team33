package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void removeItem(Product product) {
        items.remove(product);
    }


    public Map<Product, Integer> getItems() {
        return items;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(items.keySet());
    }


    // calculate the total cost
    public double calculateTotalCost() {
        double totalCost = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            totalCost += product.getRetailPrice() * quantity;
        }
        return totalCost;
    }

    // create an Orders object
    public Orders createPendingOrder(int userID) {
        double totalCost = calculateTotalCost();
        String currentDate = String.valueOf(LocalDate.now());
        String status = "Pending";
        return new Orders(0, userID, currentDate, status, totalCost);
    }

    public void updateItemQuantity(Product product, int newQuantity) {
        if (items.containsKey(product)) {
            items.put(product, newQuantity);
        }
    }


    public void clearCart() {
        items.clear();
    }
}
