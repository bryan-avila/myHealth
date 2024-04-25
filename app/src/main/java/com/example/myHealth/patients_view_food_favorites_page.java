package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

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

    // Set up global DB stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private CollectionReference favFoodCollectionRef = db.collection("users").document(currentUser.getUid()).collection("favFoods");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_view_food_favorites_page);

        // Set up recyler view stuff
        RecyclerView fav_food_recycler_view = findViewById(R.id.recycler_view_favorites);

        fav_food_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        // Using a collection reference from the DB....
        // test...
        favFoodCollectionRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                List<FavoriteFoods> favoriteFoods = new ArrayList<>(); // if collection exists, create an array to populate recycler view

                for(QueryDocumentSnapshot document : task.getResult())
                {
                    FavoriteFoods fav_food_doc = document.toObject(FavoriteFoods.class);
                    String food_name = (String) document.get("favFood"); // Ignore the top line? Just get the food's name from the document
                    FavoriteFoods food = new FavoriteFoods(food_name); // Create a new FavoriteFoods object, passing the food name in the parameter
                    favoriteFoods.add(food); // Add the newly created FavoriteFoods object to the recycler view list
                }

                // Set up adapter to have your list of favorited foods
                MyAdapterFavoriteFoods myFavFoodAdapter = new MyAdapterFavoriteFoods(getApplicationContext(), favoriteFoods);
                System.out.println(" ATTN: ------------ " + favoriteFoods.size() + " is the # of favorited foods. --------------");
                fav_food_recycler_view.setAdapter(myFavFoodAdapter);
                myFavFoodAdapter.setOnItemClickListener(new MyAdapterFavoriteFoods.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, FavoriteFoods favoriteFoods) {
                    }
                });

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