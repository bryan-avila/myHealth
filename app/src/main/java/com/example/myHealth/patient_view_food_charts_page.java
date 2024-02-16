package com.example.myHealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class patient_view_food_charts_page extends AppCompatActivity {

    // variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList;

    private List<String> xValues = Arrays.asList("Phosphorus", "Potassium", "Protein");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_food_charts_page);

        // ------- BAR CHART SET UP ---------
            // initializing variable for bar chart.
            barChart = findViewById(R.id.chart);
            barChart.getAxisLeft().setDrawLabels(false);

            // Control the Left Y Axis Line
            YAxis y = barChart.getAxisLeft();
            y.setAxisLineWidth(2f);
            y.setAxisLineColor(Color.BLACK);
            y.setGranularity(1f); // Controls how the labels look when zooming in
            y.setLabelCount(6); // Controls label entries for the y axis
            // calling method to get bar entries.
            getBarEntries();

            // creating a new bar data set.
            barDataSet = new BarDataSet(barEntriesArrayList, "Nutrients");

            // creating a new bar data and
            // passing our bar data set.
            barData = new BarData(barDataSet);

            // below line is to set data
            // to our bar chart.
            barChart.setData(barData);

            // adding color to our bar data set.
            barDataSet.setColors(Color.BLUE);

            // setting text color.
            barDataSet.setValueTextColor(Color.BLACK);

            // Control the X Axis and change names
            barChart.getDescription().setEnabled(false);
            barChart.invalidate();
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setGranularityEnabled(true);

            // Create and modify header textview
        TextView header_message = findViewById(R.id.text_view_charts_header_message);
        header_message.setPaintFlags(header_message.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        header_message.setText("Today's Charts");

        // Create and modify date textview
        TextView header_date = findViewById(R.id.text_view_charts_todays_date);
        String date;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        date = simpleDateFormat.format(calendar.getTime()).toString();
        header_date.setText(date);



        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set resources selected
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

    private void getBarEntries() {
        // creating a new array list
        barEntriesArrayList = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntriesArrayList.add(new BarEntry(0, 45));
        barEntriesArrayList.add(new BarEntry(1, 60)); // X value controls the order of appearance in the bar chart
        barEntriesArrayList.add(new BarEntry(2, 30)); // y value controls how highh the bar chart goes
    }
}
