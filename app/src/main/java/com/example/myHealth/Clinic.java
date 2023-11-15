package com.example.myHealth;
import java.io.Serializable;


public class Clinic implements Serializable{
    String clinicName;
    String ID;
    String email;
    String location;
    String phone;
    int startHour;
    int finishHour;
    int machines;


    public Clinic() {
    }

    public Clinic(String clinicName, String email, String location, String phone, int startHour, int finishHour, int machines) {
        this.clinicName = clinicName;
        this.email = email;
        this.location = location;
        this.phone = phone;
        this.startHour = startHour;
        this.finishHour = finishHour;
        this.machines = machines;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getID() { return ID; }

    public void setID(String ID) { this.ID = ID; }

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

    public int getStartHour() { return startHour; }

    public void setStartHour(int startHour) { this.startHour = startHour; }

    public int getFinishHour() { return finishHour; }

    public void setFinishHour(int finishHour) { this.finishHour = finishHour; }

    public int getMachines() { return machines; }

    public void setMachines(int machines) { this.machines = machines; }
}
