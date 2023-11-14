package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class clinic_view_med_hist extends AppCompatActivity {

    // DISPLAY LIST OF PATIENTS
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_view_med_hist);

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_patients);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get all user information from firebase
        CollectionReference patients = db.collection("users");

        patients.get().addOnCompleteListener(task -> {

            //asdasdasdas
            // Create a list of all patients
            List<Patient> patientsList = null;

            if (task.isSuccessful()) {
                patientsList = new ArrayList<>();

                // For loop populating the recycler view
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Patient p = document.toObject(Patient.class);
                    patientsList.add(p);
                }
            }

            MyPatientAdapter myPatAdapater = new MyPatientAdapter(getApplicationContext(), patientsList);
            recyclerView.setAdapter(myPatAdapater);

            // Make Patient's Clickable
            myPatAdapater.setOnItemClickListener(new MyPatientAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, Patient patients) {
                    // Send them to the prescription form after clicking on a patients name using this onItemClickListener
                    Intent intent = new Intent(clinic_view_med_hist.this, clinic_prescription_form.class);
                    // Send extra info to know where to send the medication information
                    intent.putExtra("patient", patients.getEmail());
                    startActivity(intent);
                }
            });

        });

    }
}