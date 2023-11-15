package UserInterface.java.Models;

import java.util.regex.Pattern;

public class Address {
    private int addressID;
    private String houseNumber;
    private String roadName;
    private String city;
    private String postcode;
    private int userID; 

    public User(int addressID, String houseNumber, String roadName, String city, String postcode, int userID) {
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
    public string getHouseNumber() {return houseNumber;}
    public string getRoadName() {return roadName;}
    public string getPostcode() {return postcode;}
    public string city() {return city;}
    
    // Setters

    public void setAddressID(int addressID) {
        if (isNotNull(addressID)) {
            this.addressID = addressID;
        }
        else throw new IllegalArgumentException("Invalid address format");
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
