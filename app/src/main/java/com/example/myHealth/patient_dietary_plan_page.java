package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class patient_dietary_plan_page extends AppCompatActivity {

    String food_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_dietary_plan);

        TextView food_text = findViewById(R.id.text_view_food_db_test);

        // TESTING FOR USDA Database API********
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform network operations here
                try {

                    // Grab the specific food's nutrient info with this URL
                    String endpoint = "https://api.nal.usda.gov/fdc/v1/food/2344788?nutrients=305&api_key=hIXmsCYannc5plOrGfwlqSZkuUsBAznpJEgxtz5T";


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

                    // Access food's array of nutrients
                    JSONArray nutrientsArray = (JSONArray) jsonResponse.get("foodNutrients");

                    // Iterate through the array
                    for (Object nutrientObj : nutrientsArray)
                    {
                        JSONObject nutrient = (JSONObject) nutrientObj;
                        JSONObject nutrientInfo = (JSONObject) nutrient.get("nutrient");

                        System.out.println("Nutrient name: " + nutrientInfo.get("name"));

                        // Match nutrient names
                        if(nutrientInfo.get("name").equals("Vitamin A, RAE"))
                        {
                            //TODO Get Potassium working
                            double phosAmount = (double) nutrient.get("amount");
                            System.out.println(phosAmount + " aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaah");
                            food_name = "Pot Amount: " + phosAmount;
                        }

                        Log.d("happy", "worked");
                        // Access other properties as needed
                    }

                    runOnUiThread(() -> {
                                // Update the TextView text
                               food_text.setText(food_name);
                            });

                    // Disconnect the connection
                    connection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("sad", "didnt work");
                }
            }
        }).start();
    }
}