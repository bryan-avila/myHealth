package com.example.myHealth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

public class patient_first_time_time_diet_survey_page extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference userRef = db.collection("users").document(currentUser.getUid());

    CollectionReference dietInfoRef = userRef.collection("dietInfo");

    private DocumentReference userMedHistory = db.collection("users").document(currentUser.getUid()).collection("medicalHistory").document("medicalHistory");
    private ListenerRegistration userMedHistoryListener;

    Button autofill_btn;

    String chosen_stage;


    // TO DO:
    // 1. Do input validation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_first_time_sign_up_diet_survey);

    }


    // onClick listener using .xml declaration onClick="onDietButtonClick"
    public void onDietButtonClick(View view)
    {
        EditText editTextPhosphorus = findViewById(R.id.edit_text_phosphorus);
        EditText editTextPotassium = findViewById(R.id.edit_text_potassium);
        EditText editTextProtein = findViewById(R.id.edit_text_protein);
        EditText editTextSodium = findViewById(R.id.edit_text_sodium);
        EditText editTextWater = findViewById(R.id.edit_text_water);
        EditText editTextCalories = findViewById(R.id.edit_text_calories);

        String Phosphorus = editTextPhosphorus.getText().toString() + " mg";
        String Potassium = editTextPotassium.getText().toString() + " mg";
        String Protein = editTextProtein.getText().toString() + " g";
        String Sodium = editTextSodium.getText().toString() + " mg";
        String Water = editTextWater.getText().toString() + " ml";
        String Calories = editTextCalories.getText().toString() + " kcal";

        // add the medical history to a hashmap
        Map<String, Object> dietInfo = new HashMap<>();
        dietInfo.put("phosphorus", Phosphorus);
        dietInfo.put("potassium", Potassium);
        dietInfo.put("protein", Protein);
        dietInfo.put("sodium", Sodium);
        dietInfo.put("water", Water);
        dietInfo.put("calories", Calories);

        // Add medical history data to the map
        dietInfoRef.document("dietInfo")
                .set(dietInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle success
                        Log.d(TAG, "Diet info added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Log.w(TAG, "Error adding diet info document", e);
                    }
                });

        // Once they finish inputting diet, send them to page that lets them make appointments
        Intent intent = new Intent(getApplicationContext(), patient_first_time_recurring_clinics.class);
        startActivity(intent);
        finish(); // cannot press back

    }


    // Loading user's data to check if they are
    // In Stage 5/4 and general health guidelines
    public void onStart() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DocumentReference userRef = db.collection("users").document(currentUser.getUid());

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // User document exists, retrieve data
                            String userFirstName = document.getString("firstName");
                            String userLastName = document.getString("lastName");
                            String userEmail = document.getString("email");
                            String userPhone = document.getString("phone");
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
        // Automatically loading of user data
        userMedHistoryListener = userMedHistory.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                // Error checking
                if(error != null)
                {
                    Toast.makeText(patient_first_time_time_diet_survey_page.this, "Error while loading!", Toast.LENGTH_LONG).show();
                    return;
                }

                // If medical history data exists
                if (documentSnapshot.exists())
                {
                    // Initialize data
                    String patient_stage = documentSnapshot.get("stage").toString();
                    TextView stage_4_message = findViewById(R.id.text_view_stage_4_warning);
                    TextView stage_5_message = findViewById(R.id.text_view_stage_5_warning);


                    // Display different nutrient values depending on stage/med history/age
                    if(patient_stage.equals("Kidney Disease Stage 4"))
                    {
                     /*   // Load in general nutrient amounts and warning message
                        EditText editTextPhosphorus = findViewById(R.id.edit_text_phosphorus);
                        EditText editTextPotassium = findViewById(R.id.edit_text_potassium);
                        EditText editTextProtein = findViewById(R.id.edit_text_protein);
                        EditText editTextSodium = findViewById(R.id.edit_text_sodium);
                        EditText editTextWater = findViewById(R.id.edit_text_water);
                        EditText editTextCalories = findViewById(R.id.edit_text_calories);
                        editTextPhosphorus.setText("900", TextView.BufferType.EDITABLE);
                        editTextPotassium.setText("2500", TextView.BufferType.EDITABLE);
                        editTextProtein.setText("46",TextView.BufferType.EDITABLE);
                        editTextSodium.setText("2300", TextView.BufferType.EDITABLE);
                        editTextWater.setText("N/A", TextView.BufferType.EDITABLE);
                        editTextCalories.setText("2000", TextView.BufferType.EDITABLE);*/
                        chosen_stage = "4";
                        stage_4_message.setVisibility(View.VISIBLE);
                        autofill_btn = findViewById(R.id.button_patient_diet_auto_fill);
                        autofill_btn.setVisibility(View.VISIBLE);
                        autofill_btn.setClickable(true);
                    }

                    else if(patient_stage.equals("Kidney Disease Stage 5"))
                    {
                       /* EditText editTextPhosphorus = findViewById(R.id.edit_text_phosphorus);
                        EditText editTextPotassium = findViewById(R.id.edit_text_potassium);
                        EditText editTextProtein = findViewById(R.id.edit_text_protein);
                        EditText editTextSodium = findViewById(R.id.edit_text_sodium);
                        EditText editTextWater = findViewById(R.id.edit_text_water);
                        EditText editTextCalories = findViewById(R.id.edit_text_calories);
                        editTextPhosphorus.setText("800", TextView.BufferType.EDITABLE);
                        editTextPotassium.setText("2000", TextView.BufferType.EDITABLE);
                        editTextProtein.setText("40",TextView.BufferType.EDITABLE);
                        editTextSodium.setText("1800", TextView.BufferType.EDITABLE);
                        editTextWater.setText("N/A", TextView.BufferType.EDITABLE);
                        editTextCalories.setText("2000", TextView.BufferType.EDITABLE);*/
                        chosen_stage ="5";
                        stage_5_message.setVisibility(View.VISIBLE);
                        autofill_btn = findViewById(R.id.button_patient_diet_auto_fill);
                        autofill_btn.setVisibility(View.VISIBLE);
                        autofill_btn.setClickable(true);
                    }
                }
            }
        });
    }

    public void onAutoFillClick(View view)
    {

        if("4".equals(chosen_stage)) {
            EditText editTextPhosphorus = findViewById(R.id.edit_text_phosphorus);
            EditText editTextPotassium = findViewById(R.id.edit_text_potassium);
            EditText editTextProtein = findViewById(R.id.edit_text_protein);
            EditText editTextSodium = findViewById(R.id.edit_text_sodium);
            EditText editTextWater = findViewById(R.id.edit_text_water);
            EditText editTextCalories = findViewById(R.id.edit_text_calories);
            editTextPhosphorus.setText("900", TextView.BufferType.EDITABLE);
            editTextPotassium.setText("2500", TextView.BufferType.EDITABLE);
            editTextProtein.setText("46", TextView.BufferType.EDITABLE);
            editTextSodium.setText("2300", TextView.BufferType.EDITABLE);
            editTextWater.setText("N/A", TextView.BufferType.EDITABLE);
            editTextCalories.setText("2000", TextView.BufferType.EDITABLE);
        }

        if("5".equals(chosen_stage)) {
            EditText editTextPhosphorus = findViewById(R.id.edit_text_phosphorus);
            EditText editTextPotassium = findViewById(R.id.edit_text_potassium);
            EditText editTextProtein = findViewById(R.id.edit_text_protein);
            EditText editTextSodium = findViewById(R.id.edit_text_sodium);
            EditText editTextWater = findViewById(R.id.edit_text_water);
            EditText editTextCalories = findViewById(R.id.edit_text_calories);
            editTextPhosphorus.setText("800", TextView.BufferType.EDITABLE);
            editTextPotassium.setText("2000", TextView.BufferType.EDITABLE);
            editTextProtein.setText("40", TextView.BufferType.EDITABLE);
            editTextSodium.setText("1800", TextView.BufferType.EDITABLE);
            editTextWater.setText("N/A", TextView.BufferType.EDITABLE);
            editTextCalories.setText("2000", TextView.BufferType.EDITABLE);
        }


    }
}