package com.example.myHealth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class first_time_sign_up_diet_survey extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = myFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference userRef = db.collection("users").document(currentUser.getUid());
    CollectionReference dietInfoRef = userRef.collection("dietInfo");

    // TO DO:
    // 1. Do input validation
    // 2. Store information in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_sign_up_diet_survey);
    }


    // onClick listener using .xml declartion onClick="onDietButtonClick"
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
        dietInfoRef.add(dietInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Handle success
                        Log.d(TAG, "Diet info added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Log.w(TAG, "Error adding diet info document", e);
                    }
                });

        // Once they finish inputting diet, send them home
        Intent intent = new Intent(first_time_sign_up_diet_survey.this, home_page.class);
        startActivity(intent);
        finish();

    }
}