package com.example.myHealth;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderUpcomingAppointment extends RecyclerView.ViewHolder {

    TextView clinicName, startTime, endTime, recurring;

    public MyViewHolderUpcomingAppointment(@NonNull View itemView) {
        super(itemView);
        // Get the .XML R.id's from the prescribed_med_item_view.xml file
        clinicName = itemView.findViewById(R.id.text_view_clinic_name);
        startTime = itemView.findViewById(R.id.text_view_start_time);
        endTime = itemView.findViewById(R.id.text_view_end_time);
        recurring = itemView.findViewById(R.id.text_view_recurring);

        // Underline clinic name
        clinicName.setPaintFlags(clinicName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }


}
