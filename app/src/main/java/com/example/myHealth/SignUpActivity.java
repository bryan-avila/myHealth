package com.example.myHealth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    EditText firstName, lastName, email, username, passwordEntered, passwordConfirm, phoneNumber;
    Button signupButton;

    // @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        passwordEntered = findViewById(R.id.passwordEntered);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        signupButton = findViewById(R.id.signupButton);
        phoneNumber = findViewById(R.id.phoneNumber);

        //Test commit here!:)
        firstName.setHorizontallyScrolling(true);
        lastName.setHorizontallyScrolling(true);
        username.setHorizontallyScrolling(true);

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

                if (check == true) {
                    Toast.makeText(SignUpActivity.this, "SIGN UP SUCCESSFUL", Toast.LENGTH_SHORT).show(); //WE WOULD NEED TO ADVANCE USER TO HOME PAGE HERE

                } else {
                    Toast.makeText(SignUpActivity.this, "SIGN UP FAILED. TRY AGAIN", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Boolean validateInfo (String firstNameSignUp, String lastNameSignUp, String emailSignUp, String passwordSignUp, String passwordSignUpConfirm, String phoneSignUp) {
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
        else  if (!phoneSignUp.matches("^[+]?[0-9]{10}$")) { //WOULD THIS MESS WITH FORMATING OF PHONE NUMBER WE WANT
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
