package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class clinic_prescription_form extends AppCompatActivity {

    EditText medName, medDosage, medFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_prescription_form);


        medName = findViewById(R.id.medNameEdit);
        medDosage = findViewById(R.id.medDosageEdit);
        medFrequency = findViewById(R.id.medFrequencyEdit);

    }

    public void onSubmitPrescriptionClick (View view) {

        String string_med_Name = medName.getText().toString();
        String string_med_Dosage = medDosage.getText().toString();
        String string_med_Frequency = medFrequency.getText().toString();

        boolean inputCheckerOnMedication = validateMedInput(string_med_Name, string_med_Dosage, string_med_Frequency);

        if (inputCheckerOnMedication) {
            Toast.makeText(clinic_prescription_form.this, "Medication Prescribed.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), clinic_view_prescriptions.class));
        } else {
            Toast.makeText(clinic_prescription_form.this, "Please fill out any empty fields.", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validateMedInput (String med_Name, String med_Dosage, String med_Frequency) {
        if (med_Name.length() == 0) {
            medName.requestFocus();
            medName.setError("FIELD CANNOT BE EMPTY");
            return false;
        } else if (med_Dosage.length() == 0) {
            medDosage.requestFocus();
            medDosage.setError("FIELD CANNOT BE EMPTY");
            return false;
        }  else if (med_Frequency.length() == 0) {
            medFrequency.requestFocus();
            medFrequency.setError("FIELD CANNOT BE EMPTY");
            return false;
        } else {
            return true;
        }
    }
}