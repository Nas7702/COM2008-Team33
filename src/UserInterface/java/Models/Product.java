package Models;

public class Product {
    private int productID;
    private String brandName;
    private String productName;
    private String productCode;
    private double retailPrice;
    private String gauge;
    private String era;
    private String dccCode;
    private int quantity;

    // Constructor, getters, and setters
    public Product(int productID, String brandName, String productName, String productCode,
                   double retailPrice, String gauge, String era, String dccCode, int quantity) {
        this.productID = productID;
        this.brandName = brandName;
        this.productName = productName;
        this.productCode = productCode;
        this.retailPrice = retailPrice;
        this.gauge = gauge;
        this.era = era;
        this.dccCode = dccCode;
        this.quantity = quantity;
    }

    // Getters and Getters


    public int getProductID() {
        return productID;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public String getGauge() {
        return gauge;
    }

    public String getEra() {
        return era;
    }

    public String getDccCode() {
        return dccCode;
    }
    public int getQuantity() {
        return quantity;
    }


    // Override toString method if needed for displaying in list
    @Override
    public String toString() {
        return productName + " - " + brandName + " - £" + retailPrice;
    }

}
