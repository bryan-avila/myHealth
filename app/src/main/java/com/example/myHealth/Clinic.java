package com.example.myHealth;
import java.io.Serializable;


public class Clinic implements Serializable{
    String clinicName;
    String email;
    String location;
    String phone;

    public Clinic() {
    }

    public Clinic(String clinicName, String email, String location, String phone) {
        this.clinicName = clinicName;
        this.email = email;
        this.location = location;
        this.phone = phone;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
