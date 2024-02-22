package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class patient_dietary_plan_page extends AppCompatActivity {

    String food_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_dietary_plan);

        TextView food_text = findViewById(R.id.text_view_food_db_test);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform network operations here
                try {
                    Log.d("start", "start");
                    // USDA FoodData Central API endpoint for food search
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

                    // Parse the JSON response
                    JSONParser parser = new JSONParser();
                    JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());

                    // Access the list of foods
                    JSONArray foods = (JSONArray) jsonResponse.get("foods");
                    for (Object food : foods) {
                        JSONObject foodObject = (JSONObject) food;
                        System.out.println("Food Name: " + foodObject.get("description"));
                        food_name = "Food Name: " + foodObject.get("description");
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