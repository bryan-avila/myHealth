package com.example.myHealth;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderPatient extends RecyclerView.ViewHolder {

    TextView firstName, lastName, email, phone;

    public MyViewHolderPatient(@NonNull View itemView) {
        super(itemView);
        firstName = itemView.findViewById(R.id.text_view_p_first_name);
        lastName = itemView.findViewById(R.id.text_view_p_last_name);
        email = itemView.findViewById(R.id.text_view_p_email);
        phone = itemView.findViewById(R.id.text_view_p_phone);
    }


}
