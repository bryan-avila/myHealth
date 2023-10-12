package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomNavBar extends AppCompatActivity {

    //Number of selected tab. We have 5 so value should lie between 1-5 Home page is selected by default
    private int selectedTab = 1;
    TextView dateTimeDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_nav_bar);

        String dateTime;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;

        dateTimeDisplay = (TextView) findViewById(R.id.text_date_display);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MMMM MM, yyyy HH:mm:ss aaa");
        dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        dateTimeDisplay.setText(dateTime);


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

        final Button LogoutButton = findViewById(R.id.LogoutButton);


        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = myFirestore.getmAuthInstance();
                mAuth.signOut();

                // Start the LoginActivity (or any other activity you want to go to)
                Intent intent = new Intent(CustomNavBar.this, MainActivity.class);
                startActivity(intent);

                // Optionally, finish the current activity to prevent the user from going back to it
                finish();
            }
        });

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if home is already selected
                if (selectedTab != 1) {
                    //unselect other tabs expect home tab
                    appointmenttxt.setVisibility(View.GONE);
                    chattxt.setText(View.GONE);
                    notificationtxt.setVisibility(View.GONE);
                    profiletxt.setVisibility(View.GONE);

                    //might need to change the icons around here
                    appointmentImage.setImageResource(R.drawable.make_appoint);
                    chatImage.setImageResource(R.drawable.chat_icon);
                    notificationImage.setImageResource(R.drawable.notifications);
                    profileImage.setImageResource(R.drawable.account);

                    //Deprecated thing here
                    //appointmentLayout.setBackgroundColor(getResources().getColor(ContextCompat.getColor(this, R.color.transparent));

                    //Select home tab
                    hometxt.setVisibility(View.VISIBLE);
                    homeImage.setImageResource(R.drawable.home_icon_selected);
                    homeLayout.setBackgroundResource(R.drawable.home_button_back);

                    //Create animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f,1f, 1f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    homeLayout.startAnimation(scaleAnimation);

                    //Set 1st tab as selected
                    selectedTab = 1;
                }

            }
        });

        appointmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if appointment is already selected
                if (selectedTab != 1) {
                    //unselect other tabs expect appointment tab
                    hometxt.setVisibility(View.GONE);
                    chattxt.setText(View.GONE);
                    notificationtxt.setVisibility(View.GONE);
                    profiletxt.setVisibility(View.GONE);

                    //might need to change the icons around here
                    homeImage.setImageResource(R.drawable.home_icon);
                    chatImage.setImageResource(R.drawable.chat_icon );
                    notificationImage.setImageResource(R.drawable.notifications);
                    profileImage.setImageResource(R.drawable.account);

                    //Deprecated thing here
                    //appointmentLayout.setBackgroundColor(getResources().getColor(ContextCompat.getColor(this, R.color.transparent));

                    //Select appointment tab
                    appointmenttxt.setVisibility(View.VISIBLE);
                    appointmentImage.setImageResource(R.drawable.make_appoint_selected);
                    appointmentLayout.setBackgroundResource(R.drawable.appointment_button_back);

                    //Create animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f,1f, 1f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    homeLayout.startAnimation(scaleAnimation);

                    //Set 1st tab as selected
                    selectedTab = 1;
                }

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