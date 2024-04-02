package com.example.myHealth;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

// This is the Sign Up Activity page for Patients

public class edit_profile_activity extends AppCompatActivity {

    EditText firstName, lastName, email, currentPasswordEntered, passwordEntered, passwordConfirm, phoneNumber;
    Button confirmButton;
    AlertDialog.Builder builder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference docRef = db.collection("users").document(currentUser.getUid());

    // @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        //gets instances of database connection and user authentication
        FirebaseFirestore db = MyFirestore.getDBInstance();
        FirebaseAuth mAuth = MyFirestore.getmAuthInstance();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        currentPasswordEntered = findViewById(R.id.currentPasswordEntered);
        passwordEntered = findViewById(R.id.passwordEntered);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        confirmButton = findViewById(R.id.confirmButton);
        phoneNumber = findViewById(R.id.phoneNumber);

        // Display current fields
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        firstName.setText(document.getString("firstName"));
                        lastName.setText(document.getString("lastName"));
                        email.setText(document.getString("email"));
                        phoneNumber.setText(document.getString("phone"));
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        email.setFocusable(false);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); // Phone number auto-format while inputting
        firstName.setHorizontallyScrolling(true); // Fixes line break issue
        lastName.setHorizontallyScrolling(true); // "
        builder = new AlertDialog.Builder(this);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameConfirm = firstName.getText().toString();
                String lastNameConfirm = lastName.getText().toString();
                String emailConfirm = email.getText().toString();
                String currentPasswordConfirm = currentPasswordEntered.getText().toString();
                String newPasswordConfirm = passwordEntered.getText().toString();
                String newPasswordRepeatConfirm = edit_profile_activity.this.passwordConfirm.getText().toString();
                String phoneConfirm = phoneNumber.getText().toString();
                String currentEmail = currentUser.getEmail();


                boolean check = validateInfo(firstNameConfirm, lastNameConfirm, emailConfirm, newPasswordConfirm, newPasswordRepeatConfirm, phoneConfirm, currentPasswordConfirm);

                if (currentPasswordConfirm.equals("")) {
                    currentPasswordEntered.requestFocus();
                    currentPasswordEntered.setError("FIELD CANNOT BE EMPTY");
                }

                else if (check) {
                    AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, currentPasswordConfirm);
                    currentUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Update account info in firestore database
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("firstName", firstNameConfirm);
                                        user.put("lastName", lastNameConfirm);
                                        user.put("email", emailConfirm);
                                        user.put("phone", phoneConfirm);
                                        docRef.set(user);
                                        boolean exit = true;

                                        // Check if user wants to change password
                                        if (!TextUtils.isEmpty(newPasswordConfirm) || !TextUtils.isEmpty(newPasswordRepeatConfirm)) {
                                            boolean success = false;
                                            exit = false;
                                            while (!success) {
                                                if (newPasswordConfirm.equals(newPasswordRepeatConfirm)) {
                                                    Log.d(TAG, "Password updated, signing out.");
                                                    builder.setTitle("Password updated, please log-in again. ✔️")
                                                            .setCancelable(false)
                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    mAuth.signOut();
                                                                    Intent intent = new Intent(getApplicationContext(), account_type_login_checker.class);
                                                                    startActivity(intent);
                                                                    finish(); // cannot press back
                                                                }
                                                            }).show();
                                                    success = true;
                                                }
                                                else {
                                                    passwordEntered.requestFocus();
                                                    passwordConfirm.requestFocus();
                                                }
                                            }
                                        }
                                        if (exit) {
                                            Toast.makeText(edit_profile_activity.this, "INFO UPDATED ✔️", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(edit_profile_activity.this, patient_home_page.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else {
                                        Log.d(TAG, "Error: authentication failed");
                                        currentPasswordEntered.requestFocus();
                                        currentPasswordEntered.setError("INCORRECT PASSWORD");
                                    }
                                }
                            });
                }
            }
        });
    }
    private Boolean validateInfo (String firstNameSignUp, String lastNameSignUp, String emailSignUp, String passwordSignUp, String passwordSignUpConfirm, String phoneSignUp, String currentPasswordConfirm) {
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
        else if (passwordSignUp.length() > 0 && passwordSignUp.length() < 5) {
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
