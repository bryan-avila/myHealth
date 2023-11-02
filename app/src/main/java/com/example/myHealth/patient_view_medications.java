package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class patient_view_medications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_medications);


    }


    public void onDetailsClick(View view)
    {
        startActivity(new Intent(getApplicationContext(), medication1_details.class));
    }
}