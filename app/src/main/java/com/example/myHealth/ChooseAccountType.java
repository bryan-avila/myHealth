package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;


public class ChooseAccountType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account_type);
    }


    // OnClickerListener for Patient Button. Defined in .XML
    public void onPatientClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    // OnClickerListener for Caretaker Button. Defined in .XML

    public void onCaretakerClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), CaretakerSignUp.class);
        startActivity(intent);

    }
}