package com.example.myHealth;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import java.util.List;

public class pick_appointment_time extends AppCompatActivity {
    // Inside your activity or fragment


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_appointment_time);

        Clinic clinic;
        String selectedDate;
        // ArrayList<Boolean> availabilityList;
        ArrayList<String> timesList;

        // Retrieve the data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            clinic = (Clinic) intent.getSerializableExtra("clinicData");
            selectedDate = (String) intent.getStringExtra("selectedDate");
            // availabilityList = (ArrayList<Boolean>) intent.getStringArrayListExtra("availabilityList");
            timesList = (ArrayList<String>) intent.getStringArrayListExtra("timesList");
        } else {
            clinic = null;
            selectedDate = null;
            // availabilityList = null;
            timesList = null;
        }
        // Log the size of the ArrayList
        Log.d("ArrayListInfo", "Size of times recycler view: " + timesList.size());
        // Log all elements in the ArrayList
        if (timesList.size() > 0) {
            for (int i = 0; i < timesList.size(); i++) {
                Log.d("ArrayListInfo", "Time " + i + ": " + timesList.get(i));
            }
        } else {
            Log.d("ArrayListInfo", "timeslist is Empty");
        }

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TimeAdapter timeAdapter = new TimeAdapter(timesList);
        recyclerView.setAdapter(timeAdapter);

        //list of items
        FirebaseFirestore db = myFirestore.getDBInstance();
        FirebaseAuth mAuth = myFirestore.getmAuthInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference clinicRef = db.collection("clinic").document(clinic.getID()).collection("appointments");





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


