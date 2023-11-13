package com.sheffield;

import java.util.regex.Pattern;

public class User {
    private int userID;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private Boolean isStaff;
    private Boolean isManager;

    // Constructor to initialize a User object with its attributes
    public User(int userID, String email, String password, String firstName, String lastName, String address, Boolean isStaff, Boolean isManager) {
        this.userID = userID;
        this.setEmail(email);
        this.setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.isStaff = isStaff;
        this.isManager = isManager;
    }

    // Getter and setter methods for the userID
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID; // Assuming there's no complex validation for userID
    }

    // Getter and setter methods for the email with validation
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email is not valid.");
        }
    }

    // Getter and setter methods for the password with validation
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (isValidPassword(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password is not valid.");
        }
    }

    // Getter and setter methods for firstName
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName; // Add validation if needed
    }

    // Getter and setter methods for lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName; // Add validation if needed
    }

    // Getter and setter methods for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address; // Add validation if needed
    }

    // Getter and setter methods for isStaff
    public Boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(Boolean isStaff) {
        this.isStaff = isStaff; // Add validation if needed
    }

    // Getter and setter methods for isManager
    public Boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(Boolean isManager) {
        this.isManager = isManager; // Add validation if needed
    }

    // Private validation methods for each attribute
    private boolean isValidEmail(String email) {
        // Simple example of email validation
        return email != null && Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email);
    }

    private boolean isValidPassword(String password) {
        // Example of password validation
        // Note: This is a very basic example. Password validation logic should be more comprehensive.
        return password != null && password.length() >= 8;
    }

    // Add isValid methods for other attributes as needed
    // ...

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", isStaff=" + isStaff +
                ", isManager=" + isManager +
                '}';
    }
}
