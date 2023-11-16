package com.example.myHealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// MyAdapter.java
public class MyPrescribedMedAdapter extends RecyclerView.Adapter<MyViewHolderPrescribedMed>{

    Context context;
    List<PrescribedMedications> pMedications;
    private OnItemClickListener mListener;

    public MyPrescribedMedAdapter(Context context, List<PrescribedMedications> p_Medications) {
        this.context = context;
        this.pMedications = p_Medications;
    }

    @NonNull
    @Override
    public MyViewHolderPrescribedMed onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolderPrescribedMed(LayoutInflater.from(context).inflate(R.layout.prescribed_med_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderPrescribedMed holder, int position) {
        holder.medicationName.setText(pMedications.get(position).getMedicationName());
        holder.dosageAmount.setText(pMedications.get(position).getDosageAmount());
        holder.frequency.setText(pMedications.get(position).getFrequency());



        // Set click listener
        holder.itemView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onItemClick(position, pMedications.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pMedications.size();
    }

    // Method to set the click listener from outside the adapter
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, PrescribedMedications prescribedMedications);
    }
}
