package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class patient_view_food_charts_page extends AppCompatActivity {

    // variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList barNutrientEntries;
    float i_patient_phosphorus;
    Number i_patient_potassium;
    Number i_patient_protein;
    String todays_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    // DB Stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    DocumentReference patientNutrientRef = db.collection("users").document(currentUser.getUid()).collection("nutrients").document(todays_date);
    private ListenerRegistration userL;
    private List<String> xValues = Arrays.asList("Phosphorus", "Potassium", "Protein");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_food_charts_page);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Grab the reference of the patients nutrients for todays_date
            DocumentReference patientNutrientRef = db.collection("users").document(currentUser.getUid()).collection("nutrients").document(todays_date);

            // Check if it exists
            patientNutrientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            System.out.println("ATTN: No issues loading patient document.");
                        } else {
                            System.out.println("ATTN: ERROR loading patient info?");

                        }
                    } else {
                        // Handle failure to retrieve user document
                        System.out.println("ATTN: Major issue. Check code.");
                    }
                }
            });
        }

        super.onStart();
        // Automatically loading
        // Firestore wants to load things quickly, so it loads in locally before from the cloud
        // Save addSnapShotListener to noteListener, automatically detach/attach by adding this
        userL = patientNutrientRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                // Error checking
                if (error != null) {
                    Toast.makeText(patient_view_food_charts_page.this, "Error while loading!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot.exists()) {
                    //TODO Make sure that nutrient values are added over time when adding food in patient_add_food_page.java!
                    String str_patient_phosphorus = documentSnapshot.get("phosphorus").toString();
                    String str_patient_potassium = documentSnapshot.get("potassium").toString();
                    String str_patient_protein = documentSnapshot.get("protein").toString();
                    i_patient_phosphorus = (float) Float.parseFloat(str_patient_phosphorus);

                    //TODO It does grab data correctly
                    Toast.makeText(patient_view_food_charts_page.this, str_patient_phosphorus + " aha", Toast.LENGTH_LONG).show();

                }
            }
        });


        // ------- BAR CHART SET UP ---------
            // initializing variable for bar chart.
            barChart = findViewById(R.id.chart);
            barChart.getAxisLeft().setDrawLabels(false);

            // Control the Left Y Axis Line
            YAxis y = barChart.getAxisLeft();
            y.setAxisLineWidth(1f);
            y.setAxisLineColor(Color.BLACK);
            y.setGranularity(1f); // Controls how the labels look when zooming in
            y.setLabelCount(6); // Controls label entries for the y axis

            // creating a new array list
            barNutrientEntries = new ArrayList<>();
             int zero = 0;

            //TODO How to add data to barchart as variables so that x:, y: appears
//            barNutrientEntries.add(new BarEntry(0, 800));
              BarEntry barEntryPhos = new BarEntry(zero, i_patient_phosphorus);
              barNutrientEntries.add(barEntryPhos);

           // creating a new bar data set.
            barDataSet = new BarDataSet(barNutrientEntries, "Nutrients");

            // creating a new bar data and
            // passing our bar data set.
            barData = new BarData(barDataSet);

            // below line is to set data
            // to our bar chart.
            barChart.setData(barData);

            // adding color to our bar data set.
            barDataSet.setColors(Color.BLUE);

            // setting text color.
            barDataSet.setValueTextColor(Color.BLACK);

            // Control the X Axis and change names
            barChart.getDescription().setEnabled(false);
            barChart.invalidate();
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues)); // Give names to the bars
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setGranularityEnabled(true);

            //----------- END OF BAR CHART SETUP ------------

            // Create and modify header textview
        TextView header_message = findViewById(R.id.text_view_charts_header_message);
        header_message.setPaintFlags(header_message.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        header_message.setText("Today's Charts");

        // Create and modify date textview
        TextView header_date = findViewById(R.id.text_view_charts_todays_date);
        String date;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        date = simpleDateFormat.format(calendar.getTime()).toString();
        header_date.setText(date);



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

//    public void onStart() {
//
//            FirebaseUser currentUser = mAuth.getCurrentUser();
//            if (currentUser != null) {
//                // Grab the reference of the patients nutrients for todays_date
//                DocumentReference patientNutrientRef = db.collection("users").document(currentUser.getUid()).collection("nutrients").document(todays_date);
//
//                // Check if it exists
//                patientNutrientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                System.out.println("ATTN: No issues loading patient document.");
//                            } else {
//                                System.out.println("ATTN: ERROR loading patient info?");
//
//                            }
//                        } else {
//                            // Handle failure to retrieve user document
//                            System.out.println("ATTN: Major issue. Check code.");
//                        }
//                    }
//                });
//            }
//
//            super.onStart();
//            // Automatically loading
//            // Firestore wants to load things quickly, so it loads in locally before from the cloud
//            // Save addSnapShotListener to noteListener, automatically detach/attach by adding this
//            userL = patientNutrientRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//
//                    // Error checking
//                    if (error != null) {
//                        Toast.makeText(patient_view_food_charts_page.this, "Error while loading!", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//
//                    if (documentSnapshot.exists()) {
//                       //TODO Make sure that nutrient values are added over time when adding food in patient_add_food_page.java!
//                        String str_patient_phosphorus = documentSnapshot.get("phosphorus").toString();
//                        String str_patient_potassium = documentSnapshot.get("potassium").toString();
//                        String str_patient_protein = documentSnapshot.get("protein").toString();
//
//                        Toast.makeText(patient_view_food_charts_page.this, str_patient_phosphorus, Toast.LENGTH_LONG).show();
//
//                        i_patient_phosphorus = Integer.decode(str_patient_phosphorus);
//                        i_patient_potassium = Integer.decode(str_patient_potassium);
//                        i_patient_protein = Integer.decode(str_patient_protein);
//
//                    }
//                }
//            });
//    }
}
