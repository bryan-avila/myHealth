package com.example.myHealth;

import java.io.Serializable;

public class Appointment implements Serializable {
    String date;
    String startTime;
    String endTime;
    Boolean recurring;
    String clinic;

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

    public Appointment(String startTime, String endTime, Boolean recurring, String clinic) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurring = recurring;
        this.clinic = clinic;
    }

    public Boolean getRecurring() { return recurring; }

    public void setRecurring(Boolean recurring) { this.recurring = recurring; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getStartTime() { return startTime; }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }

    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }
}
