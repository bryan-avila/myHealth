package com.example.myHealth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class first_time_medical_survey_page extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = myFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference userRef = db.collection("users").document(currentUser.getUid());
    CollectionReference medicalHistoryRef = userRef.collection("medicalHistory");




    // TO DO:
    // 1. Create collections specific to a user who has recently signed up that will hold all the info
    // 2. Store the user input here and upload them to firestore
    // 3. Make this page more aesthetically pleasing
    // 4. After medical history q&a is finished, send them to the home page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_registration);

    }

    public void onFinishClick(View view)
    {
        // general medical info
        EditText editTextHeightFeet = findViewById(R.id.edit_text_height_in_feet);
        EditText editTextHeightInches = findViewById(R.id.edit_text_height_in_inches);
        EditText editTextAge = findViewById(R.id.edit_text_age);
        EditText editTextWeight = findViewById(R.id.edit_text_weight);
        EditText editTextBloodType = findViewById(R.id.edit_text_blood_type);
        // gender values
        RadioGroup radioGroupGenderType = findViewById(R.id.radio_group_gender);
        // treatment values
        RadioGroup radioGroupTreatmentType = findViewById(R.id.radio_group_treatment);
        // stage values
        RadioGroup radioGroupStageType = findViewById(R.id.radio_group_stages);
        // other conditions
        CheckBox checkBoxDiabetes = findViewById(R.id.check_box_diabetes);
        CheckBox checkBoxHighBloodPressure = findViewById(R.id.check_box_highBloodPressure);

        String HeightFeet = editTextHeightFeet.getText().toString();
        String HeightInches = editTextHeightInches.getText().toString();
        String Age = editTextAge.getText().toString();
        String Weight = editTextWeight.getText().toString();
        String BloodType = editTextBloodType.getText().toString();
        // find out which gender is chosen
        String Gender = "";
        int selectedRadioButtonGenderId = radioGroupGenderType.getCheckedRadioButtonId();
        if (selectedRadioButtonGenderId != -1) {
            // At least one radio button is selected
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonGenderId);
            Gender = selectedRadioButton.getText().toString();
        } else {
            Gender = "prefer not to say";
        }
        // find out which treatment is chosen
        String Treatment = "";
        int selectedRadioButtonTreatmentId = radioGroupTreatmentType.getCheckedRadioButtonId();
        if (selectedRadioButtonTreatmentId != -1) {
            // At least one radio button is selected
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonTreatmentId);
            Treatment = selectedRadioButton.getText().toString();
        } else {
            Treatment = "N/A";
        }
        // find out which kidney disease stage is chosen
        String Stage = "";
        int selectedRadioButtonStageId = radioGroupStageType.getCheckedRadioButtonId();
        if (selectedRadioButtonStageId != -1) {
            // At least one radio button is selected
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonStageId);
            Stage = selectedRadioButton.getText().toString();
        } else {
            Stage = "N/A";
        }
        // find out if they have diabetes
        String Diabetes = "";
        if (checkBoxDiabetes.isChecked()) {
            // CheckBox is selected
            Diabetes = checkBoxDiabetes.getText().toString();
        } else {
            Diabetes = "N/A";
        }
        // find out if they have high blood pressure
        String HighBloodPressure = "";
        if (checkBoxHighBloodPressure.isChecked()) {
            // CheckBox is selected
            HighBloodPressure = checkBoxHighBloodPressure.getText().toString();
        } else {
            HighBloodPressure = "N/A";
        }


        // add the medical history to a hashmap
        Map<String, Object> medicalHistory = new HashMap<>();
        medicalHistory.put("heightFeet", HeightFeet);
        medicalHistory.put("heightInches", HeightInches);
        medicalHistory.put("age", Age);
        medicalHistory.put("weight", Weight);
        medicalHistory.put("bloodType", BloodType);
        medicalHistory.put("gender", Gender);
        medicalHistory.put("treatment", Treatment);
        medicalHistory.put("stage", Stage);
        medicalHistory.put("diabetes", Diabetes);
        medicalHistory.put("bloodPressure", HighBloodPressure);

        // Add medical history data to the map
        medicalHistoryRef.document("medicalHistory")
                .set(medicalHistory)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle success
                        Log.d(TAG, "Medical history added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Log.w(TAG, "Error adding medical history", e);
                    }
                });


        Intent intent = new Intent(getApplicationContext(), first_time_sign_up_diet_survey.class);
        startActivity(intent);
        finish();
    }
}




