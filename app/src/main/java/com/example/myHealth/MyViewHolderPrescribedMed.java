package com.example.myHealth;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderPrescribedMed extends RecyclerView.ViewHolder {

    TextView medicationName, frequency, dosageAmount;

    public MyViewHolderPrescribedMed(@NonNull View itemView) {
        super(itemView);
        // Get the .XML R.id's from the prescribed_med_item_view.xml file
        medicationName = itemView.findViewById(R.id.text_view_prescribed_med_name);
        frequency = itemView.findViewById(R.id.text_view_prescribed_dosage);
        dosageAmount = itemView.findViewById(R.id.text_view_prescribed_frequency);
    }


}
