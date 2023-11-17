package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class medical_records_page_clinic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_records_page_clinic);

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);

        //Set medical hist selected
        bottomNavigationView.setSelectedItemId(R.id.medicalHistIdClinic);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentIdClinic) {
                    startActivity(new Intent(getApplicationContext(),
                            appointments_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.homeIdCLinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_home_page.class));
                    finish();
                    return true;
                } else if (id == R.id.medicalHistIdClinic) {
                    return true;
                } else if (id == R.id.resourcesIdClinic) {
                    startActivity(new Intent(getApplicationContext(), resources_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.profileIdClinic) {
                    startActivity(new Intent(getApplicationContext(), profile_page_clinic.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }


    public void onMyPatientRecordsClick(View view) {
        //create a new activity that lists patients
        //Allow to click on a patient and display user's medical info
        //startActivity(new Intent(getApplicationContext(), clinic_view_med_hist.class));
    }

    public void onMedicationsPrescriptionsClick(View view) {
        //will need to show current patients to prescribe meds
        startActivity(new Intent(getApplicationContext(), clinic_view_med_hist.class));
    }

    public void onManageMedicationsClick(View view) {
        startActivity(new Intent(getApplicationContext(), clinic_view_patients_manage_meds.class));
    }

}