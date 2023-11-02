package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class clinic_home_page extends AppCompatActivity {
    TextView dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_home_page);


        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.homeIdCLinic);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentIdClinic) {
                    startActivity(new Intent(getApplicationContext(), appointments_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.homeIdCLinic) {
                    return true;
                } else if (id == R.id.medicalHistIdClinic) {
                    startActivity(new Intent(getApplicationContext(), medical_records_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.resourcesIdClinic) {
                    startActivity(new Intent(getApplicationContext(), resources_page_clinic.class));
                    finish();
                    return true;
                } else if (id == R.id.profileIdClinic) {
                    startActivity(new Intent(getApplicationContext(), profile_page_clinic.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

        // Display current date
        dateFormat = (TextView) findViewById(R.id.date);

        String date;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("'Today is': MMMM dd, yyyy");
        date = simpleDateFormat.format(calendar.getTime()).toString();
        dateFormat.setText(date);

    }
}
