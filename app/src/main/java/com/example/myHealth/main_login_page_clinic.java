package com.example.myHealth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class main_login_page_clinic extends AppCompatActivity {
    TextView login_text;
    EditText email;
    EditText password;
    Button loginButton_clinic;
    Button signUpBtn_clinic;

    // testing 1 2 3
    // testing github yoooo
    // test

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login_page_clinic);

        // Obtain information from account_type_login_checker.java
        /*Bundle bundle = getIntent().getExtras();
        String profile_type = bundle.getString("profile");

        // Change the greeting depending on profile type
        if(profile_type.equals("patient"))
        {
            login_text = findViewById(R.id.loginText);
            login_text.setText("Patient Login");
        }

        else if(profile_type.equals("clinic"))
        {
            login_text = findViewById(R.id.loginText);
            login_text.setText("Clinic Login");
        }

        else {
            Toast.makeText(this, "Error Finding Profile Type?", Toast.LENGTH_LONG).show();
        }*/

        login_text = findViewById(R.id.loginText);
        login_text.setText("Clinic Login");

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton_clinic = findViewById(R.id.loginButton_clinic);
        signUpBtn_clinic = findViewById(R.id.registerButton_clinic);

        email.setHorizontallyScrolling(true);

        loginButton_clinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
                // Check if Email or Password are empty
                if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(main_login_page_clinic.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(main_login_page_clinic.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        // If they entered an email with clinic.com, then they must be a Clinic user. Send to clinic home page
                                        if (user.getEmail().contains("@clinic.com")) {
                                            Intent intent = new Intent(main_login_page_clinic.this, clinic_home_page.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(intent);
                                            finish(); // cannot press back
                                        } else {
                                            Log.w(TAG, "Invalid Credentials", task.getException());
                                            Toast.makeText(main_login_page_clinic.this, "Invalid Credentials.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(main_login_page_clinic.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        // This is the code to go to choose account type page view
        signUpBtn_clinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we need to send user to two different pages depending on what the user is
                Intent intent = new Intent(getApplicationContext(), clinic_sign_up_page.class);
                startActivity(intent);
            }
        });
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        //this checks if there is currently a user signed in already
//        FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if(currentUser != null){
//            // If you log in successfully once, it stays logged in even after you delete a user from the Firebase console.
//            // Work around this issue by preventing this if statement
//
//            // If the user created an email account that contains clinic.com, they will automatically
//            // be logged in as a clinic user
//            if(currentUser.getEmail().contains("@clinic.com")) {
//                Intent intent = new Intent(main_login_page.this, clinic_home_page.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//                finish();
//            }
//
//            // Else, they will be logged in as a patient user
//            else {
//                Intent intent = new Intent(main_login_page.this, patient_home_page.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//                finish();
//            }
//        }
//    }
}