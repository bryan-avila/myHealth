package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class clinic_view_prescriptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_view_prescriptions);
    }

    public void onFillPrescriptionClick(View view) {
        startActivity(new Intent(getApplicationContext(), clinic_prescription_form.class));
    }
}