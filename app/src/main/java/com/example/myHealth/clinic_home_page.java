package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class clinic_home_page extends AppCompatActivity {
    TextView dateFormat;
    TextView clinicNumOfMachines;
    Calendar calendar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = myFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    // Get the clinic information from the database
    private DocumentReference clinicRef = db.collection("clinic").document(currentUser.getUid());
    private ListenerRegistration clinicListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_home_page);


        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.homeIdCLinic);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentIdClinic) {
                    startActivity(new Intent(getApplicationContext(), appointments_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.homeIdCLinic) {
                    return true;
                } else if (id == R.id.medicalHistIdClinic) {
                    startActivity(new Intent(getApplicationContext(), medical_records_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.resourcesIdClinic) {
                    startActivity(new Intent(getApplicationContext(), resources_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.profileIdClinic) {
                    startActivity(new Intent(getApplicationContext(), profile_page_clinic.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

        // Display current date by using clinic specific date R.id
        dateFormat = (TextView) findViewById(R.id.text_view_clinic_home_page_date);

        String date;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("'Today is': MMMM dd, yyyy");
        date = simpleDateFormat.format(calendar.getTime()).toString();
        dateFormat.setText(date);

    }

    // AUTOMATIC LOADING OF DATABASE INFORMATION
    public void onStart() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //
            DocumentReference cRef = db.collection("clinic").document(currentUser.getUid());

            cRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // User document exists, retrieve data
                            String cName = document.getString("clinicName");

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
        clinicListener = clinicRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                // Error checking
                if(error != null)
                {
                    Toast.makeText(clinic_home_page.this, "Error while loading!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot.exists()) {
                    // Document exists
                    // This will do the same work as the onLoad method
                    // But it is done automatically

                    // Set greeting
                    calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    // Get the dashboard of the clinic
                    TextView welcome = (TextView) findViewById(R.id.text_view_dashboard_clinic);
                    clinicNumOfMachines = (TextView) findViewById(R.id.text_view_clinic_num_of_machines);

                    String greeting = "null";
                    if (hour >= 5 && hour < 12){
                        greeting = "Good morning, ";
                    } else if (hour >= 12 && hour < 17){
                        greeting = "Good afternoon, ";
                    } else {
                        greeting = "Good evening, ";
                    }

                        // Set a greeting message using the Clinic's name
                        String user_firstname = documentSnapshot.get("clinicName").toString();
                        welcome.setText(greeting + user_firstname + ".");
                     String cNumOfMachines = documentSnapshot.get("numOfMachines").toString();
                     clinicNumOfMachines.setText(cNumOfMachines + " machines available for use");


                }
            }
        });
    }
}
