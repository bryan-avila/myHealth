package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class profile_page_clinic extends AppCompatActivity {

    TextView clinicEmailPlaceholder, clinicNamePlaceholder, clinicLocationPlaceholder, clinicPhonePlaceholder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = myFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference uRef = db.collection("users").document(currentUser.getUid());
    private ListenerRegistration userL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page_clinic);

        clinicEmailPlaceholder = findViewById(R.id.clinicEmailPlaceholder);
        clinicNamePlaceholder = findViewById(R.id.clinicNamePlaceholder);
        clinicLocationPlaceholder = findViewById(R.id.clinicLocationPlaceholder);
        clinicPhonePlaceholder = findViewById(R.id.clinicPhonePlaceholder);

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
                } else if (id == R.id.medicalHistIdClinic) {
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

        //Log out button here

        //Retrieve information from database to display
    }
}