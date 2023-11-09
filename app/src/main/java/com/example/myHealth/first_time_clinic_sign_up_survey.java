package com.example.myHealth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class first_time_clinic_sign_up_survey extends AppCompatActivity {



    EditText startHour, finishHour, numofMachines;
    FirebaseFirestore db = myFirestore.getDBInstance();
    FirebaseAuth mAuth = myFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    // Gave Clinics a collection inside a document
    private DocumentReference clinicRef = db.collection("clinic").document(currentUser.getUid());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_clinic_sign_up_survey);
    }

    public void onClinicSurveyClick(View view)
    {
        // Initialize objects
        startHour = findViewById(R.id.edit_text_start_hours_operation);
        finishHour = findViewById(R.id.edit_text_end_hours_operation);
        numofMachines = findViewById(R.id.edit_text_num_of_machines);

        // Set user data to strings to pass to Firebase
        String sHour = startHour.getText().toString();
        String fHour = finishHour.getText().toString();
        String numOfM = numofMachines.getText().toString();

        // add user data to hashmap
        Map<String, Object> clinicInfo = new HashMap<>();
        clinicInfo.put("startHour", sHour);
        clinicInfo.put("closeHour", fHour);
        clinicInfo.put("numOfMachines", numOfM);


        // Add the data to the firebase
        clinicRef.update(clinicInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle success
                        Log.d(TAG, "Added clinic info!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Log.w(TAG, "Error adding clinc info", e);
                    }
                });

        // Send to clinic home page
        Intent intent = new Intent(first_time_clinic_sign_up_survey.this, clinic_home_page.class);
        startActivity(intent);
        finish();
    }
}