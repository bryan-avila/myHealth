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
import android.widget.PopupWindow;
import org.json.simple.JSONArray;
import android.util.Log;
import android.widget.SearchView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class patient_add_food_page extends AppCompatActivity {

    // Set Up List of Food Names
    private List<FoodNameFromList> foodNames = null;

    // Set up RecyclerView/SearchView Stuff
    SearchView filterView;

    MyFoodListAdapter foodListAdapter;

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

        // Create a thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform network operations here
                try {
                    Log.d("start", "start");

                    //TODO!!! Hardcoded example. Need to figure out a way to get user input from searchview and change the "apple" to whatever food they type
                    String endpoint = "https://api.nal.usda.gov/fdc/v1/foods/search?query=apple&api_key=DEMO_KEY";


                    // Create a URL object
                    URL url = new URL(endpoint);

                    // Open a connection to the URL
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    Log.d("middle", "middle");

                    // Set request method
                    connection.setRequestMethod("GET");
                    Log.d("get", "success");

                    // Get the response code
                    int responseCode = connection.getResponseCode();
                    System.out.println("Response Code: " + responseCode);
                    Log.d("code", "code" + responseCode);

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
                    for (Object food : foods) {
                        JSONObject foodObject = (JSONObject) food;
                        //food_name = "Food Name: " + foodObject.get("description");
                        System.out.println("Food Name: " + foodObject.get("description"));
                        String food_name = (String) foodObject.get("description"); // Get the food_name by converting the foodObject to a String
                        FoodNameFromList f = new FoodNameFromList(food_name); // Create a new FoodNameFromList object with food_name as the argument
                        foodNames.add(f); // Add to list
                        Log.d("happy", "worked");
                        // Access other properties as needed
                    }

                    // Affect Android UI Elements
                    runOnUiThread(() -> {
                        // Set the Adapter of the page with the list created in this thread
                        foodListAdapter = new MyFoodListAdapter(getApplicationContext(), foodNames);
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

        // Set the searchview to the one from patient_add_food_page.xml
        filterView = findViewById(R.id.search_view_food_names);
        filterView.clearFocus();
        filterView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                foodListAdapter.getFilter().filter(newText); // Filter based off the text in the search view
                //TODO!! Make food name items clickable
                return true;
            }
        });

    }
}