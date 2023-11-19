package com.example.myHealth;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderClinic extends RecyclerView.ViewHolder {

    TextView clinicName;
    TextView location;

    public MyViewHolderClinic(@NonNull View itemView) {
        super(itemView);
        clinicName = itemView.findViewById(R.id.clinicName);
        location = itemView.findViewById(R.id.location);
    }



}
