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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private SearchView searchView;

    MyPatientAdapter patientAdapter;
    private List<Patient> patientsList = null;

    String patientMedName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_view_patients_manage_meds);

        //-------------------------------UNCOMMENT TO ALLOW FILTERING TO WORK-----------------------
        //Find id for search bar
        /*searchView = findViewById(R.id.searchView);
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
        });*/
        //------------------------------------------------------------------------------------------

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

        });
    }



    //----------------------------------------------------------------------------------------------
    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idSearch = item.getItemId();
        if (idSearch == R.id.searchView) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchString = newText;
                patientAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }*/
    //----------------------------------------------------------------------------------------------

    //---------------------------------------UNCOMMENT TO ALLOW FILTERING TO WORK-------------------
    //Filtering does not work
    /*private void filterList(String text) {
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
            //MyPatientAdapter newPatientAdapter = new MyPatientAdapter(getApplicationContext(), patientsList);
            //newPatientAdapter.filterList(filteredList);


            //Below implementation works but messes up with prescribing medication to user
            MyPatientAdapter newPatientAdapter = new MyPatientAdapter(getApplicationContext(), patientsList);
            RecyclerView recyclerViewFiltered = findViewById(R.id.recycler_view_patients);
            recyclerViewFiltered.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewFiltered.setAdapter(newPatientAdapter); //this overrides the previous .setAdapter
            newPatientAdapter.filterList(filteredList);
        }
    }*/
}