package com.example.myHealth;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class Appointment implements Serializable {
    String date;
    String startTime;
    String endTime;
    Boolean recurring;
    String clinicName;
    String firstName;
    String lastName;
    Boolean complete;
    DocumentReference documentPath;
    // FOR CLINIC SIDE:
    String email;
    String phone;

    public Appointment() {}
    public Appointment(String date, String startTime, String endTime, Boolean recurring, String firstName, String lastName) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurring = recurring;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Appointment(String date, String startTime, String endTime, Boolean recurring) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurring = recurring;
    }
    public Appointment(String startTime, String endTime, Boolean recurring) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurring = recurring;
    }

    public Appointment(String startTime, String endTime, Boolean recurring, String clinicName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurring = recurring;
        this.clinicName = clinicName;
    }

    public Appointment(String startTime, String endTime, Boolean recurring, String clinicName, Boolean complete) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurring = recurring;
        this.clinicName = clinicName;
        this.complete = complete;
    }

    public Appointment(String clinicName, String date, String startTime, String endTime, Boolean recurring, Boolean complete) {
        this.clinicName = clinicName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurring = recurring;
        this.complete = complete;
    }

    public Boolean getRecurring() { return recurring; }

    public void setRecurring(Boolean recurring) { this.recurring = recurring; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getStartTime() { return startTime; }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }

    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public DocumentReference getDocumentPath() { return documentPath; }
    public void setDocumentPath(DocumentReference documentPath) { this.documentPath = documentPath; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
