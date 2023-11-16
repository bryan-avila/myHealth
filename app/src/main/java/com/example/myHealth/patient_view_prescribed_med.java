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

public class patient_view_prescribed_med extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_prescribed_med);

        // Set up the RecyclerView
        RecyclerView med_recycle_view = findViewById(R.id.recycler_view_precribed_meds);
        med_recycle_view.setLayoutManager(new LinearLayoutManager(this));

        //list of items
        FirebaseFirestore db = myFirestore.getDBInstance();
        FirebaseAuth mAuth = myFirestore.getmAuthInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Get the correct database path
        CollectionReference patientMedRef = db.collection("users").document(currentUser.getUid()).collection("prescriptionsInfo");

        patientMedRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                List<PrescribedMedications> pMedications_list = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    PrescribedMedications pMedication = document.toObject(PrescribedMedications.class);
                    pMedications_list.add(pMedication);
                }

                Log.d("TAG", "Medications size: " + pMedications_list.size()); // Check the size of the clinics list
                // Set the adapter (you'll create and set the adapter in later steps)
                MyPrescribedMedAdapter myAdapter = new MyPrescribedMedAdapter(getApplicationContext(), pMedications_list);
                med_recycle_view.setAdapter(myAdapter);

 /*               myAdapter.setOnItemClickListener(new MyPrescribedMedAdapter.OnItemClickListener() {
                    //this sends the user to the clinic's specific appointment page
                    @Override
                    public void onItemClick(int position, PrescribedMedications prescribedMedications) {
                        // Handle the item click here
                        Intent intent = new Intent(patient_view_prescribed_med.this, clinic_home_page.class);
                        startActivity(intent);
                    }

            });*/

    }
});
}
}