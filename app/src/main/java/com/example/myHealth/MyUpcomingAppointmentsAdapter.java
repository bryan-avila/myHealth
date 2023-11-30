package com.example.myHealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// MyAdapter.java
public class MyUpcomingAppointmentsAdapter extends RecyclerView.Adapter<MyUpcomingAppointmentsAdapter.UpcomingAppointmentViewholder>{

    Context context;
    List<String> UpcomingAppointments;

    public MyUpcomingAppointmentsAdapter(Context context, List<String> UpcomingAppointments) {
        this.context = context;
        this.UpcomingAppointments = UpcomingAppointments;
    }

    @NonNull
    @Override
    public UpcomingAppointmentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_appointment_item_view, parent, false);
        return new UpcomingAppointmentViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingAppointmentViewholder holder, int position) {
        String date = UpcomingAppointments.get(position);
        holder.bind(date);
    }

    @Override
    public int getItemCount() {
        return UpcomingAppointments.size();
    }

    public static class UpcomingAppointmentViewholder extends RecyclerView.ViewHolder {
        private TextView dateTextView;

        public UpcomingAppointmentViewholder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.text_view_date);
        }

        public void bind(String date) {
            dateTextView.setText(date);
        }
    }
}
