package com.example.myHealth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.operation.Merge;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class patient_home_page extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference userRef = db.collection("users").document(currentUser.getUid());
    private ListenerRegistration userListener;
    TextView dateFormat;
    Calendar calendar;
    TextView txtToken;
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("❌ Closing App")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        System.exit(1);
                    }
                }).create().show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_page);

        // START OF NUTRIENT DATABASE STUFF****

        // Grabbing today's date for storing food eaten that day
        String todays_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Set Nutrient values to be EMPTY on a new day
        Map<String, Object> todays_nutrients = new HashMap<>();
        todays_nutrients.put("phosphorus", 0);
        todays_nutrients.put("potassium", 0);
        todays_nutrients.put("protein", 0);

        // Check if today_date document already exists
        // If it doesn't set nutrient values to be 0
        // Check logcat to see which branch was picked
        userRef.collection("nutrients").document(todays_date).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot todays_document = task.getResult();

                    if(todays_document.exists()) // Does today's document exist?
                    {
                        System.out.println("---------- ATTN: patient_home_page.java - " + todays_date + " already exists in FireBaseDB as a DOCUMENT, no creation required!!! ----------");
                    }

                    else {

                        System.out.println("---------- ATTN: patient_home_page.java - Creating Document for " + todays_date + " in FireBaseDB!!! ----------");
                        userRef.collection("nutrients").document(todays_date).set(todays_nutrients);
                    }
                }
                else {
                    System.out.println("---------- ATTN: patient_home_page.java - ERROR. Possibly need to update ANDROID EMULATOR to get connected to Firestore again ----------");
                }
            }
        });

        // END OF NUTRIENT DATABASE STUFF****

        String TAG = "Token ID:";
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log
                        String msg = token;
                        Log.d(TAG, msg);
                    }
                });

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.homeId);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentId) {
                    Intent a = new Intent(getApplicationContext(), patient_search_centers_visit_page.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(a);
                    return true;
                } else if (id == R.id.homeId) {
                    return true;
                } else if (id == R.id.medicalHistId) {
                    Intent a = new Intent(getApplicationContext(), patient_medical_records_page.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(a);
                    return true;
                } else if (id == R.id.resourcesId) {
                    Intent a = new Intent(getApplicationContext(), patient_nutrition_page.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(a);
                    return true;
                } else if (id == R.id.profileId) {
                    Intent a = new Intent(getApplicationContext(), patient_profile_page.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(a);
                    return true;
                }
                return false;
            }
        });

        // Set up the RecyclerView for medications
        RecyclerView med_recycle_view = findViewById(R.id.recycler_view_home_page_medications);
        med_recycle_view.setLayoutManager(new LinearLayoutManager(this));

        //list of items
        FirebaseFirestore db = MyFirestore.getDBInstance();
        FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Get the correct database path
        CollectionReference patientMedRef = db.collection("users").document(currentUser.getUid()).collection("prescriptionsInfo");

        patientMedRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<PrescribedMedications> pMedications_list = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    PrescribedMedications pMedication = document.toObject(PrescribedMedications.class);
                    pMedications_list.add(pMedication);
                }
                Log.d("TAG", "Medications size: " + pMedications_list.size()); // Check the size of the clinics list
                // Set the adapter (you'll create and set the adapter in later steps)
                MyPrescribedMedAdapter myAdapter = new MyPrescribedMedAdapter(getApplicationContext(), pMedications_list);
                med_recycle_view.setAdapter(myAdapter);

               /* myAdapter.setOnItemClickListener(new MyPrescribedMedAdapter().OnItemClickListener() {
                    //this sends the user to the clinic's specific appointment page
                    @Override
                    public void onItemClick(int position, PrescribedMedications pMedications) {
                        // Handle the item click here
                        Intent intent = new Intent(patient_prescribed_med_page.this, patient_appointments_view.class);
                        startActivity(intent);
                    }
                });
*/
            }
            else {
                // Handle the error
                Log.e("TAG", "Error getting medications", task.getException());
            }
        });

        // Check for past appointments
        AppointmentManager appointmentManager = new AppointmentManager();
        appointmentManager.checkPassedAppointments(currentUser.getUid());

        // Set up the RecyclerView for appointments
        RecyclerView appointments_recycle_view = findViewById(R.id.recycler_view_home_page_appointment);
        appointments_recycle_view.setLayoutManager(new LinearLayoutManager(this));

        CollectionReference patientAppointmentDateRef = db.collection("users").document(currentUser.getUid()).collection("dates");
        Log.d("TAG", "Collection Reference: " + patientAppointmentDateRef.getPath());
        patientAppointmentDateRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Appointment> appointments = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    CollectionReference appointments_collection = patientAppointmentDateRef.document(document.getId()).collection("appointments");
                    appointments_collection.get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                DocumentReference appointmentDocumentReference = appointments_collection.document(document2.getId());
                                Log.d("TAG", "Document data: " + document2.getData());
                                Appointment appointment = document2.toObject(Appointment.class);
                                Log.d("TAG", "FINAL: " + appointment);
                                appointment.setDocumentPath(appointmentDocumentReference);
                                if (appointment.getComplete() == false) {
                                    appointments.add(appointment);
                                    Log.d("document path", appointment.getDocumentPath().toString());
                                }
                            }
                            MyUpcomingAppointmentsAdapter myAdapter = new MyUpcomingAppointmentsAdapter(getApplicationContext(), appointments, appointments_recycle_view);
                            appointments_recycle_view.setAdapter(myAdapter);
                        } else {
                            // Handle the error
                            Log.e("TAG", "Error getting appointments", task2.getException());
                        }
                    });
                }
            } else {
                // Handle the error
                Log.e("TAG", "Error getting appointments", task.getException());
            }
        });

        // Display current date
        dateFormat = (TextView) findViewById(R.id.date);

        String date;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        date = simpleDateFormat.format(calendar.getTime()).toString();
        dateFormat.setText(date);
        dateFormat.setPaintFlags(dateFormat.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

    }

    // AUTOMATIC LOADING
    public void onStart() {
        //run method to check past appointments


        super.onStart();
        // Automatically loading
        // Firestore wants to load things quickly, so it loads in locally before from the cloud
        // Save addSnapShotListener to noteListener, automatically detach/attach by adding this
        userListener = userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                // Error checking
                if(error != null)
                {
                    Toast.makeText(patient_home_page.this, "Error while loading!", Toast.LENGTH_LONG).show();
                    return;
                }
                //check if the user is a patient or a clinic
                if (documentSnapshot.exists()) {
                    // Document exists
                    // This will do the same work as the onLoad method
                    // But it is done automatically

                    // Set greeting
                    calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    TextView welcome = (TextView) findViewById(R.id.text_view_dashboard);

                    String greeting = "null";
                    if (hour >= 5 && hour < 12){
                        greeting = "Good morning, ";
                    } else if (hour >= 12 && hour < 17){
                        greeting = "Good afternoon, ";
                    } else {
                        greeting = "Good evening, ";
                    }
                    if (documentSnapshot.contains("location")) {
                    } else {
                        // Document doesn't have the specific element
                        // Perform your action here
                        String user_firstname = documentSnapshot.get("firstName").toString();
                        welcome.setText(greeting + user_firstname + ".");
                    }


                }
            }
        });
    }
}