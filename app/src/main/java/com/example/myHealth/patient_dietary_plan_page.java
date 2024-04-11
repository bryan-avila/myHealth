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

        // ----- START: Obtain the patient's nutrients limits from the DB -----
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

        // ----- END: Obtain the patient's nutrients limits from the DB -----


        // ----- START: Obtain the patient's nutrient values for today & adjust plan accordingly -----
        userL = patientTodaysNutrientsDocumentRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException error) {
                if(document.exists())
                {
                    // Obtain today's nutrient values and convert them to floats
                    String str_patient_phosphorus = document.get("phosphorus").toString();
                    String str_patient_potassium = document.get("potassium").toString();
                    String str_patient_protein = document.get("protein").toString();

                    i_patient_phosphorus = (float) Float.parseFloat(str_patient_phosphorus);
                    i_patient_potassium = (float) Float.parseFloat(str_patient_potassium);
                    i_patient_protein = (float) Float.parseFloat(str_patient_protein);

                    // Update the page to display today's nutrients
                    TextView todayPhos = findViewById(R.id.text_view_current_phos_value);
                    TextView todayProt = findViewById(R.id.text_view_current_protein_value);
                    TextView todayPotas = findViewById(R.id.text_view_current_potassium_value);
                    todayPhos.setText("Phos: " + (int) Float.parseFloat(str_patient_phosphorus));
                    todayProt.setText("Protein: " + (int) Float.parseFloat(str_patient_protein) * 10 / 10f);
                    todayPotas.setText("Potassium " + (int) Float.parseFloat(str_patient_potassium));

                    // Set up textviews to change
                    TextView header = findViewById(R.id.recommended_meals_title);
                    TextView grains = findViewById(R.id.grains); // grains header
                    TextView fruits = findViewById(R.id.fruits); // fruits header
                    TextView grainsDescription = findViewById(R.id.grains_description);
                    TextView fruitsDescription = findViewById(R.id.fruits_description);

                    // Obtain nutrient limit halfway point
                    float halfway_rec_phos_value = rec_phos_value / 2;
                    float halfway_rec_potas_value = rec_pot_value / 2;
                    float halfway_rec_prot_value = rec_protein_value / 2;

                    // 1) Low Level for all nutrients
                    if(i_patient_phosphorus < halfway_rec_phos_value && i_patient_potassium < halfway_rec_potas_value && i_patient_protein < halfway_rec_prot_value)
                    {
                        header.setText("Recommended Diet for Low Level Nutrients");
                        grains.setText("Grains & Protein Options");
                        grainsDescription.setText("1/3 cup Rice\n 1/2 Cup of Rice\n 1/3 cup of Pasta\n 1 egg\n 1/4 pound Hamburger patty");
                        fruits.setText("Fruits and Vegetables Options");
                        fruitsDescription.setText("1 Apple\n 10 Cherries\n 1/2 Cup of Cooked Broccoli\n 1/2 Cup of Carrots");
                    }

                    // 2) High Level for all nutrients
                    else if(i_patient_phosphorus > halfway_rec_phos_value && i_patient_potassium > halfway_rec_potas_value && i_patient_protein > halfway_rec_prot_value)
                    {
                        header.setText("Recommended Diet for High Level Nutrients");
                        grains.setText("Grains & Protein Options");
                        grainsDescription.setText("- Consider One Serving Options for Grains due to High Phosphorus\n - Consider One Ounce Serving for Protein due to High Protein");
                        fruits.setText("Fruits and Vegetables Options");
                        fruitsDescription.setText("- Consider Small Fruit Serving Sizes due to High Potassium\n -Consider Low Servings for Veggies due to High Potassium");
                    }

                    // 3) High Level Protein
                    else if(i_patient_protein > halfway_rec_prot_value)
                    {
                        header.setText("Recommended Diet for High Protein");
                        grains.setText("Grains & Protein Options");
                        grainsDescription.setText("1 Slice White Bread\n 1/2 Cup of Cooked Rice\n 1/2 Cup Cooked Pasta\n An ounce of protein serving or less");
                        fruits.setText("Fruits and Vegetables Options");
                        fruitsDescription.setText("1 Apple\n 1/2 Cup of Berries\n 1/2 Cup of Cooked Broccoli\n 1/2 Cup of Carrots");
                    }
                }
            }
        });

        // ----- END: Obtain the patient's nutrient values for today & adjust plan accordingly -----

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