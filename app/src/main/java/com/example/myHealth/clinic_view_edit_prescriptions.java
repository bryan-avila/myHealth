package com.example.myHealth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class clinic_view_edit_prescriptions extends AppCompatActivity {

    EditText medDosageEdited;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private ListenerRegistration clinicListener;

    // Set Up Global String Variables for DB access
    String prescriptionNameFromBundle;
    String patientIdFromBundle;

    // Set Up Global Variables for DB fields
    String dosageUnitsEdited = "";
    String frequencyEdited = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_view_edit_prescriptions);
        medDosageEdited = findViewById(R.id.edit_text_med_Dosage_edited);

        // Obtain information from clinic_view_select_patient_to_manage_meds.java
        Bundle bundle = getIntent().getExtras();
        patientIdFromBundle = bundle.getString("pat_name"); // this returns a string version of the patId stroed in the DB
        prescriptionNameFromBundle = bundle.getString("presc_name"); // this returns a string version of the prescriptionName stored in the DB
    }

    public void onDosageSelectClickEdit(View view) {
        //Make dropdown menu appear when clicked
        PopupMenu dosage_popup = new PopupMenu(this, view);
        dosage_popup.setOnMenuItemClickListener(this::onMenuItemClick2);
        dosage_popup.inflate((R.menu.menu_dosage));
        dosage_popup.show();
    }

    public boolean onMenuItemClick2(MenuItem item) {
        if(item.getItemId() == R.id.item_dosage_grams) {

            Button button_dosage;
            button_dosage = findViewById(R.id.button_med_dosage_edited);
            button_dosage.setText("g");

            dosageUnitsEdited = "g";
            return true;
        }

        else if(item.getItemId() == R.id.item_dosage_milligrams) {

            Button button_dosage;
            button_dosage = findViewById(R.id.button_med_dosage_edited);
            button_dosage.setText("mg");

            dosageUnitsEdited = "mg";
            return true;
        }

        else if(item.getItemId() == R.id.item_dosage_micrograms) {

            Button button_dosage;
            button_dosage = findViewById(R.id.button_med_dosage_edited);
            button_dosage.setText("mcg");

            dosageUnitsEdited = "mcg";
            return true;
        }
        else if(item.getItemId() == R.id.item_dosage_IU) {

            Button button_dosage;
            button_dosage = findViewById(R.id.button_med_dosage_edited);
            button_dosage.setText("IU");

            dosageUnitsEdited = "IU";
            return true;
        }
        return false;
    }

    public void onFrequencyClickEdit(View view) {
        // Make the dropdown menu appear when you click on frequency
        PopupMenu freq_popup = new PopupMenu(this, view);
        freq_popup.setOnMenuItemClickListener(this::onMenuItemClick);
        freq_popup.inflate(R.menu.menu_medications_frequency);
        freq_popup.show();
    }

    public boolean onMenuItemClick(MenuItem item)
    {
        if(item.getItemId() == R.id.item_med_frequency_once_a_day) {

            Button button_freq;
            button_freq = findViewById(R.id.button_med_Frequency_edited);
            button_freq.setText("Once A Day");

            frequencyEdited = "Once A Day";
            return true;
        }

        else if(item.getItemId() == R.id.item_med_frequency_twice_a_day) {

            Button button_freq;
            button_freq = findViewById(R.id.button_med_Frequency_edited);
            button_freq.setText("Twice A Day");

            frequencyEdited = "Twice A Day";
            return true;
        }

        else if(item.getItemId() == R.id.item_med_frequency_once_a_week) {

            Button button_freq;
            button_freq = findViewById(R.id.button_med_Frequency_edited);
            button_freq.setText("Once A Week");

            frequencyEdited = "Once A Week";
            return true;
        }

        else if(item.getItemId() == R.id.item_med_frequency_twice_a_week) {

            Button button_freq;
            button_freq = findViewById(R.id.button_med_Frequency_edited);
            button_freq.setText("Twice A Week");

            frequencyEdited = "Twice A Week";
            return true;
        }
        return false;
    }

    public void onCancelPrescriptionClick (View view) {
        // user clicked the cancel button so...
        //brings user back to the previous window without prescribing the medication
        finish();
    }

    public void onSubmitPrescriptionEditionClick(View view) {

        //Initialize string versions of the global edited objects
        String string_med_Dosage_edited = medDosageEdited.getText().toString(); // dosage Amount
        String string_dosage_units_edited = dosageUnitsEdited;
        String string_frequency_edited = frequencyEdited;

        // Initialize a DocumentReference, using info from the bundle
        DocumentReference prescriptionRef = db.collection("users").document(patientIdFromBundle).collection("prescriptionsInfo").document(prescriptionNameFromBundle);

        // Input Validation
        if (string_med_Dosage_edited.equals("") ||
                string_dosage_units_edited.equals("") || string_frequency_edited.equals("")) {
            Toast.makeText(clinic_view_edit_prescriptions.this, "Error: Field(s) are blank.", Toast.LENGTH_SHORT).show();
        }
        else {

            // Input Validation Passed so...
            // Set medication data to string to pass to Firebase
            EditText editTextdosageAmtEdited = findViewById(R.id.edit_text_med_Dosage_edited);

            int integer_frequency = 0;

            String s_dosage_amt_edited = editTextdosageAmtEdited.getText().toString();

            // Validation for converting frequency to integers
            if (frequencyEdited.equals("Once A Day")) {
                integer_frequency = 7; // 7 times per week
            } else if (frequencyEdited.equals("Twice A Day")) {
                integer_frequency = 14; // 14 times per week
            } else if (frequencyEdited.equals("Once A Week")) {
                integer_frequency = 1; // 1 time per week
            } else if (frequencyEdited.equals("Twice A Week")) {
                integer_frequency = 2; // 2 times per week
            }

            // Using the DocumentReference, use .update to change the values
            prescriptionRef.update("dosageUnits", string_dosage_units_edited);
            prescriptionRef.update("dosageAmount", s_dosage_amt_edited);
            prescriptionRef.update("frequency", string_frequency_edited);

            Toast.makeText(clinic_view_edit_prescriptions.this, prescriptionNameFromBundle + " updated successfully.", Toast.LENGTH_SHORT).show();

            // Once Finished, send them back to Clinic Home Page
            Intent intent = new Intent(clinic_view_edit_prescriptions.this, clinic_home_page.class);
            startActivity(intent);
            finish(); // cannot go back


        }
    }

    public void onDiscontinuedMedicationClick(View view)
    {
        // Clinic has decided to DISCONTINUE/DELETE a prescription from the DB
        // Initialize a DocumentReference, using info from the bundle
        DocumentReference prescriptionRef = db.collection("users").document(patientIdFromBundle).collection("prescriptionsInfo").document(prescriptionNameFromBundle);

        // Delete the document (which holds the prescription) from the DB
        prescriptionRef.delete().addOnSuccessListener(aVoid -> Log.d(TAG, "Prescription deleted SUCCESSFULLY!"));

        Toast.makeText(clinic_view_edit_prescriptions.this, prescriptionNameFromBundle + " has been discontinued successfully.", Toast.LENGTH_SHORT).show();

        // Once Finished, send them back to Clinic Home Page
        Intent intent = new Intent(clinic_view_edit_prescriptions.this, clinic_home_page.class);
        startActivity(intent);
        finish(); // cannot go back

    }

}