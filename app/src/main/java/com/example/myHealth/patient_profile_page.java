package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class patient_profile_page extends AppCompatActivity {
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), patient_home_page.class));
        finish();

    }

    TextView emailPlaceholder, firstnamePlaceholder, lastnamePlaceholder, phonePlaceholder;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference uRef = db.collection("users").document(currentUser.getUid());
    private ListenerRegistration userL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_page);

        emailPlaceholder = findViewById(R.id.emailPlaceholder);
        firstnamePlaceholder = findViewById(R.id.firstnamePlaceholder);
        lastnamePlaceholder = findViewById(R.id.lastnamePlaceholder);
        phonePlaceholder = findViewById(R.id.phonePlaceholder);


        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set profile selected
        bottomNavigationView.setSelectedItemId(R.id.profileId);

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
                    startActivity(new Intent(getApplicationContext(), patient_nutrition_page.class));
                    finish();
                    return true;
                } else if (id == R.id.profileId) {
                    return true;
                }
                return false;
            }

        });

        // Create button object, id from patient_profile_page.xml
        Button lg_out_btn = findViewById(R.id.button_patient_log_out);

        // Add button functionality with this method
        lg_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
                mAuth.signOut();

                // When a patient logs out, send them to the new "Default" screen, account_type_login_checker.class
                Intent intent = new Intent(getApplicationContext(), account_type_login_checker.class);
                startActivity(intent);

                // Optionally, finish the current activity to prevent the user from going back to it
                finish(); //cannot press back
            }
        });
    }

    public void onEditClick(View view) {
        startActivity(new Intent(getApplicationContext(),edit_profile_activity.class));
    }

    public void onResourcesClick(View view) {
        startActivity(new Intent(getApplicationContext(), empty_resourcesPage.class));
    }

    public void onStart() {
        {

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
            // Automatically loading
            // Firestore wants to load things quickly, so it loads in locally before from the cloud
            // Save addSnapShotListener to noteListener, automatically detach/attach by adding this
            userL = uRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                    // Error checking
                    if (error != null) {
                        Toast.makeText(patient_profile_page.this, "Error while loading!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //check if the user is a patient or a clinic
                    if (documentSnapshot.exists()) {
                        // Document exists
                        // This will do the same work as the onLoad method
                        // But it is done automatically
                        if (documentSnapshot.contains("location")) {
                        } else {
                            // Document doesn't have the specific element
                            // Perform your action here
                            String user_firstname = documentSnapshot.get("firstName").toString();
                            firstnamePlaceholder.setText(user_firstname);
                            String user_email = documentSnapshot.get("email").toString();
                            emailPlaceholder.setText(user_email);
                            String user_lastname = documentSnapshot.get("lastName").toString();
                            lastnamePlaceholder.setText(user_lastname);
                            String phone_number = documentSnapshot.get("phone").toString();
                            phonePlaceholder.setText(phone_number);
                        }


                    }
                }
            });
        }

    }

}