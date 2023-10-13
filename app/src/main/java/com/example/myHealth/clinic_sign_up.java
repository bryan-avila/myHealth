package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

// This is the Sign Up Activity page for Clinic

public class clinic_sign_up extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_sign_up);
    }

    // OnClickerListener for Confirmation Button. Defined in .XML

    public void onCareCenterAwaitConfirmation(View view)
    {
        Intent intent = new Intent(getApplicationContext(), login_page.class);
        startActivity(intent);
        finish();

    }
}