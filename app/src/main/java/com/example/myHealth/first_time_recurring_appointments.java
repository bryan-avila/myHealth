package com.example.myHealth;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class first_time_recurring_appointments extends AppCompatActivity {
    Clinic clinic;
    String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_recurring_appointments);

        // Retrieve the data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            clinic = (Clinic) intent.getSerializableExtra("clinicData");
        } else {
            clinic = null;
        }
    }

    public void onSkipRecurringButtonClick(View view)
    {
        Intent intent = new Intent(first_time_recurring_appointments.this, home_page.class);
        startActivity(intent);
        finish();
    }

    public void onMondayButtonClick(View view)
    {
        day = "monday";
        Log.d("TAG", "selectedDay:" + day);
        AppointmentManager appointmentManager = new AppointmentManager();
        appointmentManager.availableRecurringDayTimes(clinic.getID(), day, new OnDataReadyListener<ArrayList<Boolean>>() {
            @Override
            public void onDataReady(ArrayList<Boolean> availabilityList) {
                appointmentManager.dayTimes(clinic.getID(), day, new OnDataReadyListener<ArrayList<String>>() {
                    @Override
                    public void onDataReady(ArrayList<String> timesList) {
                        // Now timesList is ready, and you can proceed with creating the intent
                        ArrayList<String> availableTimesList = new ArrayList<>();
                        for (int i = 0; i < timesList.size(); i++) {
                            if (availabilityList.get(i) == true) {
                                availableTimesList.add(timesList.get(i));
                            }
                        }

                        Intent intent = new Intent(first_time_recurring_appointments.this, pick_recurring_appointment_time.class);
                        intent.putExtra("clinicData", clinic);
                        intent.putExtra("dayData", day);
                        intent.putExtra("availableTimesList", availableTimesList);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public void onTuesdayButtonClick(View view)
    {
        day = "tuesday";
        Log.d("TAG", "selectedDay:" + day);
        Intent intent = new Intent(first_time_recurring_appointments.this, pick_recurring_appointment_time.class);
        intent.putExtra("clinicData", clinic);
        intent.putExtra("dayData", day);
        startActivity(intent);

    }

    public void onWednesdayButtonClick(View view)
    {
        day = "wednesday";
        Log.d("TAG", "selectedDay:" + day);
        Intent intent = new Intent(first_time_recurring_appointments.this, pick_recurring_appointment_time.class);
        intent.putExtra("clinicData", clinic);
        intent.putExtra("dayData", day);
        startActivity(intent);

    }

    public void onThursdayButtonClick(View view)
    {
        day = "thursday";
        Log.d("TAG", "selectedDay:" + day);
        Intent intent = new Intent(first_time_recurring_appointments.this, pick_recurring_appointment_time.class);
        intent.putExtra("clinicData", clinic);
        intent.putExtra("dayData", day);
        startActivity(intent);

    }

    public void onFridayButtonClick(View view)
    {
        day = "friday";
        Log.d("TAG", "selectedDay:" + day);
        Intent intent = new Intent(first_time_recurring_appointments.this, pick_recurring_appointment_time.class);
        intent.putExtra("clinicData", clinic);
        intent.putExtra("dayData", day);
        startActivity(intent);

    }

    public void onSaturdayButtonClick(View view)
    {
        day = "saturday";
        Log.d("TAG", "selectedDay:" + day);
        Intent intent = new Intent(first_time_recurring_appointments.this, pick_recurring_appointment_time.class);
        intent.putExtra("clinicData", clinic);
        intent.putExtra("dayData", day);
        startActivity(intent);

    }

    public void onSundayButtonClick(View view)
    {
        day = "sunday";
        Log.d("TAG", "selectedDay:" + day);
        Intent intent = new Intent(first_time_recurring_appointments.this, pick_recurring_appointment_time.class);
        intent.putExtra("clinicData", clinic);
        intent.putExtra("dayData", day);
        startActivity(intent);

    }

}
