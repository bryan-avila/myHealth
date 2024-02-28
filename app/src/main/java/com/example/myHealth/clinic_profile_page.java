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

public class clinic_profile_page extends AppCompatActivity {
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), clinic_home_page.class));
        finish();

    }

    TextView clinicEmailPlaceholder, clinicNamePlaceholder, clinicLocationPlaceholder, clinicPhonePlaceholder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference uRef = db.collection("clinic").document(currentUser.getUid());
    private ListenerRegistration userL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_profile_page);

        clinicEmailPlaceholder = findViewById(R.id.clinicEmailPlaceholder);
        clinicNamePlaceholder = findViewById(R.id.clinicNamePlaceholder);
        clinicLocationPlaceholder = findViewById(R.id.clinicLocationPlaceholder);
        clinicPhonePlaceholder = findViewById(R.id.clinicPhonePlaceholder);

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);

        //Set profile selected
        bottomNavigationView.setSelectedItemId(R.id.profileIdClinic);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentIdClinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_appointments_page.class));
                    finish();
                    return true;
                } else if (id == R.id.homeIdCLinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_home_page.class));
                    finish();
                    return true;
                } else if (id == R.id.medicalHistIdClinic) {
                    startActivity(new Intent(getApplicationContext(), clinic_medical_records_page.class));
                    finish();
                    return true;
                } else if (id == R.id.resourcesIdClinic) {
                    startActivity(new Intent(getApplicationContext(), resources_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.profileIdClinic) {
                    return true;
                }
                return false;
            }

        });

        //Log out button here

        Button log_out_button = findViewById(R.id.button_clinic_log_out);

        log_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
                mAuth.signOut();

                // When a clinic logs out, send them to the new "Default" screen, account_type_login_checker.class
                Intent intent = new Intent(getApplicationContext(), account_type_login_checker.class);
                startActivity(intent);

                // Optionally, finish the current activity to prevent the user from going back to it
                finishAffinity(); // cannot press back
            }
        });
    }

    public void onEditClickClinic(View view) {
        startActivity(new Intent(getApplicationContext(), clinic_edit_my_profile.class));
    }


    public void onStart() {
        {

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                DocumentReference userRef = db.collection("clinic").document(currentUser.getUid());

                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // User document exists, retrieve data
                                String clinicName = document.getString("clinicName");
                                String clinicEmail = document.getString("email");
                                String clinicLocation = document.getString("location");
                                String clinicPhone = document.getString("phone");
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
                        Toast.makeText(clinic_profile_page.this, "Error while loading!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //check if the user is a patient or a clinic
                    if (documentSnapshot.exists()) {
                        // Document exists
                        // This will do the same work as the onLoad method
                        // But it is done automatically
                        if (documentSnapshot.contains("location")) {
                            String clinic_name = documentSnapshot.get("clinicName").toString();
                            clinicNamePlaceholder.setText(clinic_name);
                            String clinic_Email = documentSnapshot.get("email").toString();
                            clinicEmailPlaceholder.setText(clinic_Email);
                            String clinic_location = documentSnapshot.get("location").toString();
                            clinicLocationPlaceholder.setText(clinic_location);
                            String clinic_phone = documentSnapshot.get("phone").toString();
                            clinicPhonePlaceholder.setText(clinic_phone);
                        } else {
                            //do nothing as the clinic is the user
                        }
                    }
                }
            });
        }

    }
}