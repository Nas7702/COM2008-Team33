package Models;

public class BankDetails {
    private int bankDetailsID;
    private int userID;
    private String cardName;
    private String cardHolderName;
    private String cardNumber;
    private String postcode;
    private String expiryDate;
    private String securityCode;

    public BankDetails(int bankDetailsID, int userID, String cardName, String cardHolderName, String cardNumber, String postcode, String expiryDate, String securityCode) {
        this.bankDetailsID = bankDetailsID;
        this.userID = userID;
        this.cardName = cardName;
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.postcode = postcode;
        this.expiryDate = expiryDate;
        this.securityCode = securityCode;
    }

    public int getBankDetailsID() {
        return bankDetailsID;
    }

    public int getUserID() {
        return userID;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardHolderName() { return cardHolderName; }

    public String getCardNumber() { return cardNumber; }

    public String getPostcode() {
        return postcode;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setCardName(String cardName) {
        if (cardName != null && !cardName.trim().isEmpty()) {
            this.cardName = cardName;
        } else {
            throw new IllegalArgumentException("Card name cannot be null or empty.");
        }
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public void setCardNumber(String cardNumber) {
        if (cardNumber != null && cardNumber.matches("\\d{16}")) {
            this.cardNumber = cardNumber;
        } else {
            throw new IllegalArgumentException("Card number must be 16 digits.");
        }
    }

    public void setPostcode(String postcode) {
        // Add postcode validation
        this.postcode = postcode;
    }

    public void setExpiryDate(String expiryDate) {
        if (expiryDate != null && expiryDate.matches("\\d{2}/\\d{2}")) {
            this.expiryDate = expiryDate;
        } else {
            throw new IllegalArgumentException("Expiry date must be in MM/YY format.");
        }
    }

    public void setSecurityCode(String securityCode) {
        if (securityCode != null && securityCode.matches("\\d{3}")) {
            this.securityCode = securityCode;
        } else {
            throw new IllegalArgumentException("Security code must be 3 digits.");
        }
    }

    @Override
    public String toString() {
        return "BankDetails{" +
                "bankDetailsID=" + bankDetailsID +
                ", userID=" + userID +
                ", cardName='" + cardName + '\'' +
                ", cardHolderName='" + cardHolderName + '\'' +
                ", postcode='" + postcode + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", securityCode='" + securityCode + '\'' +
                '}';
    }

}
