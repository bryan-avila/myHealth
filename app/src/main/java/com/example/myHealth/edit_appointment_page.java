package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class edit_appointment_page extends AppCompatActivity {
    FirebaseFirestore db = MyFirestore.getDBInstance();
    String appointmentPath;
    Clinic clinic;
    String clinicName;
    String date;
    String startTime;
    String endTime;

    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), patient_home_page.class));
        finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment_page);

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
                    startActivity(new Intent(getApplicationContext(), patient_medical_records_page.class));
                    finish();
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

        Intent intent = getIntent();
        if (intent != null) {
            appointmentPath = (String) intent.getSerializableExtra("appointmentPath");
        } else {
            appointmentPath = null;
        }

        DocumentReference appointmentDocument = db.document(appointmentPath);
        appointmentDocument.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // DocumentSnapshot contains the data
                            clinicName = documentSnapshot.getString("clinicName");
                            date = documentSnapshot.getString("date");
                            startTime = documentSnapshot.getString("startTime");
                            endTime = documentSnapshot.getString("endTime");
                            Log.d("TAG", "clinic name: " + clinicName);
                            Log.d("TAG", "date: " + date);
                            Log.d("TAG", "start time: " + startTime);
                            Log.d("TAG", "end time: " + endTime);
                            TextView appointmentClinic = (TextView) findViewById(R.id.clinic_name_text);
                            TextView appointmentDate = (TextView) findViewById(R.id.appointment_date_text);
                            TextView appointmentStartTime = (TextView) findViewById(R.id.appointment_start_time_text);
                            TextView appointmentEndTime = (TextView) findViewById(R.id.appointment_end_time_text);
                            appointmentClinic.setText(clinicName);
                            appointmentDate.setText(date);
                            appointmentStartTime.setText("Start Time: " + startTime);
                            appointmentEndTime.setText("End Time: " + endTime);


                            // Use the data as needed
                        } else {
                            // Document does not exist
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                    }
                });

        String clinicID = appointmentDocument.getId();
        Log.d("TAG", "clinicID: " + clinicID);
        CollectionReference clinicsRef = db.collection("clinic");
        clinicsRef.document(clinicID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    clinic = documentSnapshot.toObject(Clinic.class);
                    clinic.setID(clinicID);
                    Log.d("TAG", "clinic: " + clinic);
                } else {
                    // Document does not exist
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle errors
            }
        });
    }

    public void onStart() {
        super.onStart();


    }

    public void onEditClinicClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), edit_appointment_clinic_page.class);
        intent.putExtra("appointmentPath", appointmentPath);
        startActivity(intent);
    }

    public void onEditDateClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), edit_appointment_date_page.class);
        intent.putExtra("appointmentPath", appointmentPath);
        intent.putExtra("clinicData", clinic);
        startActivity(intent);
    }

    public void onEditTimeClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), edit_appointment_time_page.class);
        Log.d("timebutton", "clicked");
        Log.d("TAG", "clinicID: " + clinic.getID());
        intent.putExtra("appointmentPath", appointmentPath);
        intent.putExtra("clinic", clinic);
        intent.putExtra("date", date);
        startActivity(intent);
    }
}