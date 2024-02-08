package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class patient_appointments_view extends AppCompatActivity {

    CalendarView calendarview;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_page);

        // Underline Select Date text
        TextView selectDate = (TextView) findViewById(R.id.loginText);
        selectDate.setPaintFlags(selectDate.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        // Create a calendarView object
        calendarview = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();
        Clinic clinic;

        // Retrieve the data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            clinic = (Clinic) intent.getSerializableExtra("clinicData");

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
        } else {
            clinic = null;
        }
        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                // Toast message displaying date MM/DD/YYYY
               Toast.makeText(patient_appointments_view.this, (month + 1) + "/" + day +"/" + year, Toast.LENGTH_LONG).show();
                String selectedDate = year + "-" + (month + 1) + "-" + day;
                Log.d("TAG", "selectedDate:" + selectedDate);
                Log.d("TAG", "clinic id:" + clinic.getID());


                AppointmentManager appointmentManager = new AppointmentManager();
                appointmentManager.availableDayTimes(clinic.getID(), selectedDate, new OnDataReadyListener<ArrayList<Boolean>>() {
                    @Override
                    public void onDataReady(ArrayList<Boolean> availabilityList) {
                        appointmentManager.dayTimes(clinic.getID(), selectedDate, new OnDataReadyListener<ArrayList<String>>() {
                            @Override
                            public void onDataReady(ArrayList<String> timesList) {
                                // Now timesList is ready, and you can proceed with creating the intent
                                ArrayList<String> availableTimesList = new ArrayList<>();
                                for (int i = 0; i < timesList.size(); i++) {
                                    if (availabilityList.get(i) == true) {
                                        availableTimesList.add(timesList.get(i));
                                    }
                                }

                                Intent intent = new Intent(patient_appointments_view.this, pick_appointment_time.class);
                                intent.putExtra("clinicData", clinic);
                                intent.putExtra("selectedDate", selectedDate);
                                intent.putExtra("availableTimesList", availableTimesList);
                                startActivity(intent);
                            }
                        });
                    }
                });
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
                    startActivity(new Intent(getApplicationContext(), patient_home_page.class)); //check this line it might be wrong
                    finish();
                    return true;
                } else if (id == R.id.medicalHistId) {
                    startActivity(new Intent(getApplicationContext(), patient_medical_records_page.class));
                    finish();
                    return true;
                } else if (id == R.id.resourcesId) {
                    startActivity(new Intent(getApplicationContext(), patient_nutrition_page.class));
                    finish();
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
        Toast.makeText(patient_appointments_view.this, selected_date, Toast.LENGTH_SHORT).show();

    }
}