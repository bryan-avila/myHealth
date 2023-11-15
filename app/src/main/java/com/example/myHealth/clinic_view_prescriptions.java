package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class clinic_view_prescriptions extends AppCompatActivity {

    //This class might be placed when we click on a patient to prescribe medication
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_clinic_view_prescriptions);
    }

    // When clinic clicks this button, send them to search for a list of patients
    public void onFillPrescriptionClick(View view) {
        startActivity(new Intent(getApplicationContext(), clinic_prescription_form.class));
    }
}