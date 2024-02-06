package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class patient_diet_page extends AppCompatActivity {
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), patient_home_page.class));
        finish();

    }
    // Initialize Database Stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    DocumentReference patientRef = db.collection("users").document(currentUser.getUid());

    private ListenerRegistration patientListener;

    // Initialize Activity Stuff
    Button addFoodBtn, viewFavoritesBtn, viewChartsBtn;

    TextView header_and_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_diet_page);

        // Create and modify header to have date
        header_and_date = findViewById(R.id.text_view_patient_nutrition_page_text);
        String date;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        date = simpleDateFormat.format(calendar.getTime()).toString();
        header_and_date.setText("Nutrition - " + date);

        // Assign buttons
        addFoodBtn = findViewById(R.id.button_add_foods);
        viewFavoritesBtn = findViewById(R.id.button_view_favorites);
        viewChartsBtn = findViewById(R.id.button_view_charts);

        // Set onclick listener methods to buttons
        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), patient_add_food_page.class);
                startActivity(intent);
            }
        });

        viewFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), patients_view_food_favorites_page.class);
                startActivity(intent);
            }
        });

        viewChartsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), patient_view_food_charts_page.class);
                startActivity(intent);
            }
        });



        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set resources selected
        bottomNavigationView.setSelectedItemId(R.id.resourcesId);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentId) {
                    startActivity(new Intent(getApplicationContext(), patient_search_centers_visit_page.class));
                    finish();
                    return true;
                } else if (id == R.id.homeId) {
                    startActivity(new Intent(getApplicationContext(), patient_home_page.class));
                    finish();
                    return true;
                } else if (id == R.id.medicalHistId) {
                    startActivity(new Intent(getApplicationContext(), patient_medical_records_page.class));
                    finish();
                    return true;
                } else if (id == R.id.resourcesId) {
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

    public void onStart() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            // Get current user, from collection users
            DocumentReference patientRef = db.collection("users").document(currentUser.getUid());

            patientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // User document exists, retrieve data
                            String patient_first_name = document.getString("firstName");

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
        // Automatic loading
        patientListener = patientRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                // Error checking
                if(error != null)
                {
                    Toast.makeText(patient_diet_page.this, "Error while loading!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot.exists()) {
                    // Change a textview from the dietpage to
                    TextView welcome = (TextView) findViewById(R.id.text_view_nutrition_default_text);
                    String patient_firstname = documentSnapshot.get("firstName").toString();
                    welcome.setText(patient_firstname + "!!");

                }
            }
        });
    }
}