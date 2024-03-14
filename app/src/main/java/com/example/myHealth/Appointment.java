package com.example.myHealth;

import java.io.Serializable;

public class Appointment implements Serializable {
    String date;
    String startTime;
    String endTime;
    Boolean recurring;
    String clinicName;
    Boolean complete;

    public Appointment() {}
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
}
