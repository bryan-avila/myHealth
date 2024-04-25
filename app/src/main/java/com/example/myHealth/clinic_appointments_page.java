package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class clinic_appointments_page extends AppCompatActivity {
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), clinic_home_page.class));
        finish();

    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    //Need to know who is the current clinic
    FirebaseUser currentClinic = mAuth.getCurrentUser();

    private DocumentReference userRef = db.collection("clinic").document(currentClinic.getUid());

    //private List<Appointment> appointmentsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_page_clinic);


        // Set up the RecyclerView
        RecyclerView recyclerViewClinicAppointments = findViewById(R.id.recycler_view_upcoming_appointments_clinic);
        recyclerViewClinicAppointments.setLayoutManager(new LinearLayoutManager(this));

        // Get correct database path for appointments
        CollectionReference clinicAppointmentDateRef = db.collection("clinic").document(currentClinic.getUid()).collection("dates");
        Log.d("TAG", "Collection Reference: " + clinicAppointmentDateRef.getPath());
        clinicAppointmentDateRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Appointment> appointments = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    CollectionReference appointments_collection = clinicAppointmentDateRef.document(document.getId()).collection("appointments");
                    appointments_collection.get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                DocumentReference userDocument = db.collection("users").document(document2.getId());
                                userDocument.get().addOnCompleteListener(task3 -> {
                                    if (task3.isSuccessful()) {
                                        DocumentReference apptDocument = appointments_collection.document(document2.getId());
                                        apptDocument.get().addOnCompleteListener(task4 -> {
                                            if (task4.isSuccessful()) {
                                                DocumentSnapshot userDocumentFields = task3.getResult();
                                                DocumentSnapshot apptDocumentFields = task4.getResult();
                                                Log.d("TAG", "Document data: " + userDocumentFields.getData());
                                                Appointment clinicAppointment = userDocumentFields.toObject(Appointment.class);
                                                Log.d("TAG", "FINAL: " + clinicAppointment);
                                                clinicAppointment.setDate(apptDocumentFields.getString("date"));
                                                clinicAppointment.setStartTime(apptDocumentFields.getString("startTime"));
                                                clinicAppointment.setEndTime(apptDocumentFields.getString("endTime"));
                                                appointments.add(clinicAppointment);
                                            }
                                            Log.d("TAG", "AMOUNT OF APPOINTMENTS FOUND: " + appointments.size());
                                            MyClinicAppointmentsAdapter myAdapter = new MyClinicAppointmentsAdapter(getApplicationContext(), appointments, recyclerViewClinicAppointments);
                                            recyclerViewClinicAppointments.setAdapter(myAdapter);
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            }
            else {
                // Handle the error
                Log.e("TAG", "Error getting medications", task.getException());
            }
        });




        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);

        //Set appointment selected
        bottomNavigationView.setSelectedItemId(R.id.appointmentIdClinic);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentIdClinic) {
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
}