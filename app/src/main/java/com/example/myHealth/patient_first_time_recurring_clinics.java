package com.example.myHealth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class patient_first_time_recurring_clinics extends AppCompatActivity {
    public void onSkipRecurringButtonClick1(View view)
    {
        // Once they finish inputting diet, send them home
        Intent intent = new Intent(patient_first_time_recurring_clinics.this, patient_home_page.class);
        startActivity(intent);
        finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_first_time_recurring_clinics_page);

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set medical hist selected
        bottomNavigationView.setSelectedItemId(R.id.appointmentId);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentId) {
                    startActivity(new Intent(getApplicationContext(), patient_search_centers_visit_page.class));
                    finish();
                    return true;
                } else if (id == R.id.homeId) {
                    startActivity(new Intent(getApplicationContext(), patient_home_page.class)); //check this line it might be wrong
                    finish();
                    return true;
                } else if (id == R.id.medicalHistId) {
                    startActivity(new Intent(getApplicationContext(), patient_medical_records_page.class)); //check this line it might be wrong
                    finish();
                    return true;
                } else if (id == R.id.resourcesId) {
                    startActivity(new Intent(getApplicationContext(), patient_diet_page.class));
                    finish();
                    return true;
                } else if (id == R.id.profileId) {
                    startActivity(new Intent(getApplicationContext(), patient_profile_page.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //list of items
        FirebaseFirestore db = MyFirestore.getDBInstance();
        CollectionReference clinicsRef = db.collection("clinic");

        clinicsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Clinic> clinics = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Clinic clinic = document.toObject(Clinic.class);
                    String ID = document.getId();
                    clinic.setID(ID);
                    clinics.add(clinic);
                }
                Log.d("TAG", "Clinics size: " + clinics.size()); // Check the size of the clinics list
                // Set the adapter (you'll create and set the adapter in later steps)
                MyClinicAdapter myAdapter = new MyClinicAdapter(getApplicationContext(), clinics);
                recyclerView.setAdapter(myAdapter);

                myAdapter.setOnItemClickListener(new MyClinicAdapter.OnItemClickListener() {
                    //this sends the user to the clinic's specific appointment page
                    @Override
                    public void onItemClick(int position, Clinic clinic) {
                        // Handle the item click here
                        Intent intent = new Intent(patient_first_time_recurring_clinics.this, patient_first_time_recurring_appointments_page.class);
                        intent.putExtra("clinicData", clinic);
                        startActivity(intent);
                    }
                });

            } else {
                // Handle the error
                Log.e("TAG", "Error getting clinics", task.getException());
            }
        });

    }

    public void onClinic1Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), patient_appointments_view.class));
    }

    public void onClinic2Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), patient_appointments_view.class));
    }

    public void onClinic3Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), patient_appointments_view.class));
    }

    public void onClinic4Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), patient_appointments_view.class));
    }

    public void onClinic5Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), patient_appointments_view.class));
    }
}
