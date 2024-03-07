package com.example.myHealth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class clinic_edit_my_profile extends AppCompatActivity {

    EditText startHour, finishHour, numofMachines;
    FirebaseFirestore db = MyFirestore.getDBInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    // Gave Clinics a collection inside a document
    private DocumentReference clinicRef = db.collection("clinic").document(currentUser.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_edit_my_profile);
    }

    public void onClinicSurveyClickCancel(View view) {
        finish();
    }

    public void onClinicSurveyClick(View view)
    {
        // Initialize objects
        startHour = findViewById(R.id.edit_text_start_hours_operation);
        finishHour = findViewById(R.id.edit_text_end_hours_operation);
        numofMachines = findViewById(R.id.edit_text_num_of_machines);

        if (startHour.getText().toString().equals("") ||
                finishHour.getText().toString().equals("") ||
                    numofMachines.getText().toString().equals("")) {
            Toast.makeText(clinic_edit_my_profile.this, "Error: Field(s) are blank.", Toast.LENGTH_SHORT).show();
        } else {
            // Set user data to strings to pass to Firebase
            String sHour = startHour.getText().toString();
            int intsHour = Integer.parseInt(sHour);
            String fHour = finishHour.getText().toString();
            int intfHour = (Integer.parseInt(fHour) + 12);
            String numOfM = numofMachines.getText().toString();
            int intnumOfM = Integer.parseInt(numOfM);

            // add user data to hashmap
            Map<String, Object> clinicInfo = new HashMap<>();
            clinicInfo.put("startHour", intsHour);
            clinicInfo.put("closeHour", intfHour);
            clinicInfo.put("numOfMachines", intnumOfM);


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
                            Log.w(TAG, "Error adding clinic info", e);
                        }
                    });

            // Send to clinic home page
            Intent intent = new Intent(clinic_edit_my_profile.this, clinic_home_page.class);
            startActivity(intent);
            finish(); // cannot go back
        }
    }
}