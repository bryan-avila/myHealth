package com.example.myHealth;

import java.io.Serializable;

public class PrescribedMedications implements Serializable {

    // Get Prescribed Medication info
    String dosageAmount;

    String frequency;

    String medicationName;

    public PrescribedMedications() {
    }

    public PrescribedMedications(String mName, String freq, String dosAmt) {
        this.medicationName = mName;
        this.frequency = freq;
        this.dosageAmount = dosAmt;
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

}
