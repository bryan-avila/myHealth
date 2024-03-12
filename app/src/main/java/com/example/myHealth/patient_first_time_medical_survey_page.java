package com.example.myHealth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
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

public class patient_first_time_medical_survey_page extends AppCompatActivity {

    // Set up database stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference userRef = db.collection("users").document(currentUser.getUid());
    CollectionReference medicalHistoryRef = userRef.collection("medicalHistory");

    // Set up textview stuff
    TextView header1, header2, header3, header4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_first_time_registration);

        header1 = findViewById(R.id.text_view_p_registration_header_1);
        header2 = findViewById(R.id.text_view_p_registration_header_2);
        header3 = findViewById(R.id.text_view_p_registration_header_3);
        header4 = findViewById(R.id.text_view_p_registration_header_4);

        header1.setPaintFlags(header1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        header1.setText("Enter Your Basic Medical History");

        header2.setPaintFlags(header2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        header2.setText("What Treatment Are You Receiving");

        header3.setPaintFlags(header3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        header3.setText("Which Stage Kidney Disease?");

        header4.setPaintFlags(header4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        header4.setText("Medical History:");


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


        Intent intent = new Intent(getApplicationContext(), patient_first_time_time_diet_survey_page.class);
        startActivity(intent);
        finish(); // cannot press back
    }
}




