package com.example.myHealth;

import java.io.Serializable;

public class PrescribedMedications implements Serializable {

    // Get Prescribed Medication info
    String dosageAmount;

    String frequency;

    String medicationName;

    String clinicName;

    String clinicPhone;

    public PrescribedMedications() {
    }

    public PrescribedMedications(String mName, String freq, String dosAmt, String clinicName, String clinicPhone) {
        this.medicationName = mName;
        this.frequency = freq;
        this.dosageAmount = dosAmt;
        this.clinicName = clinicName;
        this.clinicPhone = clinicPhone;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String mName) {
        this.medicationName = mName;
    }

    public String getDosageAmount() {
        return dosageAmount;
    }

    public void setDosageAmount(String dosAmt) {
        this.dosageAmount = dosAmt;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String freq) {
        this.frequency = freq;
    }

    public String getClinicName() {return clinicName;}

    public void setClinicName(String cName) {this.clinicName = cName;}

    public String getClinicPhone() {return clinicPhone;}

    public void setClinicPhone(String cPhone) {this.clinicPhone = cPhone;}

}
