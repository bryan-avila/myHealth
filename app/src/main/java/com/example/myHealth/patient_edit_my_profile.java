package com.example.myHealth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

public class patient_edit_my_profile extends AppCompatActivity {

    // Set up Firebase stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    DocumentReference userReference = db.collection("users").document(currentUser.getUid()); // Firebase collection path
    ListenerRegistration userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_my_profile);
    }

    public void onCancelPatCancelChange(View view) {
        finish();
    }

    public void onPatMakeChangesClick(View view)
    {
        // Get the new information from the editText
        EditText editTextPhoneNumber = findViewById(R.id.edit_text_new_phone_num);

        // Store it as a string
        String newPhoneNum = editTextPhoneNumber.getText().toString();

        // Put the new information into a map
        Map<String, Object> newInfo = new HashMap<>();
        newInfo.put("phone", newPhoneNum);

        // Get the firestore database document path
        DocumentReference userRef = db.collection("users").document(currentUser.getUid());

        // Update the information, using .update to not overwrite other fields
        userRef.update(newInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle success
                        Log.d(TAG, "Phone Updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Log.w(TAG, "Error updating changes", e);
                    }
                });

        // Once they finish inputting diet, send them to page that lets them make appointments
        Intent intent = new Intent(getApplicationContext(), patient_home_page.class);
        startActivity(intent);
        finish();

    }

//    // Loading user's automatically
//    public void onStart() {
//
//        // Get the data on the current user
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            // Get the path from the Firebase
//            userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            // User document exists, retrieve data
//                        } else {
//                            // User document doesn't exist, handle accordingly
//                        }
//                    } else {
//                        // Handle failure to retrieve user document
//                    }
//                }
//            });
//        }
//
//
//        super.onStart();
//        // Automatically loading of user data
//        userListener = userReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//
//                // Error checking
//                if(error != null)
//                {
//                    Toast.makeText(patient_edit_my_profile.this, "Error while loading!", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                // If medical history data exists
//                if (documentSnapshot.exists())
//                {
////                    // Initialize data
////                    String patient_stage = documentSnapshot.get("stage").toString();
////                    TextView stage_4_message = findViewById(R.id.text_view_stage_4_warning);
////                    TextView stage_5_message = findViewById(R.id.text_view_stage_5_warning);
////
////
////                    // Display different nutrient values depending on stage/med history/age
////                    if(patient_stage.equals("Kidney Disease Stage 4"))
////                    {
////                        chosen_stage = "4";
////                        stage_4_message.setVisibility(View.VISIBLE);
////                        autofill_btn = findViewById(R.id.button_patient_diet_auto_fill);
////                        autofill_btn.setVisibility(View.VISIBLE);
////                        autofill_btn.setClickable(true);
////                    }
////
////                    else if(patient_stage.equals("Kidney Disease Stage 5"))
////                    {
////                        chosen_stage ="5";
////                        stage_5_message.setVisibility(View.VISIBLE);
////                        autofill_btn = findViewById(R.id.button_patient_diet_auto_fill);
////                        autofill_btn.setVisibility(View.VISIBLE);
////                        autofill_btn.setClickable(true);
////                    }
//
//
//                }
//            }
//        });
//    }
}