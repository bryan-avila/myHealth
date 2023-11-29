package com.example.myHealth;

import java.io.Serializable;

public class UpcomingAppointments implements Serializable {

    // Get Appointment info
    String ClinicName;

    String StartTime;

    String Recurring;

    String EndTime;

    public UpcomingAppointments() {
    }

    public UpcomingAppointments(String ClinicName, String StartTime, String Recurring, String EndTime) {
        this.ClinicName = ClinicName;
        this.StartTime = StartTime;
        this.Recurring = Recurring;
        this.EndTime = EndTime;
    }

    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String ClinicName) {
        this.ClinicName = ClinicName;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getRecurring() {
        return Recurring;
    }

    public void setRecurring(String Recurring) {
        this.Recurring = Recurring;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

}
