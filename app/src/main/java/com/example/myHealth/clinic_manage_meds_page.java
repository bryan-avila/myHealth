package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class clinic_manage_meds_page extends AppCompatActivity {

    // This page does not belong to anything??????????????
    // This page does not belong to anything??????????????
    // This page does not belong to anything??????????????
    // This page does not belong to anything??????????????
    // This page does not belong to anything??????????????
    // Too bad!

    // Set up DB Stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Set up Patient Adapter stuff
    List<Patient> patientsList;

    // Set up Searchview stuff
    RecyclerView recyclerView = findViewById(R.id.recycler_view_patients);
    SearchView filterView;
    MyPatientAdapter myPatAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_view_patients_manage_meds);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get all user information from firebase
        CollectionReference patients = db.collection("users");

        patients.get().addOnCompleteListener(task -> { // Gets information from the database

            // Created a list of all patients (above)

            if (task.isSuccessful()) {
                //If patients exist, initialize list of patients here
                patientsList = new ArrayList<>();

                // For loop populating the recycler view with patients
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Patient p = document.toObject(Patient.class);
                    String patient_id = document.getId(); // Get patient userID
                    p.setPat_ID(patient_id); // Set patient id
                    patientsList.add(p);
                }
            }

            myPatAdapater = new MyPatientAdapter(getApplicationContext(), patientsList);
            recyclerView.setAdapter(myPatAdapater);

            // Make Patients Clickable
            myPatAdapater.setOnItemClickListener(new MyPatientAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, Patient patients) {
                    String p_id = patients.getPat_ID().toString();
                    // Send them to see the patient's medications after clicking on a patients name using this onItemClickListener
                    // Will need to implement an edit functionality to delete medications for a patient
                    Intent intent = new Intent(clinic_manage_meds_page.this, clinic_prescribed_meds_page.class);
                    intent.putExtra("patient",p_id); // send patient id with the intent
                    startActivity(intent);
                }
            });

        }); // END OF database info collection

        Toast.makeText(this, patientsList.size() + "lol", Toast.LENGTH_SHORT).show();

        // Start of Search View Filtering

        filterView = findViewById(R.id.searchView);
        filterView.clearFocus();
        filterView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myPatAdapater.getFilter().filter(newText);
                return true;
            }
        });

    }
}