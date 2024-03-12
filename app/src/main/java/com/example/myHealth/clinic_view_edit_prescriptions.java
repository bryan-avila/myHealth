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

    String cName, cPhone; // global variables to get clinic's name and clinic phone
    EditText medDosageEdited;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private ListenerRegistration clinicListener;

    private ListenerRegistration userL;
    private DocumentReference userRef = db.collection("users").document(currentUser.getUid());
    private DocumentReference clinicRef = db.collection("clinic").document(currentUser.getUid());


    String dosageUnitsEdited = "";
    String frequencyEdited = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_view_edit_prescriptions);
        medDosageEdited = findViewById(R.id.edit_text_med_Dosage_edited);
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
        //brings user back to the previous window without prescribing the medication
        finish();
    }



    public void onSubmitPrescriptionEditionClick(View view) {
        //Initialize objects
        String string_med_Dosage_edited = medDosageEdited.getText().toString();
        String string_dosage_units_edited = dosageUnitsEdited;
        String string_frequency_edited = frequencyEdited;

        Bundle bundle = getIntent().getExtras();
        String patientId = bundle.getString("patient");

        DocumentReference patientRef = db.collection("users").document(patientId);

        CollectionReference prescriptionsInfoRef = patientRef.collection("prescriptionsInfo");

        /*prescriptionsInfoRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String s_med_name = queryDocumentSnapshots.getQuery().toString();
            }
        });*/

        if (string_med_Dosage_edited.equals("") ||
                string_dosage_units_edited.equals("") || string_frequency_edited.equals("")) {
            Toast.makeText(clinic_view_edit_prescriptions.this, "Error: Field(s) are blank.", Toast.LENGTH_SHORT).show();
        }  else {
            // Set medication data to string to pass to Firebase
            EditText editTextdosageAmtEdited = findViewById(R.id.edit_text_med_Dosage_edited);

            int integer_frequency = 0;

            // Set up strings the database can use
            //String s_med_name = editTextmedicationName.getText().toString(); display medication at the top in this box
            String s_dosage_amt_edited = editTextdosageAmtEdited.getText().toString();

            if (frequencyEdited.equals("Once A Day")) {
                integer_frequency = 7; // 7 times per week
            } else if (frequencyEdited.equals("Twice A Day")) {
                integer_frequency = 14; // 14 times per week
            } else if (frequencyEdited.equals("Once A Week")) {
                integer_frequency = 1; // 1 time per week
            } else if (frequencyEdited.equals("Twice A Week")) {
                integer_frequency = 2; // 2 times per week
            }



            //Update the medication info in the patient's database collection
            Map<String, Object> medicationInfoEdited = new HashMap<>();
            medicationInfoEdited.put("dosageAmount", s_dosage_amt_edited);
            medicationInfoEdited.put("dosageUnits", dosageUnitsEdited); // Get dosage units from the pop up
            medicationInfoEdited.put("frequency", frequencyEdited); // Get frequency from the pop up


            //Update the information, using .update to not overwrite other fields
            //Get the medication name as we are already inside the patient's document

            prescriptionsInfoRef.document(s_med_name).update(medicationInfoEdited)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Handle success
                            Log.d(TAG, "Updated medication information");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                            Log.w(TAG, "Error updating medication information", e);
                        }
                    });

            // Send to clinic home page
            Intent intent = new Intent(clinic_view_edit_prescriptions.this, clinic_home_page.class);
            startActivity(intent);
            finish(); // cannot go back
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
                    Toast.makeText(clinic_view_edit_prescriptions.this, "Error while loading!", Toast.LENGTH_LONG).show();
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