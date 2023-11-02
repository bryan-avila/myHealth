package com.example.myHealth;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// MyAdapter.java
public class MyClinicAdapter extends RecyclerView.Adapter<MyViewHolder>{

    Context context;
    List<Clinic> clinics;

    public MyClinicAdapter(Context context, List<Clinic> clinics) {
        this.context = context;
        this.clinics = clinics;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.clinic_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.clinicName.setText(clinics.get(position).getClinicName());
        holder.location.setText(clinics.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return clinics.size();
    }
}
