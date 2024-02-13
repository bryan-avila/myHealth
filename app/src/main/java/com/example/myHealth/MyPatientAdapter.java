package com.example.myHealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// MyAdapter.java
public class MyPatientAdapter extends RecyclerView.Adapter<MyViewHolderPatient> implements Filterable {

    Context context;
    List<Patient> patients;
    List<Patient> patientsFull; // Create another list to act as a copy
    private OnItemClickListener mListener;

    public MyPatientAdapter(Context context, List<Patient> patients) {
        this.context = context;
        this.patients = patients;
        patientsFull = new ArrayList<>(patients); // A copy of the patients list
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

    //--------- FILTER LOGIC----------
    // https://www.youtube.com/watch?v=sJ-Z9G0SDhc

    @Override
    public Filter getFilter() {
        return pFilter; // this returns the Filter object directly below, named pFilter
    }

    private Filter pFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Patient> filteredList = new ArrayList<>(); // Create New List to act as the filtered one

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(patientsFull); // If search is zero or null, return the list when it has all items
            }
            else{
                // Else, for each patient, check if the filterPattern that the user entered is equal to the firstName of the patient
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Patient patient : patientsFull)
                {
                    if(patient.getfirstName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(patient); // Add that patient to the filtered list
                    }
                }
            }

            // Add it to results or something idk
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            patients.clear(); // Remove any items in it
            patients.addAll((List)results.values); // Update patients with the new results
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener {
        void onItemClick(int position, Patient patient);
    }
}
