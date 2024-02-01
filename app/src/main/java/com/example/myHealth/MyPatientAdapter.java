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

    public List<Patient> getPatientsFilter = new ArrayList<>();
    private OnItemClickListener mListener;

    public MyPatientAdapter(Context context, List<Patient> patients) {
        this.context = context;
        this.getPatientsFilter = patients;
        this.patients = patients;
    }
    //For search bar functionality
    public void filterList(List<Patient> filteredList) {
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

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.values = getPatientsFilter;
                    filterResults.count = getPatientsFilter.size();
                } else {
                    String searchString = constraint.toString().toLowerCase();
                    List<Patient> patientList = new ArrayList<>();
                    for (Patient patient: getPatientsFilter) {
                        if (patient.getfirstName().toLowerCase().contains(searchString) || patient.getlastName().toLowerCase().contains(searchString) || patient.getEmail().toLowerCase().contains(searchString) || patient.getPhone().toLowerCase().contains(searchString)) {
                            patientList.add(patient);
                        }
                    }
                    filterResults.values = patientList;
                    filterResults.count = patientList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                patients = (List<Patient>)results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Patient patient);
    }
}
