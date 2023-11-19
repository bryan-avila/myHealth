package com.example.myHealth;

import static android.content.ContentValues.TAG;

import static com.example.myHealth.TimeConverter.convertToDecimal;
import static com.example.myHealth.TimeConverter.convertToString;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class pick_appointment_time extends AppCompatActivity {
    // Inside your activity or fragment


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_appointment_time);

        Clinic clinic;
        String selectedDate;
        // ArrayList<Boolean> availabilityList;
        ArrayList<String> availableTimesList;

        // Retrieve the data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            clinic = (Clinic) intent.getSerializableExtra("clinicData");
            selectedDate = (String) intent.getStringExtra("selectedDate");
            // availabilityList = (ArrayList<Boolean>) intent.getStringArrayListExtra("availabilityList");
            availableTimesList = (ArrayList<String>) intent.getStringArrayListExtra("availableTimesList");
        } else {
            clinic = null;
            selectedDate = null;
            // availabilityList = null;
            availableTimesList = null;
        }
        // Log the size of the ArrayList
        Log.d("ArrayListInfo", "Size of times recycler view: " + availableTimesList.size());
        // Log all elements in the ArrayList
        if (availableTimesList.size() > 0) {
            for (int i = 0; i < availableTimesList.size(); i++) {
                Log.d("ArrayListInfo", "Time " + i + ": " + availableTimesList.get(i));
            }
        } else {
            Log.d("ArrayListInfo", "timeslist is Empty");
        }

        //list of items
        FirebaseFirestore db = myFirestore.getDBInstance();
        FirebaseAuth mAuth = myFirestore.getmAuthInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference appointmentsRef = db.collection("clinic").document(clinic.getID()).collection("dates").document(selectedDate).collection("appointments");


        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TimeAdapter timeAdapter = new TimeAdapter(availableTimesList);
        timeAdapter.setOnItemClickListener(new TimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String time) {
                // Handle the click event with the selected time
                Log.d("SelectedTime", "Time " + ": " + time);
                String startTime = time;
                String date = selectedDate;
                Log.d("selectedDate", "selectedDate " + ": " + selectedDate);

                Double endTimeDecimal = convertToDecimal(time) + 4;
                if (endTimeDecimal > 12.5) {
                    endTimeDecimal -= 12;
                }
                String endTime = convertToString(endTimeDecimal);
                Log.d("endTime", "endTime " + ": " + endTime);

                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    // Create a new appointment document with the user's ID as the document ID
                    DocumentReference newAppointmentRef = appointmentsRef.document(UUID.randomUUID().toString());

                    // Create a Map to store the data
                    Map<String, Object> appointmentData = new HashMap<>();
                    appointmentData.put("startTime", startTime);
                    appointmentData.put("endTime", endTime);
                    appointmentData.put("date", date);

                    // Set the data in the document
                    newAppointmentRef.set(appointmentData)
                            .addOnSuccessListener(aVoid -> {
                                // Document successfully written
                                Log.d("Firestore", "Appointment document added successfully!");
                            })
                            .addOnFailureListener(e -> {
                                // Handle errors
                                Log.e("Firestore", "Error adding appointment document", e);
                            });
                }

            }
        });
        recyclerView.setAdapter(timeAdapter);



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
                    startActivity(new Intent(getApplicationContext(), pick_appointment_time.class));
                    finish();
                    return true;
                } else if (id == R.id.homeId) {
                    startActivity(new Intent(getApplicationContext(), home_page.class)); //check this line it might be wrong
                    finish();
                    return true;
                } else if (id == R.id.medicalHistId) {
                    startActivity(new Intent(getApplicationContext(), medical_records_page.class)); //check this line it might be wrong
                    finish();
                    return true;
                } else if (id == R.id.resourcesId) {
                    startActivity(new Intent(getApplicationContext(), patient_diet_page.class));
                    finish();
                    return true;
                } else if (id == R.id.profileId) {
                    startActivity(new Intent(getApplicationContext(), profile_page.class));
                    finish();
                    return true;
                }
                return false;
            }
        });


    }

    public void onClinic1Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), appointments_page.class));
    }

    public void onClinic2Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), appointments_page.class));
    }

    public void onClinic3Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), appointments_page.class));
    }

    public void onClinic4Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), appointments_page.class));
    }

    public void onClinic5Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), appointments_page.class));
    }

}


