package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class first_time_sign_up_diet_survey extends AppCompatActivity {


    // TO DO:
    // 1. Do input validation
    // 2. Store information in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_sign_up_diet_survey);
    }


    // onClick listener using .xml declartion onClick="onDietButtonClick"
    public void onDietButtonClick(View view)
    {
        // Once they finish inputting diet, send them home
        Intent intent = new Intent(first_time_sign_up_diet_survey.this, home_page.class);
        startActivity(intent);
        finish();

    }
}