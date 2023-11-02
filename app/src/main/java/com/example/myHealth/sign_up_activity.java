package com.example.myHealth;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

// This is the Sign Up Activity page for Patients

public class sign_up_activity extends AppCompatActivity {

    EditText firstName, lastName, email, passwordEntered, passwordConfirm, phoneNumber;
    Button signupButton;
    AlertDialog.Builder builder;

    // @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //gets instances of database connection and user authentication
        FirebaseFirestore db = myFirestore.getDBInstance();
        FirebaseAuth mAuth = myFirestore.getmAuthInstance();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        passwordEntered = findViewById(R.id.passwordEntered);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        signupButton = findViewById(R.id.signupButton);
        phoneNumber = findViewById(R.id.phoneNumber);

        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); // Phone number auto-format while inputting

        firstName.setHorizontallyScrolling(true); // Fixes line break issue
        lastName.setHorizontallyScrolling(true); // "

        builder = new AlertDialog.Builder(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstNameSignUp = firstName.getText().toString();
                String lastNameSignUp = lastName.getText().toString();
                String emailSignUp = email.getText().toString();
                String passwordSignUp = passwordEntered.getText().toString();
                String passwordSignUpConfirm = passwordConfirm.getText().toString();
                String phoneSignUp = phoneNumber.getText().toString();

                //Function for validation of inputted data
                boolean check = validateInfo (firstNameSignUp, lastNameSignUp, emailSignUp, passwordSignUp, passwordSignUpConfirm, phoneSignUp);

                if (check) {
                    //uses firebase user authentication to save user login info
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), passwordEntered.getText().toString())
                            .addOnCompleteListener(sign_up_activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        //adds account to database
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        if (currentUser != null) {
                                            Log.d(TAG, "currentUser is not null");
                                            DocumentReference userRef = db.collection("users").document(currentUser.getUid());

                                            // Check if the user document already exists
                                            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        Log.d(TAG, "task successful");
                                                        if (!document.exists()) {
                                                            Log.d(TAG, "document doesn't exist");
                                                            // Create a new user with a first and last name
                                                            Map<String, Object> user = new HashMap<>();
                                                            user.put("firstName", firstNameSignUp);
                                                            user.put("lastName", lastNameSignUp);
                                                            user.put("email", emailSignUp);
                                                            user.put("phone", phoneSignUp);

                                                            userRef.set(user)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            // User document created successfully
                                                                            Log.d(TAG, "DocumentSnapshot added;");
                                                                            // Upon registration send users to do medical history questions
                                                                            builder.setTitle("Almost done! ✅")
                                                                                    .setMessage("Please fill out medical history form & diet form to finish registration.")
                                                                                    .setCancelable(false)
                                                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                                            Intent intent = new Intent(getApplicationContext(), first_time_medical_survey_page.class);
                                                                                            startActivity(intent);
                                                                                            finish();
                                                                                        }
                                                                                    })
                                                                                    .show();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            // Handle failure to create user document
                                                                            Log.w(TAG, "Error adding document", e);
                                                                        }
                                                                    });
                                                        }
                                                    } else {
                                                        Log.d(TAG, "document exists");
                                                        // Handle failure to check if user document exists
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        builder.setTitle("Account creation failed! ❌")
                                                .setMessage("The email you used is already linked to an existing account.")
                                                .setCancelable(false)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        // Don't need this finish() since it will just load the next view twice
                                                        //finish();
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(sign_up_activity.this, "SIGN UP FAILED. TRY AGAIN", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Boolean validateInfo (String firstNameSignUp, String lastNameSignUp, String emailSignUp, String passwordSignUp, String passwordSignUpConfirm, String phoneSignUp) {
        phoneSignUp = phoneSignUp.replaceAll("\\D+", "");
        if (firstNameSignUp.length() == 0) {
            firstName.requestFocus();
            firstName.setError("FIELD CANNOT BE EMPTY");
            return false;
        }
        else if (!firstNameSignUp.matches("[a-zA-Z]+")) {
            firstName.requestFocus();
            firstName.setError("ENTER ONLY ALPHABETICAL CHARACTERS");
            return false;
        }
        if (lastNameSignUp.length() == 0) {
            lastName.requestFocus();
            lastName.setError("FIELD CANNOT BE EMPTY");
            return false;
        }
        else if (!lastNameSignUp.matches("[a-zA-Z]+")) {
            lastName.requestFocus();
            lastName.setError("ENTER ONLY ALPHABETICAL CHARACTERS");
            return false;
        }
        else if (emailSignUp.length() == 0) {
            email.requestFocus();
            email.setError("FIELD CANNOT BE EMPTY");
            return false;
        }
        else if (!emailSignUp.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            email.requestFocus();
            email.setError("INVALID EMAIL");
            return false;
        }
        else if (phoneSignUp.length() == 0) {
            phoneNumber.requestFocus();
            phoneNumber.setError("FIELD CANNOT BE EMPTY");
            return false;
        }
        else if (phoneSignUp.length() != 10) { //WOULD THIS MESS WITH FORMATING OF PHONE NUMBER WE WANT
            phoneNumber.requestFocus();
            phoneNumber.setError("INVALID PHONE NUMBER");
            return false;
        }
        else if (passwordSignUp.length() < 5) {
            passwordEntered.requestFocus();
            passwordEntered.setError("MINIMUM 6 CHARACTERS REQUIRED");
            return false;
        }
        else if (!passwordSignUp.equals(passwordSignUpConfirm)) {
            passwordConfirm.requestFocus();
            passwordConfirm.setError("PASSWORDS DON'T MATCH");
            return false;
        }
        else {
            return true;
        }
    }
}
