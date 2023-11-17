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

    // Constructor, getters, and setters
    public Product(int productID, String brandName, String productName, String productCode,
                   double retailPrice, String gauge, String era, String dccCode) {
        this.productID = productID;
        this.brandName = brandName;
        this.productName = productName;
        this.productCode = productCode;
        this.retailPrice = retailPrice;
        this.gauge = gauge;
        this.era = era;
        this.dccCode = dccCode;
    }

    // Getters and Setters

    // Override toString method if needed for displaying in list
    @Override
    public String toString() {
        return productName + " - " + brandName + " - £" + retailPrice;
    }
}
