package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomNavBar extends AppCompatActivity {

    //Number of selected tab. We have 5 so value should lie between 1-5 Home page is selected by default
    private int selectedTab = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_nav_bar);

        final LinearLayout homeLayout = findViewById(R.id.homeLayout);
        final LinearLayout appointmentLayout = findViewById(R.id.appointmentLayout);
        final LinearLayout chatLayout = findViewById(R.id.chatLayout);
        final LinearLayout notificationLayout = findViewById(R.id.notificationLayout);
        final LinearLayout profileLayout = findViewById(R.id.profileLayout);

        final ImageView homeImage = findViewById(R.id.homeImage);
        final ImageView appointmentImage = findViewById(R.id.appointmentImage);
        final ImageView chatImage = findViewById(R.id.chatImage);
        final ImageView notificationImage = findViewById(R.id.notificatioImage);
        final ImageView profileImage = findViewById(R.id.profileImage);

        final TextView hometxt = findViewById(R.id.hometxt);
        final TextView appointmenttxt = findViewById(R.id.appointmenttxt);
        final TextView chattxt = findViewById(R.id.chattxt);
        final TextView notificationtxt = findViewById(R.id.notificationtxt);
        final TextView profiletxt = findViewById(R.id.profiletxt);

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        appointmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        notificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}