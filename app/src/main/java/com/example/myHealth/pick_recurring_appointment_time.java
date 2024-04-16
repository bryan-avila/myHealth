package com.example.myHealth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class pick_recurring_appointment_time extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_pick_recurring_appointment_time);

        TextView clinicNameTextView = findViewById(R.id.ClinicName);
        TextView dayOfWeekTextView = findViewById(R.id.DayOfWeek);

        Clinic clinic;
        String selectedDay;
        ArrayList<String> availableTimesList = new ArrayList<>();

        // Retrieve the data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            clinic = (Clinic) intent.getSerializableExtra("clinicData");
            selectedDay = (String) intent.getStringExtra("dayData");
            availableTimesList = (ArrayList<String>) intent.getStringArrayListExtra("availableTimesList");

        } else {
            clinic = null;
            selectedDay = null;
            availableTimesList = null;
        }
        // Set the values to the corresponding TextViews
        if (clinic != null) {
            clinicNameTextView.setText(clinic.getClinicName());
        }

        if (selectedDay != null) {
            dayOfWeekTextView.setText(selectedDay);
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
        CollectionReference appointmentsRef = db.collection("clinic").document(clinic.getID()).collection("days").document(selectedDay).collection("appointments");


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
                String day = selectedDay;
                Log.d("selectedDate", "selectedDate " + ": " + selectedDay);

                double newtime = timeconverter.convertToDecimal(startTime);

                appointmentmanager.makeSingleRecurringAppointment(clinic.getID(), day, newtime, true);
                appointmentmanager.makeMultipleAppointments(clinic.getClinicName(), clinic.getID(), day, newtime, true);
                Toast.makeText(pick_recurring_appointment_time.this, "(Recurring) Appointment date: " + selectedDay + "\nTime: " + time, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(pick_recurring_appointment_time.this, patient_home_page.class);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(timeAdapter);


    }



}
