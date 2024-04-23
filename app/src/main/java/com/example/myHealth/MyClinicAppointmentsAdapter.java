package com.example.myHealth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

// MyAdapter.java
public class MyClinicAppointmentsAdapter extends RecyclerView.Adapter<MyClinicAppointmentsAdapter.ClinicAppointmentViewholder>{
    Context context;
    List<Appointment> appointments;
    RecyclerView recyclerView;
    int mExpandedPosition = -1;
    int previousExpandedPosition = -1;
    private OnItemClickListener mListener;

    public MyClinicAppointmentsAdapter(Context context, List<Appointment> appointments, RecyclerView recyclerView) {
        this.context = context;
        this.appointments = appointments;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ClinicAppointmentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clinic_appointment_item_view, parent, false);

        // create a new view
        ClinicAppointmentViewholder vh = null;
        vh = new ClinicAppointmentViewholder(view);

        Log.e("viewType: ", ""+viewType);
        if (viewType == 1) {
            vh.patientName.setVisibility(View.VISIBLE);
            vh.phone.setVisibility(View.VISIBLE);
            vh.email.setVisibility(View.VISIBLE);
            vh.date.setVisibility(View.VISIBLE);
            vh.start_and_end_times.setVisibility(View.VISIBLE);
        } else {
            vh.phone.setVisibility(View.GONE);
            vh.email.setVisibility(View.GONE);
            vh.date.setVisibility(View.GONE);
            vh.start_and_end_times.setVisibility(View.GONE);
        }
        return vh;
    }

    public void setOnItemClickListener(OnItemClickListener listener) { mListener = listener; }

    public interface OnItemClickListener {
        void onItemClick(int position, Clinic clinic);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicAppointmentViewholder holder, int position) {
        String title = appointments.get(holder.getBindingAdapterPosition()).getFirstName() + " " + appointments.get(holder.getBindingAdapterPosition()).getLastName() + "\n(" + appointments.get(holder.getBindingAdapterPosition()).getDate() + ")";
        holder.patientName.setPaintFlags(holder.patientName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        holder.bind(title);

        holder.phone.setText(appointments.get(holder.getBindingAdapterPosition()).getPhone());
        holder.email.setText("Email: " + appointments.get(holder.getBindingAdapterPosition()).getEmail());
        holder.date.setText("Date: " + appointments.get(holder.getBindingAdapterPosition()).getDate());
        holder.start_and_end_times.setText("Time: " + appointments.get(holder.getBindingAdapterPosition()).getStartTime() + " - " + appointments.get(holder.getBindingAdapterPosition()).getEndTime());

        holder.phone.setPaintFlags(holder.phone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.email.setPaintFlags(holder.email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.date.setPaintFlags(holder.date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.start_and_end_times.setPaintFlags(holder.start_and_end_times.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        AppointmentManager appointmentManager = new AppointmentManager();
        DocumentReference documentPath = appointments.get(holder.getBindingAdapterPosition()).getDocumentPath();

        final boolean isExpanded = holder.getBindingAdapterPosition() == mExpandedPosition;
        holder.phone.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.email.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.date.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.start_and_end_times.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = holder.getBindingAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:holder.getBindingAdapterPosition();
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() { return appointments.size(); }

    public static class ClinicAppointmentViewholder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        public CardView cv;
        private TextView patientName;
        private TextView date;
        private TextView start_and_end_times;
        private TextView phone;
        private TextView email;

        public ClinicAppointmentViewholder(@NonNull View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cardView);
            patientName = itemView.findViewById(R.id.appointment_title);
            phone = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            date = itemView.findViewById(R.id.date);
            start_and_end_times = itemView.findViewById(R.id.start_and_end_times);

            titleTextView = itemView.findViewById(R.id.appointment_title);
        }

        public void bind(String title) {
            titleTextView.setText(title);
        }
    }

}
