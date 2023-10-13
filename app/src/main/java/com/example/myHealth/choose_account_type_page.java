package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class choose_account_type_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account_type);
    }


    // OnClickerListener for Patient Button. Defined in .XML
    public void onPatientClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), sign_up_activity.class);
        startActivity(intent);
        finish();
    }

    // OnClickerListener for Caretaker Button. Defined in .XML

    public void onCaretakerClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), clinic_sign_up.class);
        startActivity(intent);
        finish();

    }
}