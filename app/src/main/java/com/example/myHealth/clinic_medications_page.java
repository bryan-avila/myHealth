package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class clinic_medications_page extends AppCompatActivity {
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), clinic_home_page.class));
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_medical_records_page);

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);

        //Set medical hist selected
        bottomNavigationView.setSelectedItemId(R.id.medicationsIdClinic);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentIdClinic) {
                    startActivity(new Intent(getApplicationContext(),
                            clinic_appointments_page.class));
                    finish();
                    return true;
                } else if (id == R.id.homeIdClinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_home_page.class));
                    finish();
                    return true;
                } else if (id == R.id.medicationsIdClinic) {
                    return true;
                } else if (id == R.id.patientRecordsIdClinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_patient_records_page.class));
                    finish();
                    return true;
                } else if (id == R.id.profileIdClinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_profile_page.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    public void onMedicationsPrescriptionsClick(View view) {
        // Send to Clinic's View of Patient Names to select and prescribe meds to
        startActivity(new Intent(getApplicationContext(), clinic_view_med_prescription.class));
    }

    public void onManageMedicationsClick(View view) {
        // Send to them to a page that has a RecyclerView 
        startActivity(new Intent(getApplicationContext(), clinic_view_select_patient_to_manage_meds.class));
    }

}