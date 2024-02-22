import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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

// Likely Do Not Need This Java************************
public class MyAsyncTask extends Activity {
    private String my_url = "https://api.nal.usda.gov/fdc/v1/foods/search?query=apple&api_key=DEMO_KEY"; // Your API endpoint

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up your UI components
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Execute the AsyncTask when needed (e.g., button click)
        new myasynctask().execute();
    }

    private class myasynctask extends AsyncTask<Void, Void, String> {
        ProgressDialog pdLoading = new ProgressDialog(MyAsyncTask.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Loading..."); // Show a progress dialog
            pdLoading.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // Perform your network tasks here (e.g., HTTP request)
            // Access the parent class's variable 'url' if needed
            // Return the result (e.g., JSON response)try {
                        // USDA FoodData Central API endpoint for food search

            try{
                        // Create a URL object
                        URL url = new URL(my_url);

                        // Open a connection to the URL
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        // Set request method
                        connection.setRequestMethod("GET");

                        // Get the response code
                        int responseCode = connection.getResponseCode();
                        System.out.println("Response Code: " + responseCode);

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
                            String food_name = (String) foodObject.get("description");
                            Log.d("Food Name: ", food_name);
                            // Access other properties as needed
                        }

                        // Disconnect the connection
                        connection.disconnect();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            return "Result from background task";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdLoading.dismiss(); // Dismiss the progress dialog
            // Handle the result (e.g., update UI components)
        }
    }
}
