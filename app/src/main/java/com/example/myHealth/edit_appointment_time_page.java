package com.example.myHealth;

import static com.example.myHealth.TimeConverter.convertToString;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class edit_appointment_time_page extends AppCompatActivity {
    String date;
    Clinic clinic;
    String appointmentPath;
    String clinicAppointmentPath;
    DocumentReference appointmentDocument;
    DocumentReference clinicAppointmentDocument;
    // Inside your activity or fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("timepage", "opened");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_pick_appointment_time);

        ArrayList<String> availableTimesList = new ArrayList<>();

        // Retrieve the data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            appointmentPath = (String) intent.getStringExtra("appointmentPath");
            clinicAppointmentPath = (String) intent.getSerializableExtra("clinicAppointmentPath");
            clinic = (Clinic) intent.getSerializableExtra("clinic");
            date = (String) intent.getStringExtra("date");

        } else {
            appointmentPath = null;
            clinicAppointmentPath = null;
            clinic = null;
            date = null;
        }

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppointmentManager appointmentManager = new AppointmentManager();
        appointmentManager.availableDayTimes(clinic.getID(), date, new OnDataReadyListener<ArrayList<Boolean>>() {
            @Override
            public void onDataReady(ArrayList<Boolean> availabilityList) {
                appointmentManager.dayTimes(clinic.getID(), date, new OnDataReadyListener<ArrayList<String>>() {
                    @Override
                    public void onDataReady(ArrayList<String> timesList) {
                        // Now timesList is ready, and you can proceed with creating the intent
                        for (int i = 0; i < timesList.size(); i++) {
                            if (availabilityList.get(i) == true) {
                                availableTimesList.add(timesList.get(i));
                            }
                        }

                        // Log the size of the ArrayList
                        Log.d("ArrayListInfo", "Size of times recycler view: " + availableTimesList.size());
                        // Log all elements in the ArrayList
                        if (availableTimesList.size() > 0) {
                            for (int i = 0; i < availableTimesList.size(); i++) {
                                Log.d("ArrayListInfo", "Time " + i + ": " + availableTimesList.get(i));
                            }
                        } else {
                            Log.d("ArrayListInfo", "timeslist is Empty");
                        }

                        TextView ClinicName = findViewById(R.id.ClinicName);
                        //set the clinic name to clinic name
                        ClinicName.setText(clinic.getClinicName());

                        //list of items
                        FirebaseFirestore db = MyFirestore.getDBInstance();

                        TimeAdapter timeAdapter = new TimeAdapter(availableTimesList);
                        TimeConverter timeconverter = new TimeConverter();

                        //Implements
                        timeAdapter.setOnItemClickListener(new TimeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(String time) {
                                // Handle the click event with the selected time
                                Log.d("SelectedTime", "Time " + ": " + time);
                                String startTime = time;

                                //initializes extra values to use for better formatting (see below explanation)
                                String meridian = "NULL";
                                int hour = 0;
                                String[] parts = time.split(":");

                                //Checks if "parts[0]" is greater than or EQUAL TO 12 (to convert from 24-hour to 12-hour time format)
                                //If so, use modulus to convert to 12-hour format and assign meridian.
                                //If not (ex: 05:00AM), keep meridian as is.

                                //TO-DO: Change calendar schedule time from 24-hour to 12-hour format.
                                try {
                                    if (Integer.parseInt(parts[0]) >= 12) {
                                        hour = (Integer.parseInt(parts[0])) % 12; //converts into 12-hour format
                                        meridian = "PM";
                                    }
                                    else {
                                        meridian = "AM";
                                    }
                                    int minute = Integer.parseInt(parts[1]);
                                    time = hour + ":" + String.format("%02d", minute);
                                    System.out.println("String " + date);
                                    System.out.println("Int " + "(" + hour + ":" + minute + ")");
                                }
                                catch (NumberFormatException e)
                                {
                                    Toast.makeText(edit_appointment_time_page.this, "SOMETHING WENT WRONG!", Toast.LENGTH_LONG).show();
                                }

                                Log.d("date", "date " + ": " + date);

                                double timedouble = timeconverter.convertToDecimal(startTime);
                                String timestart = convertToString(timedouble);
                                String timeend = convertToString((timedouble + 4));

                                Map<String, Object> updates = new HashMap<>();
                                updates.put("startTime", timestart);
                                updates.put("endTime", timeend);

                                FirebaseFirestore db = MyFirestore.getDBInstance();
                                appointmentDocument = db.document(appointmentPath);
                                clinicAppointmentDocument = db.document(clinicAppointmentPath);
                                appointmentDocument.update(updates)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error updating document", e);
                                            }
                                        });

                                clinicAppointmentDocument.update(updates)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error updating document", e);
                                            }
                                        });

                                //Intent intent = new Intent(edit_appointment_time_page.this, edit_appointment_page.class);
                                //intent.putExtra("appointmentPath", appointmentPath);
                                Intent intent = new Intent(edit_appointment_time_page.this, patient_home_page.class);
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(timeAdapter);
                    }
                });
            }
        });



        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set medical hist selected
        bottomNavigationView.setSelectedItemId(R.id.appointmentId);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentId) {
                    startActivity(new Intent(getApplicationContext(), edit_appointment_time_page.class));
                    finish();
                    return true;
                } else if (id == R.id.homeId) {
                    startActivity(new Intent(getApplicationContext(), patient_home_page.class)); //check this line it might be wrong
                    finish();
                    return true;
                } else if (id == R.id.medicalHistId) {
                    startActivity(new Intent(getApplicationContext(), patient_medical_records_page.class)); //check this line it might be wrong
                    finish();
                    return true;
                } else if (id == R.id.resourcesId) {
                    startActivity(new Intent(getApplicationContext(), patient_dietary_plan_page.class));
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

    public void onClinic1Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), patient_appointments_view.class));
    }

    public void onClinic2Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), patient_appointments_view.class));
    }

    public void onClinic3Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), patient_appointments_view.class));
    }

    public void onClinic4Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), patient_appointments_view.class));
    }

    public void onClinic5Click(View view)
    {
        startActivity(new Intent(getApplicationContext(), patient_appointments_view.class));
    }

}


