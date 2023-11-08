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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class first_time_clinic_sign_up_survey extends AppCompatActivity {



    EditText startHour, finishHour, numofMachines;
    FirebaseFirestore db = myFirestore.getDBInstance();
    FirebaseAuth mAuth = myFirestore.getmAuthInstance();
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

        // Get firebase info
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference clinicRef = db.collection("clinic").document(currentUser.getUid());

        // Set user data to strings to pass to Firebase
        String sHour = startHour.toString();
        String fHour = finishHour.toString();
        String numOfM = numofMachines.toString();
        clinicRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "task successful");
                    if (document.exists()) {
                        Log.d(TAG, "document doesnt exist");
                        // Create a new clinic
                        Map<String, Object> clinic = new HashMap<>();
                        clinic.put("startHour", sHour);
                        clinic.put("closeHour", fHour);
                        clinic.put("numOfMachines", numOfM);


                        clinicRef.set(clinic)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // User document created successfully
                                        Log.d(TAG, "added hours and machines");
                                        // Upon registration send users to do medical history questions
                                        Intent intent = new Intent(first_time_clinic_sign_up_survey.this, clinic_home_page.class);
                                        startActivity(intent);
                                        finish(); // If you don't want to allow the user to go back to the registration screen
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure to create user document
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });


                    }
                }
            }
        });
    }
}