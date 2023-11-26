package Models;


public class Orders {
    private int orderID;
    private int userID;
    private String date;
    private String status;
    private Double totalCost;

    public Orders(int orderID, int userID, String date, String status, Double totalCost) {
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

    public Double getTotalCost() {
        return totalCost;
    }

}
