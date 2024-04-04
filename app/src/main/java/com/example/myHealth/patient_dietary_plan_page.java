package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class patient_dietary_plan_page extends AppCompatActivity {

    // Grab todays_date for DB access
    String todays_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    // Set up DB Stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    DocumentReference patientTodaysNutrientsDocumentRef = db.collection("users").document(currentUser.getUid()).collection("nutrients").document(todays_date);
    DocumentReference patientNutrientsLimitDocumentRef = db.collection("users").document(currentUser.getUid()).collection("dietInfo").document("dietInfo");
    private ListenerRegistration userL;

    // Global float values of patient nutrient limits (taken from String)
    float rec_protein_value;
    float rec_phos_value;
    float rec_pot_value;

    // Set up Patient Nutrient Variables
    public float i_patient_phosphorus, i_patient_potassium, i_patient_protein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_dietary_plan);

        // Obtain the patient's nutrients limits from the DB
        userL = patientNutrientsLimitDocumentRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException error) {

                if(document.exists()) // Obtain the nutrient limits for the patient
                {
                    String str_rec_phos_amt = document.get("phosphorus").toString();
                    TextView rec_phos_amt = findViewById(R.id.text_view_patient_nutrition_plan_phos_limit);
                    rec_phos_amt.setText("Phos Limit: " + str_rec_phos_amt);
                    String str_numeric_phos = str_rec_phos_amt.replaceAll("[^0-9]", ""); // Removes non-digit characters from the DB
                    rec_phos_value = (float) Float.parseFloat(str_numeric_phos); // Global Float Rec Phos Value!

                    String str_rec_protein_amt = document.get("protein").toString();
                    TextView rec_protein_amt = findViewById(R.id.text_view_patient_nutrition_plan_prot_limit);
                    rec_protein_amt.setText("Prot Limit: " + str_rec_protein_amt);
                    String str_numeric_protein = str_rec_protein_amt.replaceAll("[^0-9]", ""); // Removes non-digit characters from the DB
                    rec_protein_value = (float) Float.parseFloat(str_numeric_protein); // Global Float Prot Value!

                    String str_rec_potas_amt = document.get("potassium").toString();
                    TextView rec_pot_amt = findViewById(R.id.text_view_patient_nutrition_plan_potas_limit);
                    rec_pot_amt.setText("Potas. Limit: " + str_rec_potas_amt);
                    String str_numeric_potas = str_rec_potas_amt.replaceAll("[^0-9]", ""); // Removes non-digit characters from the DB
                    rec_pot_value = (float) Float.parseFloat(str_numeric_potas); // Global Float Potas Value!

                }

            }
        });

        userL = patientTodaysNutrientsDocumentRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException error) {
                if(document.exists())
                {
                    String str_patient_phosphorus = document.get("phosphorus").toString();
                    String str_patient_potassium = document.get("potassium").toString();
                    String str_patient_protein = document.get("protein").toString();
                    i_patient_phosphorus = (float) Float.parseFloat(str_patient_phosphorus);
                    i_patient_potassium = (float) Float.parseFloat(str_patient_potassium);
                    i_patient_protein = (float) Float.parseFloat(str_patient_protein);
                    TextView header = findViewById(R.id.recommended_meals_title);

                    float halfway_rec_phos_value = rec_phos_value / 2;
                    float halfway_rec_potas_value = rec_pot_value / 2;
                    float halfway_rec_prot_value = rec_protein_value / 2;

                    // Change Plan depending on if the patient has exceeded or is within, or is close to nutrient limits
                    if(i_patient_phosphorus < halfway_rec_phos_value && i_patient_potassium < halfway_rec_potas_value && i_patient_protein < halfway_rec_prot_value)
                    {
                        header.setText("Recommended Diet for Low Level Nutrients");
                        // Grans and fruits can stay the same for now!
                    }
                    if(i_patient_phosphorus > halfway_rec_phos_value)
                    {
                        header.setText("Recommended Diet for High Phosphorus Level");
                    }
                    //TODO Change plans
                }
            }
        });

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set medical hist selected
        bottomNavigationView.setSelectedItemId(R.id.medicalHistId);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentId) {

                    startActivity(new Intent(getApplicationContext(),
                            patient_search_centers_visit_page.class));
                    finish();
                    return true;
                } else if (id == R.id.homeId) {
                    startActivity(new Intent(getApplicationContext(), patient_home_page.class)); //check this line it might be wrong
                    finish();
                    return true;
                } else if (id == R.id.medicalHistId) {
                    return true;
                } else if (id == R.id.resourcesId) {
                    startActivity(new Intent(getApplicationContext(), patient_nutrition_page.class));
                    finish();
                    return true;
                } else if (id == R.id.profileId) {
                    startActivity(new Intent(getApplicationContext(), patient_profile_page.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}