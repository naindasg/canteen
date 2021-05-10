package com.example.canteen;

public class Customer {
    private String universityInitials;
    private String email;
    private String userId;
    private String firstName;
    private String lastName;
    private String type;
    private String customerName;
    private String customerEmail;

    public String getCustomerName() {
        return customerName;
    }

    public void  setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUniversityInitials() {
        return universityInitials;
    }

    public void setUniversityInitials(String universityInitials) {
        this.universityInitials = universityInitials;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
