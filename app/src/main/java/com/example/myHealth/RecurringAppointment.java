package com.example.myHealth;

public class RecurringAppointment {
    String day;
    String startTime;
    String endTime;
    Boolean recurring;

    public RecurringAppointment() {}
    public RecurringAppointment(String date, String startTime, String endTime, Boolean recurring) {
        this.day = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurring = recurring;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }
}
