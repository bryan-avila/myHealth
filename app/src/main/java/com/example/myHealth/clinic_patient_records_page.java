package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class clinic_patient_records_page extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SearchView filterView;
    MyPatientAdapter myPatAdapater;
    private List<Patient> patientsList = null;
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), clinic_home_page.class));
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_view_patients_medical_records);

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_patients_for_med_records);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get all user information from firebase
        CollectionReference patients = db.collection("users");

        patients.get().addOnCompleteListener(task -> {

            // Created a list of all patients (above)

            if (task.isSuccessful()) {
                //initialized list of patients here
                patientsList = new ArrayList<>();

                // For loop populating the recycler view
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Patient p = document.toObject(Patient.class);
                    String patient_id = document.getId(); // Get patient userID
                    p.setPat_ID(patient_id); // Set patient id
                    patientsList.add(p);
                }
            }

            myPatAdapater = new MyPatientAdapter(getApplicationContext(), patientsList);
            recyclerView.setAdapter(myPatAdapater);

            // Make Patient's Clickable
            myPatAdapater.setOnItemClickListener(new MyPatientAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, Patient patients) {

                    String p_id = patients.getPat_ID().toString();
                    // Send them to the medical history page after clicking on a patients name using this onItemClickListener
                    Intent intent = new Intent(clinic_patient_records_page.this, clinic_view_chosen_patient_medical_history.class);
                    // Send extra info to know where to send the medication information
                    intent.putExtra("patient",p_id);
                    startActivity(intent);
                }
            });

        });


        // Get searchView from activity
        filterView = findViewById(R.id.search_view_find_patient_to_view_records);
        filterView.clearFocus();
        filterView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myPatAdapater.getFilter().filter(newText);

                // Make the filtered list clickable
                myPatAdapater.setOnItemClickListener(new MyPatientAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, Patient patients) {

                        String p_id = patients.getPat_ID().toString();
                        // Send them to patient_medical_history_page.java
                        Intent intent = new Intent(clinic_patient_records_page.this, clinic_view_chosen_patient_medical_history.class);
                        // Send extra info to know where to send the medication information
                        intent.putExtra("patient",p_id);
                        startActivity(intent);
                    }
                });
                return true;
            }
        });

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);

        //Set resources selected
        bottomNavigationView.setSelectedItemId(R.id.patientRecordsIdClinic);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentIdClinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_appointments_page.class));
                    finish();
                    return true;
                } else if (id == R.id.homeIdClinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_home_page.class));
                    finish();
                    return true;
                } else if (id == R.id.medicationsIdClinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_medications_page.class));
                    finish();
                    return true;
                } else if (id == R.id.patientRecordsIdClinic) {
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
}