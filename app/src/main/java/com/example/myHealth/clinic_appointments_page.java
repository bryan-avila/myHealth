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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class clinic_appointments_page extends AppCompatActivity {

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
        CollectionReference clinicAppointmentRef = db.collection("clinic").document(currentClinic.getUid()).collection("days").document("monday").collection("appointments");

        clinicAppointmentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Appointment> appointmentsList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    //Appointment appointment = document.toObject(PrescribedMedications.class); //get appointment information
                    //appointmentsList.add(appointment);
                }
                Log.d("TAG", "Medications size: " + appointmentsList.size()); // Check the size of the clinics list
                // Set the adapter (you'll create and set the adapter in later steps)
                //MyPrescribedMedAdapter myAdapter = new MyPrescribedMedAdapter(getApplicationContext(), appointmentsList);
                //med_recycle_view.setAdapter(myAdapter);

               /* myAdapter.setOnItemClickListener(new MyPrescribedMedAdapter().OnItemClickListener() {
                    //this sends the user to the clinic's specific appointment page
                    @Override
                    public void onItemClick(int position, PrescribedMedications pMedications) {
                        // Handle the item click here
                        Intent intent = new Intent(patient_prescribed_med_page.this, patient_appointments_view.class);
                        startActivity(intent);
                    }
                });
*/
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
                } else if (id == R.id.homeIdCLinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_home_page.class));
                    finish();
                    return true;
                } else if (id == R.id.medicalHistIdClinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_medical_records_page.class));
                    finish();
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
}