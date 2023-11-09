package com.example.myHealth;

import java.io.Serializable;

public class Patient implements Serializable {

    // Get Patient Information
    String firstName;
    String lastName;
    String phone;
    String email;

    public Patient() {
    }

    public Patient(String fName, String lName, String phoneNum, String emailInfo) {
        this.firstName = fName;
        this.lastName = lName;
        this.email = emailInfo;
        this.phone = phoneNum;
    }

    public String getfirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        this.firstName = fName;
    }

    public String getlastName() {
        return lastName;
    }

    public void setLastName(String lName) {
        this.lastName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}