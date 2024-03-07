package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class clinic_view_prescribed_meds_page extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_view_prescribed_meds);

        // Obtain information from clinic_view_select_patient_to_manage_meds.java
        Bundle bundle = getIntent().getExtras();
        String patientId = bundle.getString("patient");

        // Set up the RecyclerView for medications
        RecyclerView med_recycle_view = findViewById(R.id.recycler_view_clinic_prescribed_meds);
        med_recycle_view.setLayoutManager(new LinearLayoutManager(this));

        //list of items
        FirebaseFirestore db = MyFirestore.getDBInstance();
        FirebaseAuth mAuth = MyFirestore.getmAuthInstance();

        // Get the correct database path using patient id from passed value
        CollectionReference patientMedRef = db.collection("users").document(patientId).collection("prescriptionsInfo");

        patientMedRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<PrescribedMedications> pMedications_list = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    PrescribedMedications pMedication = document.toObject(PrescribedMedications.class);
                    pMedications_list.add(pMedication);
                }
                Log.d("TAG", "Medications size: " + pMedications_list.size()); // Check the size of the clinics list
                // Set the adapter (you'll create and set the adapter in later steps)
                MyPrescribedMedAdapter myAdapter = new MyPrescribedMedAdapter(getApplicationContext(), pMedications_list);
                med_recycle_view.setAdapter(myAdapter);

                //make meds clickable
                myAdapter.setOnItemClickListener(new MyPrescribedMedAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, PrescribedMedications prescribedMedications) {

                        //Send them to a form where clinic can edit the prescription or cancel it
                        Intent intent = new Intent(clinic_view_prescribed_meds_page.this, clinic_view_edit_prescriptions.class);
                        startActivity(intent);
                    }
                });
            }
            else {
                // Handle the error
                Log.e("TAG", "Error getting medications", task.getException());
            }

        });
    }
}