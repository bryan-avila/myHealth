package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import org.json.simple.JSONArray;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class patient_add_food_page extends AppCompatActivity {

    // Set up DB Stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference userRef = db.collection("users").document(currentUser.getUid());

    // Set Up List of Food Names
    private List<FoodNameFromList> foodNames = null;

    // Set up RecyclerView/SearchView Stuff
    SearchView filterView;
    MyFoodListAdapter foodListAdapter;

    ImageButton favorites_Btn;

    // Global stuff for updating documents
    String todays_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    double phosAmount, proteinAmount;
    double potassiumAmount;
    double currentPhosAmount, currentProteinAmount, currentPotassiumAmount;

    // Set up document reference for the document snapshot
    DocumentReference patientNutrientRef = db.collection("users").document(currentUser.getUid()).collection("nutrients").document(todays_date);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_food_page);

        // Set up Bottom Nav Bar
        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set resources selected, the resourcesID, AKA the diet page
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

        // Set up the RecyclerView to the one in patient_add_food_page.xml
        RecyclerView recyclerView = findViewById(R.id.recycler_view_food_names);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize an empty list of food names
        foodNames = new ArrayList<>();
        FoodNameFromList f = new FoodNameFromList(""); // Create a new FoodNameFromList object with food_name as the argument
        foodNames.add(f); // Add to list
        // Set the Adapter of the page with the list created in this thread
        foodListAdapter = new MyFoodListAdapter(getApplicationContext(), foodNames);
        recyclerView.setAdapter(foodListAdapter);

        // Set up filterview
        filterView = findViewById(R.id.search_view_food_names);
        filterView.clearFocus();

        filterView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // This method works whenever the user enters text into the SearchView and presses ENTER.
            // A lot of things happen when a user clicks on an item, so a lot of indentations....
            @Override
            public boolean onQueryTextSubmit(String user_text)
            {
                // Create Thread! Start of thread ****
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Perform network operations here
                        try {
                            Log.d("Thread 1", "---------- Starting 1st Thread ----------");

                            String endpoint = "https://api.nal.usda.gov/fdc/v1/foods/search?query=" + user_text + "&api_key=hIXmsCYannc5plOrGfwlqSZkuUsBAznpJEgxtz5T";


                            // Create a URL object
                            URL url = new URL(endpoint);

                            // Open a connection to the URL
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            Log.d("Thread 1 Connection", "1st Establishing connection...");

                            // Set request method
                            connection.setRequestMethod("GET");
                            Log.d("Thread 1 Get", "Thread 1 Get Success");

                            // Get the response code
                            int responseCode = connection.getResponseCode();
                            Log.d("Thread 1 Code", "Thread 1 code is " + responseCode);

                            // Read the response
                            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String line;

                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            reader.close();

                            JSONParser parser = new JSONParser();
                            JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());

                            // Initialize an empty list of food names
                            foodNames = new ArrayList<>();

                            // Access the list of foods
                            JSONArray foods = (JSONArray) jsonResponse.get("foods");

                            // For all foods in the array, get the food's description and food ID
                            // Place them in foodNames ArrayList
                            for (Object food : foods) {

                                JSONObject foodObject = (JSONObject) food;
                                String food_name = (String) foodObject.get("description");
                                String food_id = foodObject.get("fdcId").toString();

                                FoodNameFromList f = new FoodNameFromList(food_name); // Create a new FoodNameFromList object
                                f.setFood_id(food_id); // Set the ID
                                foodNames.add(f); // Add to global list of FoodNames
                            }

                            // Affect Android UI Elements, such as updating the Adapter and setting an onItemClickListener
                            runOnUiThread(() -> {

                                // Set the Adapter of the page with the list created in this thread
                                foodListAdapter = new MyFoodListAdapter(getApplicationContext(), foodNames);

                                // Let the food names that appear in the recycler view become clickable!
                                foodListAdapter.setOnItemClickListener(new MyFoodListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, FoodNameFromList food_names) {

                                        // When you click on a food, store the food_id in this string
                                        String current_food_id = food_names.getFood_id().toString();

                                        // Using a document reference, we need to grab the patient's CURRENT nutrient values and update them with the
                                        // food values which they just clicked on!
                                        patientNutrientRef.get().addOnCompleteListener(task ->
                                        {

                                            if(task.isSuccessful())
                                            {
                                                DocumentSnapshot todays_date_doc = task.getResult();

                                                    // If today's date document exists, then create a thread and set the CURRENT nutrient amounts found in the DB to the global variables
                                                    if(todays_date_doc.exists())
                                                    {
                                                        // Create a new thread so that you may grab the nutrient information of the food you clicked!

                                                        //************************START OF 2ND THREAD**********************************************

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                try {

                                                                    // Grab the current fields found in the document
                                                                    currentPhosAmount = todays_date_doc.getDouble("phosphorus");
                                                                    currentProteinAmount = todays_date_doc.getDouble("protein");
                                                                    currentPotassiumAmount = todays_date_doc.getLong("potassium");

                                                                    Log.d("Thread 2", "---------- Starting 2nd Thread ----------");

                                                                    // Grab the specific food's nutrient info with this URL using the food id
                                                                    String endpoint = "https://api.nal.usda.gov/fdc/v1/food/" + current_food_id + "?nutrients=305&api_key=hIXmsCYannc5plOrGfwlqSZkuUsBAznpJEgxtz5T";

                                                                    // Create a URL object
                                                                    URL url = new URL(endpoint);

                                                                    // Open a connection to the URL
                                                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                                                    Log.d("Thread 2 Connection", "2nd Establshing connection...");

                                                                    // Set request method
                                                                    connection.setRequestMethod("GET");
                                                                    Log.d("Thread 2 Get", "Thread 2 Get Success");

                                                                    // Get the response code
                                                                    int responseCode = connection.getResponseCode();
                                                                    Log.d("Thread 2 Code", "Thread 2 Code is " + responseCode);

                                                                    // Read the response
                                                                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                                                    StringBuilder response = new StringBuilder();
                                                                    String line;

                                                                    while ((line = reader.readLine()) != null) {
                                                                        response.append(line);
                                                                    }
                                                                    reader.close();

                                                                    JSONParser parser = new JSONParser();
                                                                    JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());

                                                                    // Access food's array of nutrients
                                                                    JSONArray nutrientsArray = (JSONArray) jsonResponse.get("foodNutrients");

                                                                    int food_nutrient_counter = 0;
                                                                    // Iterate through the array of nutrients
                                                                    for (Object nutrientObj : nutrientsArray)
                                                                    {
                                                                        food_nutrient_counter = food_nutrient_counter + 1;
                                                                        JSONObject nutrient = (JSONObject) nutrientObj;
                                                                        JSONObject nutrientInfo = (JSONObject) nutrient.get("nutrient");

                                                                        if(food_nutrient_counter > 16) // only need to go up to the first 16 nutrients to find the three we want
                                                                        {
                                                                            break;
                                                                        }

                                                                        //TODO Some food items seem to not update DB, possibly due to data type checking ?
                                                                        //TODO Logcat shows that threads are running twice, possibly due to for loop structure ?
                                                                        //************************GET PHOSPHORUS VALUES********************************
                                                                        if(nutrientInfo.get("name").equals("Phosphorus, P"))
                                                                        {
                                                                            if(nutrient.get("amount") instanceof Double)
                                                                            {
                                                                                //System.out.print(nutrientInfo.get("name") + " is of type DOUBLE!!!!!!!!");

                                                                                // Update phosAmount
                                                                                phosAmount = (double) nutrient.get("amount");
                                                                                // Add phosAmount with the phos value currently found in the document
                                                                                phosAmount = (double) (phosAmount + currentPhosAmount);
                                                                                // Push it to the DB
                                                                                userRef.collection("nutrients").document(todays_date).update("phosphorus", phosAmount);
                                                                            }

                                                                            else if(nutrient.get("amount") instanceof Long)
                                                                            {
                                                                               // System.out.print(nutrientInfo.get("name") + " is of type LONG!!!!!!!");

                                                                                // Update phosAmount
                                                                                phosAmount = (long) nutrient.get("amount");
                                                                                // Add phosAmount with the phos value currently found in the document
                                                                                phosAmount = (long) (phosAmount + currentPhosAmount);
                                                                                // Push it to the DB
                                                                                userRef.collection("nutrients").document(todays_date).update("phosphorus", phosAmount);
                                                                            }

                                                                            Log.d("Happy Days!!!", "Added phos in for loop counter " + food_nutrient_counter + ".");

                                                                        }

                                                                        //************************GET PROTEIN VALUES********************************
                                                                        else if(nutrientInfo.get("name").equals("Protein"))
                                                                        {
                                                                            if(nutrientInfo.get("name") instanceof Double)
                                                                            {

                                                                                // Update proteinAmount
                                                                                proteinAmount = (double) nutrient.get("amount");
                                                                                // Add proteinAmount with the protein value currently found in the document
                                                                                proteinAmount = (double) (proteinAmount + currentProteinAmount);
                                                                                // Push it to the DB
                                                                                userRef.collection("nutrients").document(todays_date).update("protein", proteinAmount);
                                                                            }

                                                                            else if(nutrientInfo.get("name") instanceof Long)
                                                                            {
                                                                                // Update proteinAmount
                                                                                proteinAmount = (long) nutrient.get("amount");
                                                                                // Add proteinAmount with the protein value currently found in the document
                                                                                proteinAmount = (long) (proteinAmount + currentProteinAmount);
                                                                                // Push it to the DB
                                                                                userRef.collection("nutrients").document(todays_date).update("protein", proteinAmount);

                                                                            }

                                                                            else {

                                                                                // Update proteinAmount
                                                                                proteinAmount = (double) nutrient.get("amount");
                                                                                // Add proteinAmount with the protein value currently found in the document
                                                                                proteinAmount = (double) (proteinAmount + currentProteinAmount);
                                                                                // Push it to the DB
                                                                                userRef.collection("nutrients").document(todays_date).update("protein", proteinAmount);
                                                                            }

                                                                            Log.d("Happy Days!!!", "---------- Added protein in for loop counter " + food_nutrient_counter + " ----------");

                                                                        }

                                                                        //************************GET POTASSIUM VALUES********************************
                                                                        else if(nutrientInfo.get("name").equals("Potassium, K"))
                                                                        {
                                                                            if(nutrientInfo.get("name") instanceof Double)
                                                                            {
                                                                                // Update potassiumAmount
                                                                                potassiumAmount = (double) nutrient.get("amount");
                                                                                // Add potassiumAmount with the potassium value currently found in the document
                                                                                potassiumAmount = (double) (potassiumAmount + currentPotassiumAmount);
                                                                                // Push it to the DB
                                                                                userRef.collection("nutrients").document(todays_date).update("potassium", potassiumAmount);
                                                                            }
                                                                            else if(nutrientInfo.get("name") instanceof Long)
                                                                            {
                                                                                // Update potassiumAmount
                                                                                potassiumAmount = (long) nutrient.get("amount");
                                                                                // Add potassiumAmount with the potassium value currently found in the document
                                                                                potassiumAmount = (long) (potassiumAmount + currentPotassiumAmount);
                                                                                // Push it to the DB
                                                                                userRef.collection("nutrients").document(todays_date).update("potassium", potassiumAmount);
                                                                            }
                                                                            else {

                                                                                // Update potassiumAmount
                                                                                potassiumAmount = (long) nutrient.get("amount");
                                                                                // Add potassiumAmount with the potassium value currently found in the document
                                                                                potassiumAmount = (long) (potassiumAmount + currentPotassiumAmount);
                                                                                // Push it to the DB
                                                                                userRef.collection("nutrients").document(todays_date).update("potassium", potassiumAmount);
                                                                            }

                                                                            Log.d("Happy Days!!!", "Added Potassium in for loop counter " + food_nutrient_counter + ".");

                                                                        }

                                                                        // ---------- You have reached the end of adding new nutrient values and updating nutrient values! Hooray! ----------
                                                                        // ---------- You have reached the end of adding new nutrient values and updating nutrient values! Hooray! ----------
                                                                        // ---------- You have reached the end of adding new nutrient values and updating nutrient values! Hooray! ----------
                                                                    }

                                                                    // Disconnect the connection of the thread and get out of here!!
                                                                    connection.disconnect();

                                                                }
                                                                catch (Exception e) {
                                                                    e.printStackTrace();
                                                                    Log.d("Sad Days", "Couldn't add nutrient!!");
                                                                }
                                                            }
                                                        }).start();
                                                    }
                                                    else
                                                    {
                                                        // file doesnt exist?
                                                    }
                                            }
                                            else {
                                                // error fetching doc?
                                            }

                                        });

                                        Toast.makeText(patient_add_food_page.this, "Successfully added food " + ("\u2705"), Toast.LENGTH_LONG).show();
                                    }

                                    /*
                                    public void onFavoriteFoodCheck(View view) {
                                        //Send the name of the food to a collection that holds the favorite foods

                                    }*/
                                });
                               foodListAdapter.getFilter().filter(user_text); // Filter based off the text in the search view
                                recyclerView.setAdapter(foodListAdapter);
                            });

                            // Disconnect the connection
                            connection.disconnect();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("sad", "didnt work");
                        }
                    }
                }).start();

                //************************END OF 2ND THREAD**********************************************
                return true;

            }


            // This method grabs text as the user is currently typing them and filters accordingly. Currently not required?
            @Override
            public boolean onQueryTextChange(String newText)
            {
                foodListAdapter.getFilter().filter(newText); // Filter based off the text in the search view

                // Let the food names be clickable
                foodListAdapter.setOnItemClickListener(new MyFoodListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, FoodNameFromList food_names) {

                        String food_id = food_names.getFood_id().toString();
                        // ignore this for now??
                        Toast.makeText(patient_add_food_page.this, "ERROR. Make sure you enter a food name and then press ENTER.", Toast.LENGTH_LONG).show();

                    }

                });
                return true;
            }
        });

    }
}