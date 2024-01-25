package com.example.myHealth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class clinic_prescriptions_page extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    // Create DB reference for Patient's Prescriptions Collection



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_clinic_view_prescriptions);
    }




    // When clinic clicks this button, send them to back to search for a list of patients
    public void onFillPrescriptionClick(View view) {

        // Obtain information from clinic_med_hist_page.java
        Bundle bundle = getIntent().getExtras();
        String patientId = bundle.getString("patient");

        // Hardcoded example
        DocumentReference patientRef = db.collection("users").document("G0Zg1uGasOQSMJfOBQqz3H88sM83");

        CollectionReference prescriptionsInfoRef = patientRef.collection("prescriptionsInfo");



        // Set up edittext variables from .XML file
        EditText editTextmedicationName = findViewById(R.id.edit_text_med_Name);
        EditText editTextdosageAmt = findViewById(R.id.edit_text_med_Dosage);
        Button btn_frequency = findViewById(R.id.button_med_Frequency);


        // Set up strings the database can use
        String s_med_name = editTextmedicationName.getText().toString();
        String s_dosage_amt = editTextdosageAmt.getText().toString();
        String s_frequency = btn_frequency.getText().toString();


        // Add the medication info to the patient's database collection
        Map<String, Object> medicationInfo = new HashMap<>();
        medicationInfo.put("medicationName", s_med_name);
        medicationInfo.put("dosageAmount", s_dosage_amt);
        medicationInfo.put("medFrequency", s_frequency);

        // Add medical history data to the map
        prescriptionsInfoRef.document("prescriptionInfo")
                .set(medicationInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle success
                        Log.d(TAG, "medicationInfo added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Log.w(TAG, "Error adding medicationInfo document", e);
                    }
                });



        startActivity(new Intent(getApplicationContext(), clinic_prescription_form.class));
    }
}