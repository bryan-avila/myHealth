package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
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

    // Set Up Chart Stuff
    BarChart barChart;
    ArrayList barNutrientList = new ArrayList();
    private List<String> xValues = Arrays.asList("Phosphorus", "Potassium", "Protein");

    // Set Up Date
    String todays_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    // Set up Patient Nutrient Variables
    public float i_patient_phosphorus, i_patient_potassium, i_patient_protein;
    public String str_patient_phosphorus, str_patient_potassium, str_patient_protein;

    // DB Stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    DocumentReference patientNutrientRef = db.collection("users").document(currentUser.getUid()).collection("nutrients").document(todays_date);
    DocumentReference patientRecNutrientValues = db.collection("users").document(currentUser.getUid()).collection("dietInfo").document("dietInfo");
    private ListenerRegistration userL;

    // Set up Nutrient Limit Text View stuff
    TextView rec_protein_amt;
    TextView rec_phos_amt;
    TextView rec_pot_amt;

    // Set up Global Limit Values
    float rec_protein_value;
    float rec_phos_value;
    float rec_pot_value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_food_charts_page);

        // ---------- START: Update the page's TextView recommended nutrient using this SnapShot Listener! ----------
        userL = patientRecNutrientValues.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                if(documentSnapshot.exists())
                {
                    Log.d("Rec Nutrient Vals", "--------- Successfully obtained recommended nutrient values ----------");

                    String str_rec_protein_amt = documentSnapshot.get("protein").toString();
                    rec_protein_amt = findViewById(R.id.text_view_rec_prot_limit);
                    rec_protein_amt.setText("Prot. Limit: " + str_rec_protein_amt);
                    String str_numeric_protein = str_rec_protein_amt.replaceAll("[^0-9]", ""); // Removes non-digit characters from the DB
                    rec_protein_value = (float) Float.parseFloat(str_numeric_protein); // Global Float Prot Value!

                    String str_rec_potas_amt = documentSnapshot.get("potassium").toString();
                    rec_pot_amt = findViewById(R.id.text_view_rec_potas_limit);
                    rec_pot_amt.setText("Potas. Limit: " + str_rec_potas_amt);
                    String str_numeric_potas = str_rec_potas_amt.replaceAll("[^0-9]", ""); // Removes non-digit characters from the DB
                    rec_pot_value = (float) Float.parseFloat(str_numeric_potas); // Global Float Potas Value!

                    String str_rec_phos_amt = documentSnapshot.get("phosphorus").toString();
                    rec_phos_amt = findViewById(R.id.text_view_rec_phos_limit);
                    rec_phos_amt.setText("Phos. Limit: " + str_rec_phos_amt);
                    String str_numeric_phos = str_rec_phos_amt.replaceAll("[^0-9]", ""); // Removes non-digit characters from the DB
                    rec_phos_value = (float) Float.parseFloat(str_numeric_phos); // Global Float Rec Phos Value!

                }

            }
        });

        // ---------- END OF: Update the page's TextView recommended nutrient using this SnapShot Listener! ----------


        // ---- START OF DB STUFF -----
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            // Grab the reference of the patients nutrients for todays_date
            DocumentReference patientNutrientRef = db.collection("users").document(currentUser.getUid()).collection("nutrients").document(todays_date);

            // Check if document exists
            patientNutrientRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            System.out.println("---------- ATTN: patient_view_food_charts_page.java - No issues loading patient nutrients todays date document to obtain current nutrient values ----------");
                        } else {
                            System.out.println("---------- ATTN: ERROR loading patient info??!!?! ----------");

                        }
                    } else {
                        // Handle failure to retrieve user document
                        System.out.println("---------- ATTN: Major issue. Check code!!!! ----------");
                    }
                }
            });
        }

        // Now that document exists, get the fields inside
        userL = patientNutrientRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                // Error checking
                if (error != null) {
                    Toast.makeText(patient_view_food_charts_page.this, "Error while loading!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Get the fields if document exists
                if (documentSnapshot.exists())
                {
                    System.out.println("---------- ATTN: Patient Today's Nutrients Snapshot exists ----------");
                    str_patient_phosphorus = documentSnapshot.get("phosphorus").toString();
                    String str_patient_potassium = documentSnapshot.get("potassium").toString();
                    String str_patient_protein = documentSnapshot.get("protein").toString();
                    i_patient_phosphorus = (float) Float.parseFloat(str_patient_phosphorus);
                    i_patient_potassium = (float) Float.parseFloat(str_patient_potassium);
                    i_patient_protein = (float) Float.parseFloat(str_patient_protein);


                    // Connect barchart to .xml
                    barChart = findViewById(R.id.bar_chart_todays_nutrients);

                    // Create float versions of 0,1,2
                    float zero = 0;
                    float one = 1;
                    float two = 2;

                    // Add entries to the barNutrientList
                    BarEntry phosEntry = new BarEntry(zero, i_patient_phosphorus);
                    barNutrientList.add(phosEntry);
                    BarEntry potassiumEntry = new BarEntry(one, i_patient_potassium);
                    barNutrientList.add(potassiumEntry);
                    BarEntry proteinEntry = new BarEntry(two, i_patient_protein);
                    barNutrientList.add(proteinEntry);

                    // Initialize Data Set
                    BarDataSet barNutrientSet = new BarDataSet(barNutrientList, "Nutrients");

                    // Customize data set according to if the user has reached the recommended limit
                    if(i_patient_protein >= rec_protein_value || i_patient_phosphorus >= rec_phos_value || i_patient_potassium >= rec_pot_value)
                    {
                        //TODO Set it so only the affected nutrient's bar is changed to Red?
                        barNutrientSet.setColors(Color.RED);
                        barNutrientSet.setDrawValues(false);
                        Toast.makeText(patient_view_food_charts_page.this, "CAUTION. YOU HAVE EXCEEDED ONE OR MORE NUTRIENT LIMITS", Toast.LENGTH_LONG).show();
                    }
                    else {
                        barNutrientSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        barNutrientSet.setDrawValues(false);
                        Toast.makeText(patient_view_food_charts_page.this, "You are under your recommended limits.", Toast.LENGTH_SHORT).show();
                    }

                    // Set Bar Data/Customize Bar Data
                    barChart.setData(new BarData(barNutrientSet));
                    barChart.getDescription().setEnabled(false);
                    barChart.invalidate();
                    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues)); // Give names to the bars
                    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChart.getXAxis().setGranularity(1f);
                    barChart.getXAxis().setGranularityEnabled(true);
                    barChart.animateY(2000);
                    barChart.getDescription().setText("Today's Nutrients");
                    barChart.getDescription().setTextColor(Color.MAGENTA);

                    // Display toast if user has yet to add any food for today, meaning all nutrients must be 0
                    if(i_patient_protein == 0 && i_patient_potassium == 0 && i_patient_phosphorus == 0)
                    {
                        Toast.makeText(patient_view_food_charts_page.this, "No food added yet.", Toast.LENGTH_LONG).show();
                    }
                }

                else {
                    // For whatever reason, the documentSnapShot does NOT exist. Abort and go to patient_home_page
                       startActivity(new Intent(getApplicationContext(), patient_home_page.class));
                }
            }
        });

        // ---- END OF DB STUFF -----


        // Create and modify date textview
        TextView header_date = findViewById(R.id.text_view_charts_todays_date);
        String date;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        date = simpleDateFormat.format(calendar.getTime()).toString();
        header_date.setText(date + "  Nutrient Chart");



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
}
