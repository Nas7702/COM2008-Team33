package Models;

import java.util.regex.Pattern;

public class User {
    // Attributes
    private String email;
    private String password;
    private String forename;
    private String surname;
    private userRole role; // Role enum: CUSTOMER, STAFF, MANAGER

    // Constructor
    public User(String email, String password, String forename, String surname, userRole role) {
        this.setEmail(email);
        this.setPassword(password);
        this.forename = forename;
        this.surname = surname;
        this.role = role;
    }

    public enum userRole {
        CUSTOMER, STAFF, MANAGER
    }

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (isValidPassword(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Invalid password format.");
        }
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public userRole getRole() {
        return role;
    }

    public void setRole(userRole role) {
        this.role = role;
    }

    // Email validation
    private boolean isValidEmail(String email) {
        return email != null && Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email);
    }

    // Password validation
    private boolean isValidPassword(String password) {
//        return password != null && password.length() >= 8;
        return true;
    }

    // Override toString()
    @Override
    public String toString() {
        return "User{" +
                ", email='" + email + '\'' +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                ", role=" + role +
                '}';
    }
}