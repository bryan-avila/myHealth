package com.example.myHealth;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog.Builder;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText firstName, lastName, email, passwordEntered, passwordConfirm, phoneNumber;
    Button signupButton;
    AlertDialog.Builder builder;

    // @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        FirebaseFirestore db = myFirestore.getInstance();

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
                    // ADD ACCOUNT DETAILS TO DATABASE HERE !!!!!!!!!!!!!
                    builder.setTitle("Registration Complete! ✅")
                            .setMessage("You may now login using your account credentials.")
                            .setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .show();
                    // Create a new user with a first and last name
                    Map<String, Object> user = new HashMap<>();
                        user.put("firstName", firstNameSignUp);
                        user.put("lastName", lastNameSignUp);
                        user.put("Email", emailSignUp);
                        user.put("password", passwordSignUp);
                        user.put("phone", phoneSignUp);

                    // Add a new document with a generated ID
                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                } else {
                    Toast.makeText(SignUpActivity.this, "SIGN UP FAILED. TRY AGAIN", Toast.LENGTH_SHORT).show();
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
