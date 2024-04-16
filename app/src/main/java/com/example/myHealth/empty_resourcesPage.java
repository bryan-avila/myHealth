package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class empty_resourcesPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_resources_page);

        TextView hyperLink1 = findViewById(R.id.text_view_nat_kidney_found_hyperlink);
        hyperLink1.setMovementMethod(LinkMovementMethod.getInstance());

        TextView hyperLink2 = findViewById(R.id.text_view_davita_hyperlink);
        hyperLink2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView hyperLink3 = findViewById(R.id.text_view_kidneyfund_hyperlink);
        hyperLink3.setMovementMethod(LinkMovementMethod.getInstance());

        TextView hyperLink4 = findViewById(R.id.text_view_nat_kidneydiets_hpyerlink);
        hyperLink4.setMovementMethod(LinkMovementMethod.getInstance());

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set profile selected
        bottomNavigationView.setSelectedItemId(R.id.profileId);

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
                    startActivity(new Intent(getApplicationContext(), patient_nutrition_page.class));
                    finish();
                    return true;
                } else if (id == R.id.profileId) {
                    return true;
                }
                return false;
            }

        });
    }
}