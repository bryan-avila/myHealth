package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class appointments_page extends AppCompatActivity {

    CalendarView calendarview;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_page);

        // Create a calendarView object
        calendarview = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();

        // Retrieve the data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            Clinic clinic = (Clinic) intent.getSerializableExtra("clinicData");

            // Now you can use 'clinic' to update your UI or perform other operations
            if (clinic != null) {
                TextView clinicTextView = findViewById(R.id.clinicText);
                TextView emailTextView = findViewById(R.id.emailText);
                TextView locationTextView = findViewById(R.id.locationText);
                TextView phoneTextView = findViewById(R.id.phoneText);

                clinicTextView.setText(clinic.getClinicName());
                emailTextView.setText(clinic.getEmail());
                locationTextView.setText(clinic.getLocation());
                phoneTextView.setText(clinic.getPhone());

                Log.d("TAG", "clinic id:" + clinic.getID());
            }
        }

        // Might not need this pop up
        /*// Pop-Up Window Intialization
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_example, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        PopupWindow pWindow = new PopupWindow(popupView, width, height, focusable);*/

        //You can force a date to appear in the calendar with the following code
        //setDate(10,31,2023);

        // Get user's CURRENT date
        //getDate();

        // This function will display toast message when user clicks on a calendar date
        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            PopupWindow popupWindow;
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                // Toast message displaying date MM/DD/YYYY
               Toast.makeText(appointments_page.this, (month + 1) + "/" + day +"/" + year, Toast.LENGTH_LONG).show();
                String selectedDate = year + "-" + (month + 1) + "-" + day;

               // popup window that allows patient to pick time
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                // Initialize a PopupWindow
                popupWindow = new PopupWindow(appointments_page.this);
                // Set the width and height of the PopupWindow to MATCH_PARENT
                popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                // Inflate your custom popup layout
                View popupView = LayoutInflater.from(appointments_page.this).inflate(R.layout.appointment_popup, null);
                // Set the content view of the PopupWindow
                popupWindow.setContentView(popupView);
                // Find the close button in your popup layout
                ImageButton closeButton = popupView.findViewById(R.id.closeButton);
                // Set a click listener for the close button
                closeButton.setOnClickListener(view -> {
                    // Dismiss the popup when the close button is clicked
                    popupWindow.dismiss();
                });
                // Show the PopupWindow at a specific location
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);


            }
        });

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set appointment selected
        bottomNavigationView.setSelectedItemId(R.id.appointmentId);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentId) {
                    return true;
                } else if (id == R.id.homeId) {
                    startActivity(new Intent(getApplicationContext(), home_page.class)); //check this line it might be wrong
                    finish();
                    return true;
                } else if (id == R.id.medicalHistId) {
                    startActivity(new Intent(getApplicationContext(), medical_records_page.class));
                    finish();
                    return true;
                } else if (id == R.id.resourcesId) {
                    startActivity(new Intent(getApplicationContext(), patient_diet_page.class));
                    finish();
                    return true;
                } else if (id == R.id.profileId) {
                    startActivity(new Intent(getApplicationContext(), profile_page.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    // Function for setting a specified date
    public void setDate(int month, int day, int year)
    {
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);
        long date = calendar.getTimeInMillis();
        calendarview.setDate(date);
    }



    // Function for getting a date without users needing to click on the calendar
    public void getDate()
    {
        long thisDate = calendarview.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        String selected_date = simpleDateFormat.format(calendar.getTime());
        Toast.makeText(appointments_page.this, selected_date, Toast.LENGTH_SHORT).show();

    }
}