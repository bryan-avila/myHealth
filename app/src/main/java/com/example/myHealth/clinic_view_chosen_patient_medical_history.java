package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class clinic_view_chosen_patient_medical_history extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    private ListenerRegistration userMedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_view_chosen_patient_medical_history);
    }

    public void onStart() {
        // Obtain information from clinic_view_patients_medical_records.java
        Bundle bundle = getIntent().getExtras();
        String patientId = bundle.getString("patient");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference userMedRef = db.collection("users").document(patientId).collection("medicalHistory").document("medicalHistory");

        if (currentUser != null) {
            DocumentReference userRef = db.collection("medicalHistory").document(patientId);

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // User document exists, retrieve data
                            String age = document.getString("age");
                            // Retrieve other user data as needed
                        } else {
                            // User document doesn't exist, handle accordingly
                        }
                    } else {
                        // Handle failure to retrieve user document
                    }
                }
            });
        }


        super.onStart();
        TextView pAge = (TextView) findViewById(R.id.text_view_age_pvr_clinic);
        TextView pHeightFeet = (TextView) findViewById(R.id.text_view_height_feet_pvr_clinic);
        TextView pHeightInches = (TextView) findViewById(R.id.text_view_height_inches_pvr_clinic);
        TextView pWeight = (TextView) findViewById(R.id.text_view_weight_pvr_clinic);
        TextView pBloodType = (TextView) findViewById(R.id.text_view_blood_type_pvr_clinic);
        TextView pGender = (TextView) findViewById(R.id.text_view_gender_pvr_clinic);
        TextView pTreatment = (TextView) findViewById(R.id.radio_button_treatment_pvr_clinic);
        TextView pStage = (TextView) findViewById(R.id.radio_button_kidney_disease_stage_pvr_clinic);
        TextView pDiabetes = (TextView) findViewById(R.id.check_box_diabetes_pvr_clinic);
        TextView pBloodPressure = (TextView) findViewById(R.id.check_box_highBloodPressure_pvr_clinic);
        userMedListener = userMedRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                // Error checking
                if(error != null)
                {
                    Toast.makeText(clinic_view_chosen_patient_medical_history.this, "Error while loading!", Toast.LENGTH_LONG).show();
                    return;
                }
                //check if the user is a patient or a clinic
                if (documentSnapshot.exists())
                {

                    if (documentSnapshot.contains("location")) {
                    }
                    else {
                        // Document doesn't have the specific element
                        // Perform your action here
                        String patient_age = documentSnapshot.get("age").toString();
                        pAge.setText("Age: " + patient_age);
                        String patient_heightF = documentSnapshot.get("heightFeet").toString();
                        pHeightFeet.setText("Height(Feet): " + patient_heightF);
                        String patient_heightI = documentSnapshot.get("heightInches").toString();
                        pHeightInches.setText("Height(Inches): " + patient_heightI);
                        String patient_weight = documentSnapshot.get("weight").toString();
                        pWeight.setText("Weight: " + patient_weight);
                        String patient_blood = documentSnapshot.get("bloodType").toString();
                        pBloodType.setText("Blood Type: " + patient_blood);
                        String patient_gender = documentSnapshot.get("gender").toString();
                        pGender.setText("Gender: " + patient_gender);
                        String patient_treatment = documentSnapshot.get("treatment").toString();
                        pTreatment.setText(patient_treatment);
                        String patient_stage = documentSnapshot.get("stage").toString();
                        pStage.setText(patient_stage);
                        String patient_diabetes = documentSnapshot.get("diabetes").toString();
                        pDiabetes.setText(patient_diabetes);
                        String patient_pressure = documentSnapshot.get("bloodPressure").toString();
                        pBloodPressure.setText(patient_pressure);
                    }


                }
            }
        });
    }
}