package UserInterface.java.Models;


public class Orders {
    private int orderID;
    private int userID;
    private String date;
    private String status;
    private Float totalCost;

    public Orders(int orderID, int userID, String date, String status, Float totalCost) {
        this.orderID = orderID;
        this.userID = userID;
        this.date = date;
        this.status = status;
        this.totalCost = totalCost;
    }

    // Getters
    public int getOrderID() {
        return orderID;
    }

    public int getUserID() {
        return userID;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public Float getTotalCost() {
        return totalCost;
    }

}
