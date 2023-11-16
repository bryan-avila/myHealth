package com.example.myHealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// MyAdapter.java
public class MyPatientAdapter extends RecyclerView.Adapter<MyViewHolderPatient>{

    Context context;
    List<Patient> patients;
    private OnItemClickListener mListener;

    public MyPatientAdapter(Context context, List<Patient> patients) {
        this.context = context;
        this.patients = patients;
    }
    //For search bar functionality
    private void filterList(List<Patient> filteredList) {
        this.patients = filteredList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MyViewHolderPatient onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolderPatient(LayoutInflater.from(context).inflate(R.layout.patient_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderPatient holder, int position) {
        holder.firstName.setText(patients.get(position).getfirstName());
        holder.lastName.setText(patients.get(position).getlastName());
        holder.email.setText(patients.get(position).getEmail());
        holder.phone.setText(patients.get(position).getPhone());


        // Set click listener
        holder.itemView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onItemClick(position, patients.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }


    // Method to set the click listener from outside the adapter
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Patient patient);
    }
}
