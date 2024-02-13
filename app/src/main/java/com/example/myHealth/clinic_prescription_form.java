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

import java.util.HashMap;
import java.util.Map;

public class clinic_prescription_form extends AppCompatActivity {

    String cName, cPhone; // global variables to get clinic's name and clinic phone
    EditText medName, medDosage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference clinicRef = db.collection("clinic").document(currentUser.getUid());
    private ListenerRegistration clinicListener;

    String dosageUnits; // dosage from the pop up, make it global

    String frequency;  // Frequency from the pop up, make it global





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_fill_prescription_form);
        medName = findViewById(R.id.edit_text_med_Name);
        medDosage = findViewById(R.id.edit_text_med_Dosage);

    }

    //Dosage units implementation
    public void onDosageSelectClick(View view) {
        //Make dropdown menu appear when clicked
        PopupMenu dosage_popup = new PopupMenu(this, view);
        dosage_popup.setOnMenuItemClickListener(this::onMenuItemClick2);
        dosage_popup.inflate((R.menu.menu_dosage));
        dosage_popup.show();
    }

    public boolean onMenuItemClick2(MenuItem item) {
        if(item.getItemId() == R.id.item_dosage_grams) {

            Button button_dosage;
            button_dosage = findViewById(R.id.button_med_dosage);
            button_dosage.setText("g");

            dosageUnits = "g";
            return true;
        }

        else if(item.getItemId() == R.id.item_dosage_milligrams) {

            Button button_dosage;
            button_dosage = findViewById(R.id.button_med_dosage);
            button_dosage.setText("mg");

            dosageUnits = "mg";
            return true;
        }

        else if(item.getItemId() == R.id.item_dosage_micrograms) {

            Button button_dosage;
            button_dosage = findViewById(R.id.button_med_dosage);
            button_dosage.setText("mcg");

            dosageUnits = "mcg";
            return true;
        }
        else if(item.getItemId() == R.id.item_dosage_IU) {

            Button button_dosage;
            button_dosage = findViewById(R.id.button_med_dosage);
            button_dosage.setText("IU");

            dosageUnits = "IU";
            return true;
        }

        return false;

    }

    //Frequency implementation
    public void onFrequencyClick(View view)
    {
        // Make the dropdown menu appear when you click on frequency
        PopupMenu freq_popup = new PopupMenu(this, view);
        freq_popup.setOnMenuItemClickListener(this::onMenuItemClick);
        freq_popup.inflate(R.menu.menu_medications_frequency);
        freq_popup.show();
    }

    public boolean onMenuItemClick(MenuItem item)
    {
        if(item.getItemId() == R.id.item_med_frequency_once_a_day)
        {

            Button button_freq;
            button_freq = findViewById(R.id.button_med_Frequency);
            button_freq.setText("Once A Day");

            frequency = "Once A Day";
            return true;
        }

        else if(item.getItemId() == R.id.item_med_frequency_twice_a_day)
        {

            Button button_freq;
            button_freq = findViewById(R.id.button_med_Frequency);
            button_freq.setText("Twice A Day");

            frequency = "Twice A Day";
            return true;
        }

        else if(item.getItemId() == R.id.item_med_frequency_once_a_week)
        {

            Button button_freq;
            button_freq = findViewById(R.id.button_med_Frequency);
            button_freq.setText("Once A Week");

            frequency = "Once A Week";
            return true;
        }

        else if(item.getItemId() == R.id.item_med_frequency_twice_a_week)
        {

            Button button_freq;
            button_freq = findViewById(R.id.button_med_Frequency);
            button_freq.setText("Twice A Week");

            frequency = "Twice A Week";
            return true;
        }


        return false;
    }

    public void onSubmitPrescriptionClick (View view) {

        String string_med_Name = medName.getText().toString();
        String string_med_Dosage = medDosage.getText().toString();
        //String string_dosage_units = dosageUnits.toString();
        //String string_frequency = frequency.toString();

        boolean inputCheckerOnMedication = validateMedInput(string_med_Name, string_med_Dosage);

        // Obtain information from clinic_med_hist_page.java
        Bundle bundle = getIntent().getExtras();
        String patientId = bundle.getString("patient");

        DocumentReference patientRef = db.collection("users").document(patientId);

        CollectionReference prescriptionsInfoRef = patientRef.collection("prescriptionsInfo");


        // Set up edittext variables from .XML file
        EditText editTextmedicationName = findViewById(R.id.edit_text_med_Name);
        EditText editTextdosageAmt = findViewById(R.id.edit_text_med_Dosage);

        int integer_frequency = 0;
        // Set up strings the database can use
        String s_med_name = editTextmedicationName.getText().toString();
        String s_dosage_amt = editTextdosageAmt.getText().toString();

        if(frequency.equals("Once A Day"))
        {
            integer_frequency = 7; // 7 times per week
        }
        else if(frequency.equals("Twice A Day"))
        {
            integer_frequency = 14; // 14 times per week
        }

        else if(frequency.equals("Once A Week"))
        {
            integer_frequency = 1; // 1 time per week
        }

        else if(frequency.equals("Twice A Week"))
        {
            integer_frequency = 2; // 2 times per week
        }

        String phone = "818-355-2913";

        // Add the medication info to the patient's database collection
        Map<String, Object> medicationInfo = new HashMap<>();
        medicationInfo.put("medicationName", s_med_name);
        medicationInfo.put("dosageAmount", s_dosage_amt);
        medicationInfo.put("dosageUnits", dosageUnits); // Get dosage units from the pop up
        medicationInfo.put("frequency", frequency); // Get frequency from the pop up

       // medicationInfo.put("intFrequency", integer_frequency); // Turn string frequency into an integer for notifications
        
        medicationInfo.put("clinicName", cName); // Store the clinic name so that patients know where they got it from, global variable
        medicationInfo.put("clinicPhone",  cPhone); // Store clinic phone, using global variable

        // Add medication to the firebase. The document will be named different according to the name of the Prescription
        prescriptionsInfoRef.document(s_med_name).set(medicationInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle success
                        Log.d(TAG, "medicationInfo added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Log.w(TAG, "Error adding medicationInfo document", e);
                    }
                });




        startActivity(new Intent(getApplicationContext(), clinic_prescription_form.class));

        if (inputCheckerOnMedication) {
            Toast.makeText(clinic_prescription_form.this, "Medication Prescribed.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), clinic_medical_records_page.class));
        } else {
            Toast.makeText(clinic_prescription_form.this, "Please fill out any empty fields.", Toast.LENGTH_LONG).show();
        }

    }


    public boolean validateMedInput (String med_Name, String med_Dosage) {
        if (med_Name.length() == 0) {
            medName.requestFocus();
            medName.setError("FIELD CANNOT BE EMPTY");
            return false;
        } else if (med_Dosage.length() == 0) {
            medDosage.requestFocus();
            medDosage.setError("FIELD CANNOT BE EMPTY");
            return false;
            //crashes here
        } /*else if (dosageUnits.length() == 0) {
            medDosage.requestFocus();
            medDosage.setError("FIELD MUST BE AN INTEGER");
            return false;
        }
        else if (frequency.length() == 0) {
            medDosage.requestFocus();
            medDosage.setError("FIELD MUST BE AN INTEGER");
            return false;
        }*/ else {
            return true;
        }
    }

    public void onStart() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Get information from the firebase for clinics
            clinicRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // User document exists, retrieve data from clinic
                            cName = document.getString("clinicName");
                            cPhone = document.getString("phone");
                        } else {
                            // User document doesn't exist, handle accordingly
                        }
                    } else {
                        // Handle failure to retrieve user document
                    }
                }
            });
        }


        super.onStart();
        // Automatically loading
        // Firestore wants to load things quickly, so it loads in locally before from the cloud
        // Save addSnapShotListener to noteListener, automatically detach/attach by adding this
        clinicListener = clinicRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                // Error checking
                if(error != null)
                {
                    Toast.makeText(clinic_prescription_form.this, "Error while loading!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot.exists()) {
                    // Document exists
                    // This will do the same work as the onLoad method
                    // But it is done automatically

                    // Get name and phone information from clinics
                    cName  = documentSnapshot.get("clinicName").toString();
                    cPhone = documentSnapshot.get("phone").toString();
                }
            }
        });
    }
}