package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class patient_view_med_history extends AppCompatActivity {


    TextView top_message;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = myFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    // How to get patient's medical history data without hard coding??
    private DocumentReference userMedRef = db.collection("users").document(currentUser.getUid()).collection("medicalHistory").document("medicalHistory");
    private ListenerRegistration userMedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_med_history);
        // Set the top message to bold and underline
        top_message = findViewById(R.id.text_view_med_history_text);
        top_message.setPaintFlags(top_message.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
    }

    public void onStart() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DocumentReference userRef = db.collection("medicalHistory").document(currentUser.getUid());

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // User document exists, retrieve data
                            String age = document.getString("age");
                            // Retrieve other user data as needed
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
        userMedListener = userMedRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                // Error checking
                if(error != null)
                {
                    Toast.makeText(patient_view_med_history.this, "Error while loading!", Toast.LENGTH_LONG).show();
                    return;
                }
                //check if the user is a patient or a clinic
                if (documentSnapshot.exists())
                {

                    TextView p_age = (TextView) findViewById(R.id.text_view_age_pvr);


                    if (documentSnapshot.contains("location")) {
                    }
                    else {
                        // Document doesn't have the specific element
                        // Perform your action here
                        String patient_age = documentSnapshot.get("age").toString();
                        p_age.setText("Age: " + patient_age);
                    }


                }
            }
        });
    }
}