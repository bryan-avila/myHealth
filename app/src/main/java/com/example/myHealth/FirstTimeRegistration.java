package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FirstTimeRegistration extends AppCompatActivity {

    // TO DO:
    // 1. Create collections specific to a user who has recently signed up that will hold all the info
    // 2. Store the user input here and upload them to firestore
    // 3. Make this page more aesthetically pleasing
    // 4. After medical history q&a is finished, send them to the home page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_registration);
    }
}