package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class account_type_login_checker extends AppCompatActivity {


    //***This is the first page a person sees when they first open the app, or are logged out***

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type_login_checker);
    }

    public void onPatientClickChecker(View view) {
        Intent intent = new Intent(getApplicationContext(), main_login_page.class);
        // Send information to main_login_page.class
        String profile_type = "patient";
        intent.putExtra("profile", profile_type);
        startActivity(intent);

        //finish(); // cannot press back
    }

    public void onCaretakerClickChecker(View view) {
        Intent intent = new Intent(getApplicationContext(), main_login_page_clinic.class);
        // Send information to main_login_page_clinic.class
        String profile_type = "clinic";
        intent.putExtra("profile", profile_type);
        startActivity(intent);

        //finish(); // cannot press back
    }

    // If someone has already logged in before and closes the app
    // This method ensures they are taken to their specific home_page and can skip this page
    public void onStart() {
        super.onStart();
        //this checks if there is currently a user signed in already
        FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            // If you log in successfully once, it stays logged in even after you delete a user from the Firebase console.
            // Work around this issue by preventing this if statement

            // If the user created an email account that contains clinic.com, they will automatically
            // be logged in as a clinic user
            if(currentUser.getEmail().contains("@clinic.com"))
            {
                Intent intent = new Intent(account_type_login_checker.this, clinic_home_page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // Send information to main_login_page
                String profile_type = "clinic";
                intent.putExtra("profile", profile_type);
                startActivity(intent);
                finish();
            }

            // Else, they will be logged in as a patient user
            else
            {
                Intent intent = new Intent(account_type_login_checker.this, patient_home_page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // Send information to main_login_page
                String profile_type = "patient";
                intent.putExtra("profile", profile_type);
                startActivity(intent);
                finish();
            }
        }
    }
}