package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class profile_page_clinic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page_clinic);

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);

        //Set profile selected
        bottomNavigationView.setSelectedItemId(R.id.profileIdClinic);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentIdClinic) {
                    startActivity(new Intent(getApplicationContext(), appointments_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.homeIdCLinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_home_page.class));
                    finish();
                    return true;
                } else if (id == R.id.medicalHistId) {
                    startActivity(new Intent(getApplicationContext(), medical_records_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.resourcesIdClinic) {
                    startActivity(new Intent(getApplicationContext(), resources_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.profileIdClinic) {
                    return true;
                }
                return false;
            }

        });
    }
}