package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class patient_nutrition_page extends AppCompatActivity {
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

    String todays_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    CollectionReference patientFoodAddedRef = db.collection("users").document(currentUser.getUid()).collection("foodsAdded").document(todays_date).collection("foodsAddedToday");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_nutrition_page);

        // Create and modify header to have date
        header_and_date = findViewById(R.id.text_view_patient_nutrition_page_text);
        String date;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        date = simpleDateFormat.format(calendar.getTime()).toString();
        header_and_date.setText("Nutrition - " + date);

        // Set up the RecyclerView for medications
        RecyclerView food_added_recycler_view = findViewById(R.id.recycler_view_foods_added_today);
        food_added_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        // Set up RecyclerView
        CollectionReference patientFoodAddedRef = db.collection("users").document(currentUser.getUid()).collection("foodsAdded").document(todays_date).collection("foodsAddedToday");
        patientFoodAddedRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                List<FoodsAdded> foodsAddedList = new ArrayList<>();
                // Obtain the document, which are named the food added that day
                for (QueryDocumentSnapshot document : task.getResult())
                {
                    FoodsAdded food_added_doc = document.toObject(FoodsAdded.class);
                    foodsAddedList.add(food_added_doc);
                }
                Log.d("TAG", "---------- Today's Foods Added is of size: " + foodsAddedList.size()); // Check the size of the foods list
                MyFoodsAddedAdapter myFoodsAddedAdapter = new MyFoodsAddedAdapter(getApplicationContext(), foodsAddedList);
                food_added_recycler_view.setAdapter(myFoodsAddedAdapter);

                myFoodsAddedAdapter.setOnItemClickListener(new MyFoodsAddedAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, FoodsAdded foodsAddedList) {

                        patientFoodAddedRef.document(foodsAddedList.getFoodName().toString()).collection("nutrients").document("thisNutrients").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    DocumentSnapshot this_food_nutrient_values = task.getResult();
                                    if(this_food_nutrient_values.exists())
                                    {
                                        //TODO: Make it so when you click on a recyclerview card, it updates like when you check appointments
                                        String value = "Phos : " + this_food_nutrient_values.get("phosphorus").toString();
                                        value = value + " , Protein: " + this_food_nutrient_values.get("protein").toString();
                                        value = value + " , Potas: " + this_food_nutrient_values.get("potassium").toString();
                                        Toast.makeText(patient_nutrition_page.this, value + ", Okay?", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });
                    }
                });

            }
        });


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
}