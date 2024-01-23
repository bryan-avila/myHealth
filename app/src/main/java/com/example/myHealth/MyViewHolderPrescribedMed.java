package com.example.myHealth;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderPrescribedMed extends RecyclerView.ViewHolder {

    TextView medicationName, frequency, dosageAmount, clinicName, clinicPhone;

    public MyViewHolderPrescribedMed(@NonNull View itemView) {
        super(itemView);
        // Get the .XML R.id's from the prescribed_med_item_view.xml file
        medicationName = itemView.findViewById(R.id.text_view_prescribed_med_name);
        frequency = itemView.findViewById(R.id.text_view_prescribed_frequency);
        dosageAmount = itemView.findViewById(R.id.text_view_prescribed_dosage);
        clinicName = itemView.findViewById(R.id.text_view_clinic_name);
        clinicPhone = itemView.findViewById(R.id.text_view_clinic_phone);

        // Underline medication name
        medicationName.setPaintFlags(medicationName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }


}
