package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class home_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Adding tab functionality to bottom navigation tab
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.homeId);








        //Log out button
        final Button LogoutButton = findViewById(R.id.LogoutButton);

        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = myFirestore.getmAuthInstance();
                mAuth.signOut();

                // Start the LoginActivity (or any other activity you want to go to)
                Intent intent = new Intent(home_page.this, MainActivity.class);
                startActivity(intent);

                // Optionally, finish the current activity to prevent the user from going back to it
                finish();
            }
        });
    }
}