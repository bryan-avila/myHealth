package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class clinic_view_patients_manage_meds extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private SearchView searchView;
    private List<Patient> patientsList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_view_patients_manage_meds);

        //Find id for search bar
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus(); //removes cursor from search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_patients);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get all user information from firebase
        CollectionReference patients = db.collection("users");

        patients.get().addOnCompleteListener(task -> {

            // Created a list of all patients (above)

            if (task.isSuccessful()) {
                //initialized list of patients here
                patientsList = new ArrayList<>();

                // For loop populating the recycler view
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Patient p = document.toObject(Patient.class);
                    String patient_id = document.getId(); // Get patient userID
                    p.setPat_ID(patient_id); // Set patient id
                    patientsList.add(p);
                }
            }

            MyPatientAdapter myPatAdapater = new MyPatientAdapter(getApplicationContext(), patientsList);
            recyclerView.setAdapter(myPatAdapater);

            // Make Patient's Clickable
            myPatAdapater.setOnItemClickListener(new MyPatientAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, Patient patients) {

                    String p_id = patients.getPat_ID().toString();
                    // Send them to the prescription form after clicking on a patients name using this onItemClickListener
                    Intent intent = new Intent(clinic_view_patients_manage_meds.this, clinic_prescription_form.class);
                    // Send extra info to know where to send the medication information
                    intent.putExtra("patient",p_id);
                    startActivity(intent);
                }
            });

        });
    }

    //Filtering does not work
    private void filterList(String text) {
        List<Patient> filteredList = new ArrayList<>();
        for (Patient p : patientsList) {
            if (p.getfirstName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(p);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this,"Patient not found", Toast.LENGTH_SHORT).show();
        } else {
            //Send data to the adapter class (look at MyPatientAdapter class
            //Call adapter
            //This line not working with adapter
            MyPatientAdapter newPatientAdapter = new MyPatientAdapter(getApplicationContext(), patientsList);
            newPatientAdapter.filterList(filteredList);
        }
    }
}