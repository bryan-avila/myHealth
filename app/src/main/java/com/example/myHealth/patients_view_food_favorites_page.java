package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class patients_view_food_favorites_page extends AppCompatActivity {

    // Initialize Database Stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    CollectionReference patientFoodAddedRef = db.collection("users").document(currentUser.getUid()).collection("favoriteFoods");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_view_food_favorites_page);

        RecyclerView favorite_foods_recycler_view = findViewById(R.id.recycler_view_food_favorites);
        favorite_foods_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        patientFoodAddedRef.get().addOnCompleteListener(task -> {

            if(task.isSuccessful())
            {
                List<FavoriteFoodNameFromList> favoriteFoodsList = new ArrayList<>();

                for(QueryDocumentSnapshot document : task.getResult())
                {
                    FavoriteFoodNameFromList fav_food_doc = document.toObject(FavoriteFoodNameFromList.class);
                    favoriteFoodsList.add(fav_food_doc);
                }

                MyFavoriteFoodNameFromListAdapter myFavFoodsAdapter = new MyFavoriteFoodNameFromListAdapter(getApplicationContext(), favoriteFoodsList);
                System.out.println(myFavFoodsAdapter.getItemCount() + " LOL!");
                favorite_foods_recycler_view.setAdapter(myFavFoodsAdapter);

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set resources selected, the resourcesID, AKA the diet page
        bottomNavigationView.setSelectedItemId(R.id.resourcesId);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //check id
                if (id == R.id.appointmentId) {
                    startActivity(new Intent(getApplicationContext(), patient_search_centers_visit_page.class));
                    finish();
                    return true;
                } else if (id == R.id.homeId) {
                    startActivity(new Intent(getApplicationContext(), patient_home_page.class));
                    finish();
                    return true;
                } else if (id == R.id.medicalHistId) {
                    startActivity(new Intent(getApplicationContext(), patient_medical_records_page.class));
                    finish();
                    return true;
                } else if (id == R.id.resourcesId) {
                    return true;
                } else if (id == R.id.profileId) {
                    startActivity(new Intent(getApplicationContext(), patient_profile_page.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}