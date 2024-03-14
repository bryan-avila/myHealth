package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class patient_view_appointment_history extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_appointment_history);

        FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //list of items
        FirebaseFirestore db = MyFirestore.getDBInstance();
        CollectionReference clinicsRef = db.collection("clinic");

        CollectionReference patientAppointmentDateRef = db.collection("users").document(currentUser.getUid()).collection("dates");
        Log.d("TAG", "Collection Reference: " + patientAppointmentDateRef.getPath());
        patientAppointmentDateRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Appointment> appointments = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    CollectionReference appointments_collection = patientAppointmentDateRef.document(document.getId()).collection("appointments");
                    appointments_collection.get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                Log.d("TAG", "Document data: " + document2.getData());
                                Appointment appointment = document2.toObject(Appointment.class);
                                Log.d("TAG", "FINAL: " + appointment);
                                appointments.add(appointment);
                            }
                            MyUpcomingAppointmentsAdapter myAdapter = new MyUpcomingAppointmentsAdapter(getApplicationContext(), appointments, recyclerView);
                            recyclerView.setAdapter(myAdapter);
                        } else {
                            // Handle the error
                            Log.e("TAG", "Error getting appointments", task2.getException());
                        }
                    });
                }
            } else {
                // Handle the error
                Log.e("TAG", "Error getting appointments", task.getException());
            }
        });
    }
}