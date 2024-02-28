package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class main_choose_account_type extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_choose_account_type);
    }

    //###################################################################################################################
    //NOTE!!!! THIS IS A CURRENTLY UNUSED FILE (ITS IMPLEMENTATION WAS COMMENTED OUT BELOW)
    //THIS PAGE IS NO LONGER NECESSARY FOR THE PROJECT
    //###################################################################################################################

    // OnClickerListener for Patient Button. Defined in .XML
    public void onPatientClick(View view)
    {
        //Intent intent = new Intent(getApplicationContext(), sign_up_activity.class);
        //startActivity(intent);
        //finish(); // cannot press back
    }

    // OnClickerListener for Caretaker Button. Defined in .XML

    public void onCaretakerClick(View view)
    {
        //Intent intent = new Intent(getApplicationContext(), clinic_sign_up_page.class);
        //startActivity(intent);
        //finish(); // cannot press back

    }
}