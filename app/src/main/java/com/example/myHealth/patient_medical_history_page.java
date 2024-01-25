package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
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

public class patient_medical_history_page extends AppCompatActivity {


    TextView top_message;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    // How to get patient's medical history data without hard coding??
    private DocumentReference userMedRef = db.collection("users").document(currentUser.getUid()).collection("medicalHistory").document("medicalHistory");
    private ListenerRegistration userMedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_med_history);
        // Set the top message to bold and underline
        top_message = findViewById(R.id.text_view_med_history_text);
        top_message.setPaintFlags(top_message.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
    }

    public void onStart() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DocumentReference userRef = db.collection("medicalHistory").document(currentUser.getUid());

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
        TextView pAge = (TextView) findViewById(R.id.text_view_age_pvr);
        TextView pHeightFeet = (TextView) findViewById(R.id.text_view_height_feet_pvr);
        TextView pHeightInches = (TextView) findViewById(R.id.text_view_height_inches_pvr);
        TextView pWeight = (TextView) findViewById(R.id.text_view_weight_pvr);
        TextView pBloodType = (TextView) findViewById(R.id.text_view_blood_type_pvr);
        TextView pGender = (TextView) findViewById(R.id.text_view_gender_pvr);
        TextView pTreatment = (TextView) findViewById(R.id.radio_button_treatment_pvr);
        TextView pStage = (TextView) findViewById(R.id.radio_button_kidney_disease_stage_pvr);
        TextView pDiabetes = (TextView) findViewById(R.id.check_box_diabetes_pvr);
        TextView pBloodPressure = (TextView) findViewById(R.id.check_box_highBloodPressure_pvr);
        userMedListener = userMedRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                // Error checking
                if(error != null)
                {
                    Toast.makeText(patient_medical_history_page.this, "Error while loading!", Toast.LENGTH_LONG).show();
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