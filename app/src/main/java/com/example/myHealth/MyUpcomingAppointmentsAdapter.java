package com.example.myHealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// MyAdapter.java
public class MyUpcomingAppointmentsAdapter extends RecyclerView.Adapter<MyViewHolderUpcomingAppointment>{

    Context context;
    List<UpcomingAppointments> UpcomingAppointments;
    private OnItemClickListener mListener;

    public MyUpcomingAppointmentsAdapter(Context context, List<UpcomingAppointments> UpcomingAppointments) {
        this.context = context;
        this.UpcomingAppointments = UpcomingAppointments;
    }

    @NonNull
    @Override
    public MyViewHolderUpcomingAppointment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolderUpcomingAppointment(LayoutInflater.from(context).inflate(R.layout.upcoming_appointment_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderUpcomingAppointment holder, int position) {
        holder.clinicName.setText(UpcomingAppointments.get(position).getClinicName());
        holder.startTime.setText(UpcomingAppointments.get(position).getStartTime());
        holder.endTime.setText(UpcomingAppointments.get(position).getEndTime());
        holder.recurring.setText(UpcomingAppointments.get(position).getRecurring());



        // Set click listener
        holder.itemView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onItemClick(position, UpcomingAppointments.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return UpcomingAppointments.size();
    }

    // Method to set the click listener from outside the adapter
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, UpcomingAppointments upcomingAppointments);
    }
}
