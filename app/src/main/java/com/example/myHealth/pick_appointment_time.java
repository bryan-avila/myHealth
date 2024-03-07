package com.example.myHealth;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class pick_appointment_time extends AppCompatActivity {
    // Inside your activity or fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_pick_appointment_time);

        Clinic clinic;
        String selectedDate;
        ArrayList<String> availableTimesList;

        // Retrieve the data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            clinic = (Clinic) intent.getSerializableExtra("clinicData");
            selectedDate = (String) intent.getStringExtra("selectedDate");
            availableTimesList = (ArrayList<String>) intent.getStringArrayListExtra("availableTimesList");

        } else {
            clinic = null;
            selectedDate = null;
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

        TextView ClinicName = findViewById(R.id.ClinicName);
        //set the clinic name to clinic name
        ClinicName.setText(clinic.getClinicName());

        //list of items
        FirebaseFirestore db = MyFirestore.getDBInstance();
        FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference appointmentsRef = db.collection("clinic").document(clinic.getID()).collection("dates").document(selectedDate).collection("appointments");


        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TimeAdapter timeAdapter = new TimeAdapter(availableTimesList);
        AppointmentManager appointmentmanager = new AppointmentManager();
        TimeConverter timeconverter = new TimeConverter();

        timeAdapter.setOnItemClickListener(new TimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String time) {
                // Handle the click event with the selected time
                Log.d("SelectedTime", "Time " + ": " + time);
                String startTime = time;
                String date = selectedDate;
                Log.d("selectedDate", "selectedDate " + ": " + selectedDate);

                double newtime = timeconverter.convertToDecimal(startTime);

                appointmentmanager.makeSingleAppointment(clinic.getID(), date, newtime, false);
                Toast.makeText(pick_appointment_time.this, "appointment made for: " + time + " on: " + selectedDate, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(pick_appointment_time.this, patient_home_page.class);
                startActivity(intent);

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


