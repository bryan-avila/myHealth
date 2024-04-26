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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class edit_appointment_clinic_page extends AppCompatActivity{
    Appointment appointment;
    String appointmentPath;
    String clinicAppointmentPath;
    String appointmentDate;
    String appointmentTime;
    DocumentReference appointmentDocument;
    DocumentReference clinicAppointmentDocument;
    // Inside your activity or fragment
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), patient_home_page.class));
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            appointmentPath = (String) intent.getSerializableExtra("appointmentPath");
            clinicAppointmentPath = (String) intent.getSerializableExtra("clinicAppointmentPath");
            appointmentDate = (String) intent.getSerializableExtra("appointmentDate");
            appointmentTime = (String) intent.getSerializableExtra("appointmentTime");
        } else {
            appointmentPath = null;
            clinicAppointmentPath = null;
            appointmentDate = null;
            appointmentTime = null;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment_clinic_page);

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
                    startActivity(new Intent(getApplicationContext(), patient_nutrition_page.class));
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
        FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        CollectionReference clinicsRef = db.collection("clinic");

        appointmentDocument = db.document(appointmentPath);
        appointmentDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Convert document snapshot data to your Java class
                    appointment = documentSnapshot.toObject(Appointment.class);
                    // Now you can use the object of your class as needed
                    // For example, you can pass it to a method or display its data in the UI
                } else {
                    // Document does not exist
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle errors
            }
        });

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
                        // change the clinic in the appointment
                        appointmentDocument = db.document(appointmentPath);
                        clinicAppointmentDocument = db.document(clinicAppointmentPath);
                        TimeConverter timeConverter = new TimeConverter();
                        AppointmentManager appointmentManager = new AppointmentManager();
                        double startTime = timeConverter.convertToDecimal(appointmentTime);
                        appointmentManager.deleteAppointment(appointment, appointmentDocument);
                        appointmentManager.makeSingleAppointment(clinic.getClinicName(), clinic.getID(), appointmentDate, startTime, false);

                        DocumentReference newAppointmentDocument = db.collection("user").document(userId).collection("dates").document(appointmentDate).collection("appointments").document(clinic.getID());
                        String newAppointmentPath = newAppointmentDocument.getPath();

                        //Intent intent = new Intent(edit_appointment_clinic_page.this, edit_appointment_page.class);
                        //intent.putExtra("appointmentPath", newAppointmentPath);
                        Intent intent = new Intent(edit_appointment_clinic_page.this, patient_home_page.class);
                        startActivity(intent);
                    }
                });

            } else {
                // Handle the error
                Log.e("TAG", "Error getting clinics", task.getException());
            }
        });

    }
}


