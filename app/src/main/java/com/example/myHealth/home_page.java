package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class home_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Adding tab functionality to bottom navigation tab
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    //check id
                }
                return false;
            }
        });

        //Set home as selected
        /*bottomNavigationView.setSelectedItemId(R.id.homeId);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                }
                return false;
            }
        });*/








        //Log out button
        final Button LogoutButton = findViewById(R.id.LogoutButton);

        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = myFirestore.getmAuthInstance();
                mAuth.signOut();

                // Start the LoginActivity (or any other activity you want to go to)
                Intent intent = new Intent(home_page.this, login_page.class);
                startActivity(intent);

                // Optionally, finish the current activity to prevent the user from going back to it
                finish();
            }
        });
    }
}