package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class account_type_login_checker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type_login_checker);
    }

    public void onPatientClickChecker(View view) {
        Intent intent = new Intent(getApplicationContext(), main_login_page.class);
        startActivity(intent);
        //finish(); // cannot press back
    }

    public void onCaretakerClickChecker(View view) {
        Intent intent = new Intent(getApplicationContext(), main_login_page.class);
        startActivity(intent);
        //finish(); // cannot press back
    }
}