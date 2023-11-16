package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class home_page extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = myFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private DocumentReference userRef = db.collection("users").document(currentUser.getUid());
    private ListenerRegistration userListener;
    TextView dateFormat;
    Calendar calendar;
    TextView txtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        String TAG = "Token ID:";
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log
                        String msg = token;
                        Log.d(TAG, msg);
                    }
                });

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.homeId);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentId) {
                    startActivity(new Intent(getApplicationContext(), patient_view_search_centers_visit.class));
                    finish();
                    return true;
                } else if (id == R.id.homeId) {
                    return true;
                } else if (id == R.id.medicalHistId) {
                    startActivity(new Intent(getApplicationContext(), medical_records_page.class));
                    finish();
                    return true;
                } else if (id == R.id.resourcesId) {
                    startActivity(new Intent(getApplicationContext(), patient_diet_page.class));
                    finish();
                    return true;
                } else if (id == R.id.profileId) {
                    startActivity(new Intent(getApplicationContext(), profile_page.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

        // Set up the RecyclerView
        RecyclerView med_recycle_view = findViewById(R.id.recycler_view_home_page_medications);
        med_recycle_view.setLayoutManager(new LinearLayoutManager(this));

        //list of items
        FirebaseFirestore db = myFirestore.getDBInstance();
        FirebaseAuth mAuth = myFirestore.getmAuthInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Get the correct database path
        CollectionReference patientMedRef = db.collection("users").document(currentUser.getUid()).collection("prescriptionsInfo");

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

               /* myAdapter.setOnItemClickListener(new MyPrescribedMedAdapter().OnItemClickListener() {
                    //this sends the user to the clinic's specific appointment page
                    @Override
                    public void onItemClick(int position, PrescribedMedications pMedications) {
                        // Handle the item click here
                        Intent intent = new Intent(patient_view_prescribed_med.this, appointments_page.class);
                        startActivity(intent);
                    }
                });
*/
            }
            else {
                // Handle the error
                Log.e("TAG", "Error getting medications", task.getException());
            }
        });

        // Display current date
        dateFormat = (TextView) findViewById(R.id.date);

        String date;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("'Today is': MMMM dd, yyyy");
        date = simpleDateFormat.format(calendar.getTime()).toString();
        dateFormat.setText(date);

    }

    // AUTOMATIC LOADING
   public void onStart() {

       FirebaseUser currentUser = mAuth.getCurrentUser();
       if (currentUser != null) {
           DocumentReference userRef = db.collection("users").document(currentUser.getUid());

           userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()) {
                       DocumentSnapshot document = task.getResult();
                       if (document.exists()) {
                           // User document exists, retrieve data
                           String userFirstName = document.getString("firstName");
                           String userLastName = document.getString("lastName");
                           String userEmail = document.getString("email");
                           String userPhone = document.getString("phone");
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
        userListener = userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                // Error checking
                if(error != null)
                {
                    Toast.makeText(home_page.this, "Error while loading!", Toast.LENGTH_LONG).show();
                    return;
                }
                //check if the user is a patient or a clinic
                if (documentSnapshot.exists()) {
                    // Document exists
                    // This will do the same work as the onLoad method
                    // But it is done automatically

                    // Set greeting
                    calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    TextView welcome = (TextView) findViewById(R.id.text_view_dashboard);

                    String greeting = "null";
                    if (hour >= 5 && hour < 12){
                        greeting = "Good morning, ";
                    } else if (hour >= 12 && hour < 17){
                        greeting = "Good afternoon, ";
                    } else {
                        greeting = "Good evening, ";
                    }
                    if (documentSnapshot.contains("location")) {
                    } else {
                        // Document doesn't have the specific element
                        // Perform your action here
                        String user_firstname = documentSnapshot.get("firstName").toString();
                        welcome.setText(greeting + user_firstname + ".");
                    }


                }
            }
        });
    }
}