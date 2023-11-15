package UserInterface.java.Models;

public class Address {
    private int addressID;
    private String houseNumber;
    private String roadName;
    private String city;
    private String postcode;
    private int userID;

    public Address(int addressID, String houseNumber, String roadName, String city, String postcode, int userID) {
        this.addressID = addressID;
        this.houseNumber = houseNumber;
        this.roadName = roadName;
        this.city = city;
        this.postcode = postcode;
        this.userID = userID;
    }

    // Getters
    public int getUserID() { return userID; }
    public int getAddressID() {return addressID;}
    public String getHouseNumber() {return houseNumber;}
    public String getRoadName() {return roadName;}
    public String getPostcode() {return postcode;}
    public String city() {return city;}

    // Setters

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public void setHouseNumber(String houseNumber) {
        if (isNotNull(houseNumber)) {
            this.houseNumber = houseNumber;
        }
        else throw new IllegalArgumentException("Invalid address format");
    }

    public void setCity(String city) {
        if (isNotNull(city)) {
            this.city = city;
        } else {
            throw new IllegalArgumentException("Invalid city format");
        }
    }

    public void setPostcode(String postcode) {
        if (isNotNull(postcode)) {
            this.postcode = postcode;
        } else {
            throw new IllegalArgumentException("Invalid postcode format");
        }
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    private boolean isNotNull(String c) {
        return c != null;
    }

}
