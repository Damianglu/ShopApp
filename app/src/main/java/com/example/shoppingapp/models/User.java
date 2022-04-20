package com.example.shoppingapp.models;

public class User {
    public String fullName, emailAddress, shippingAddress, phoneNumber, registerPassword,creditNumber;

    public User(String fullName, String emailAddress, String shippingAddress, String phoneNumber, String registerPassword, String creditNumber) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
        this.registerPassword = registerPassword;
        this.creditNumber = creditNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegisterPassword() {
        return registerPassword;
    }

    public void setRegisterPassword(String registerPassword) {
        this.registerPassword = registerPassword;
    }

    public String getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(String creditNumber) {
        this.creditNumber = creditNumber;
    }
}
