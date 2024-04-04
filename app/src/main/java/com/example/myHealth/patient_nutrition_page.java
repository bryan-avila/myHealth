package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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

                // When a recycler view card is clicked, grab the food's nutrient values by going to the "nutrients" collection!
                myFoodsAddedAdapter.setOnItemClickListener(new MyFoodsAddedAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, FoodsAdded foodsAddedList) {

                        patientFoodAddedRef.document(foodsAddedList.getFoodName().toString()).collection("nutrients").document("thisNutrients").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                String foodNameForPopUP = foodsAddedList.getFoodName().toString(); // save name for the Pop Up

                                if (task.isSuccessful())
                                {
                                    DocumentSnapshot this_food_nutrient_values = task.getResult();
                                    String phos_value = "";
                                    String protein_value = "";
                                    String potassium_value = "";

                                    if(this_food_nutrient_values.exists()) // if the "thisNutrients" document exists for that food added today then grab the values from the Database and send them to the Pop UP function!
                                    {
                                        // First check if these objects are not null (that means that adding the food didn't change a nutrient value)
                                        // If these objects are not null, THEN we can create strings of the nutrient values!
                                        Object obj_phos_value = this_food_nutrient_values.get("phosphorus");

                                        if(obj_phos_value != null)
                                        {
                                            phos_value = this_food_nutrient_values.get("phosphorus").toString();
                                        }

                                        Object obj_protein_value = this_food_nutrient_values.get("protein");

                                        if(obj_protein_value != null)
                                        {
                                             protein_value = this_food_nutrient_values.get("protein").toString();
                                        }

                                        Object obj_potassium_value = this_food_nutrient_values.get("potassium");

                                        if(obj_potassium_value != null)
                                        {
                                             potassium_value = this_food_nutrient_values.get("potassium").toString();
                                        }

                                        if(obj_phos_value == null || obj_protein_value == null || obj_potassium_value == null) // Was there a problem getting nutrients?
                                        {
                                            Toast.makeText(patient_nutrition_page.this, "Error obtaining nutrient value from DB", Toast.LENGTH_LONG).show();
                                        }

                                        else {
                                            showPopUp(foodNameForPopUP, phos_value, potassium_value, protein_value);
                                        }
                                       // TextView test = findViewById(R.id.text_view_card_phosphorus);
                                        //String value = "Phos : " + this_food_nutrient_values.get("phosphorus").toString();
                                        //test.setText(value); // should print 37
                                        //test.setVisibility(View.VISIBLE);
                                        //value = value + " , Protein: " + this_food_nutrient_values.get("protein").toString();
                                        //value = value + " , Potas: " + this_food_nutrient_values.get("potassium").toString();
                                       // String value = "Phos: " + phos_value + " Prot: " + protein_value + " Potas: " + potassium_value + " !";
                                        //Toast.makeText(patient_nutrition_page.this, value + "!", Toast.LENGTH_SHORT).show();

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

    // Create a Pop-Up for food nutrients
    public void showPopUp(String food_name, String string_phos, String string_potassium, String string_protein)
    {
        // Set the pop up window title using the food name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(food_name + " Nutrients: ");

        // Grab the .xml elements and show the values
        View popUp = getLayoutInflater().inflate(R.layout.custom_food_added_nutrient_layout, null);
        TextView phos = popUp.findViewById(R.id.text_view_recycler_card_food_added_phos);
        TextView prot = popUp.findViewById(R.id.text_view_recycler_card_food_added_prot);
        TextView potassium = popUp.findViewById(R.id.text_view_recycler_card_food_added_pot);
        phos.setText("Phosphorus Amount: " + string_phos);
        prot.setText("Protein Amount: " + string_protein);
        potassium.setText("Potassium Amount: " + string_potassium);

        builder.setView(popUp);

        // Create an exit button
        builder.setPositiveButton("Exit Nutrient Pop-Up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Don't really need this so ignore
            }
        });

        builder.show(); // Show the pop up!

    }
}