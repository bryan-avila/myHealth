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

// This is the Sign Up Activity page for Clinic

public class clinic_sign_up extends AppCompatActivity {

    EditText clinic_name, clinic_email, clinic_password_Entered, clinic_password_Confirm, clinic_phone_number, clinic_location;
    Button button_clinic_finish;
    AlertDialog.Builder builder;
    //gets instances of database connection and user authentication
    FirebaseFirestore db = myFirestore.getDBInstance();
    FirebaseAuth mAuth = myFirestore.getmAuthInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_sign_up);

        // Initialize objects
        clinic_name = findViewById(R.id.edit_text_clinic_name);
        clinic_email = findViewById(R.id.edit_text_clinic_email);
        clinic_password_Entered = findViewById(R.id.edit_text_clinic_password);
        clinic_password_Confirm = findViewById(R.id.edit_text_clinic_password_confirm);
        clinic_phone_number = findViewById(R.id.edit_text_clinic_phone_number);
        clinic_location = findViewById(R.id.edit_text_clinic_location);

        clinic_phone_number.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); // Phone number auto-format while inputting

        clinic_name.setHorizontallyScrolling(true); // Fixes line break issue
    }

    // OnClickerListener for Confirmation Button. Defined in .XML

    public void onCareCenterAwaitConfirmation(View view)
    {
        // Convert objects to string to pass them to the validation method
        String string_c_name = clinic_name.getText().toString();
        String string_c_email = clinic_email.getText().toString();
        String string_c_password = clinic_password_Entered.getText().toString();
        String string_c_password_confirm = clinic_password_Confirm.getText().toString();
        String string_c_phone_number = clinic_phone_number.getText().toString();
        String string_c_location = clinic_location.getText().toString();

        //Function for validation of inputted data
        boolean check_clinic_sign_up_info = validate_clinic_info(string_c_name, string_c_email, string_c_password, string_c_password_confirm, string_c_phone_number, string_c_location);

        if(check_clinic_sign_up_info) {
            // If approved, move to login screen waiting for approval
            Toast.makeText(clinic_sign_up.this, "APPROVED.", Toast.LENGTH_LONG).show();
            //adds the clinic account to the databases
            //uses firebase user authentication to save user login info
            mAuth.createUserWithEmailAndPassword(string_c_email, string_c_password)
                    .addOnCompleteListener(clinic_sign_up.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                //adds account to database
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                if (currentUser != null) {
                                    Log.d(TAG, "currentUser is not null");
                                    DocumentReference userRef = db.collection("clinic").document(currentUser.getUid());

                                    // Check if the user document already exists
                                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                Log.d(TAG, "task successful");
                                                if (!document.exists()) {
                                                    Log.d(TAG, "document doesnt exist");
                                                    // Create a new clinic
                                                    Map<String, Object> clinic = new HashMap<>();
                                                    clinic.put("clinicName", string_c_name);
                                                    clinic.put("email", string_c_email);
                                                    clinic.put("phone", string_c_phone_number);
                                                    clinic.put("location", string_c_location);

                                                    userRef.set(clinic)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // User document created successfully
                                                                    Log.d(TAG, "DocumentSnapshot added;");
                                                                    // Upon registration send users to do medical history questions
                                                                    Intent intent = new Intent(clinic_sign_up.this, clinic_home_page.class);
                                                                    startActivity(intent);
                                                                    finish(); // If you don't want to allow the user to go back to the registration screen
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
                            }
                        }
                    });
        } else {
            Toast.makeText(clinic_sign_up.this, "DISAPPROVED.", Toast.LENGTH_LONG).show();
        }
    }

    private Boolean validate_clinic_info (String c_name, String c_email, String c_passwordSignUp, String c_passwordSignUpConfirm, String c_phone_number, String c_location) {

        c_phone_number = c_phone_number.replaceAll("\\D+", "");

        if (c_name.length() == 0) {
            clinic_name.requestFocus();
            clinic_name.setError("FIELD CANNOT BE EMPTY");
            return false;
        }

        else if (!c_name.matches("[a-zA-Z ]+")) {
            clinic_name.requestFocus();
            clinic_name.setError("ENTER ONLY ALPHABETICAL CHARACTERS");
            return false;
        }

        else if (c_email.length() == 0) {
            clinic_email.requestFocus();
            clinic_email.setError("FIELD CANNOT BE EMPTY");
            return false;
        }
        else if (!c_email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            clinic_email.requestFocus();
            clinic_email.setError("INVALID EMAIL");
            return false;
        }
        else if (c_phone_number.length() == 0) {
            clinic_phone_number.requestFocus();
            clinic_phone_number.setError("FIELD CANNOT BE EMPTY");
            return false;
        }
        else if (c_phone_number.length() != 10) { //WOULD THIS MESS WITH FORMATING OF PHONE NUMBER WE WANT
            clinic_phone_number.requestFocus();
            clinic_phone_number.setError("INVALID PHONE NUMBER");
            return false;
        }
        else if (c_passwordSignUp.length() < 5) {
            clinic_password_Entered.requestFocus();
            clinic_password_Entered.setError("MINIMUM 6 CHARACTERS REQUIRED");
            return false;
        }
        else if (!c_passwordSignUpConfirm.equals(c_passwordSignUp)) {
            clinic_password_Confirm.requestFocus();
            clinic_password_Confirm.setError("PASSWORDS DON'T MATCH");
            return false;
        }
        else {
            return true;
        }
    }

}