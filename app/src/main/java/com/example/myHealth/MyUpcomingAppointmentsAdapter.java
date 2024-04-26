package com.example.myHealth;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.List;

// MyAdapter.java
public class MyUpcomingAppointmentsAdapter extends RecyclerView.Adapter<MyUpcomingAppointmentsAdapter.UpcomingAppointmentViewholder>{
    FirebaseFirestore db = MyFirestore.getDBInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    Context context;
    List<Appointment> appointments;
    RecyclerView recyclerView;
    int mExpandedPosition = -1;
    int previousExpandedPosition = -1;
    private OnItemClickListener mListener;

    public MyUpcomingAppointmentsAdapter(Context context, List<Appointment> appointments, RecyclerView recyclerView) {
        this.context = context;
        this.appointments = appointments;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public UpcomingAppointmentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item_view, parent, false);

        // create a new view
        UpcomingAppointmentViewholder vh = null;
        vh = new UpcomingAppointmentViewholder(view);

        Log.e("viewType: ", ""+viewType);
        if (viewType == 1) {
            vh.clinicName.setVisibility(View.VISIBLE);
            vh.date.setVisibility(View.VISIBLE);
            vh.start_and_end_times.setVisibility(View.VISIBLE);
            vh.recurring.setVisibility(View.VISIBLE);
            vh.edit_button.setVisibility(View.VISIBLE);
            vh.delete_button.setVisibility(View.VISIBLE);
        } else {
            vh.clinicName.setVisibility(View.GONE);
            vh.date.setVisibility(View.GONE);
            vh.start_and_end_times.setVisibility(View.GONE);
            vh.recurring.setVisibility(View.GONE);
            vh.edit_button.setVisibility(View.GONE);
            vh.delete_button.setVisibility(View.GONE);
        }
        return vh;
    }

    public void setOnItemClickListener(OnItemClickListener listener) { mListener = listener; }

    public interface OnItemClickListener {
        void onItemClick(int position, Clinic clinic);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingAppointmentViewholder holder, int position) {
        String title = appointments.get(holder.getBindingAdapterPosition()).getClinicName() + " (" + appointments.get(holder.getBindingAdapterPosition()).getDate() + ")";
        holder.bind(title);

        holder.clinicName.setText("Clinic: " + appointments.get(holder.getBindingAdapterPosition()).getClinicName());
        holder.date.setText("Date: " + appointments.get(holder.getBindingAdapterPosition()).getDate());
        holder.start_and_end_times.setText("Time: " + appointments.get(holder.getBindingAdapterPosition()).getStartTime() + " - " + appointments.get(holder.getBindingAdapterPosition()).getEndTime());
        if (appointments.get(holder.getBindingAdapterPosition()).getRecurring())
            holder.recurring.setText("Recurring: Yes");
        else
            holder.recurring.setText("Recurring: No");

        holder.clinicName.setPaintFlags(holder.clinicName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        holder.date.setPaintFlags(holder.date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.start_and_end_times.setPaintFlags(holder.start_and_end_times.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.recurring.setPaintFlags(holder.recurring.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        AppointmentManager appointmentManager = new AppointmentManager();
        DocumentReference documentPath = appointments.get(holder.getBindingAdapterPosition()).getDocumentPath();
        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //take to page to edit date and time
                Intent intent = new Intent(context, edit_appointment_page.class);
                intent.putExtra("appointmentPath", appointments.get(holder.getBindingAdapterPosition()).getDocumentPath().getPath());
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Log.d("edit button", "clicked");
            }
        });
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deletes appointment
                appointmentManager.deleteAppointment(appointments.get(holder.getBindingAdapterPosition()), appointments.get(holder.getBindingAdapterPosition()).getDocumentPath());
                Log.d("delete button", "clicked");
                //refresh recyclerview
                appointments.remove(holder.getBindingAdapterPosition());
                notifyItemRemoved(holder.getBindingAdapterPosition());
            }
        });

        final boolean isExpanded = holder.getBindingAdapterPosition() == mExpandedPosition;
        holder.clinicName.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.date.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.start_and_end_times.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.recurring.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        if (appointments.get(holder.getBindingAdapterPosition()).getComplete() && isExpanded) {
            holder.edit_button.setVisibility(View.GONE);
            holder.delete_button.setVisibility(View.GONE);
        } else if (!appointments.get(holder.getBindingAdapterPosition()).getComplete() && isExpanded) {
            holder.edit_button.setVisibility(View.VISIBLE);
            holder.delete_button.setVisibility(View.VISIBLE);
        } else {
            holder.edit_button.setVisibility(View.GONE);
            holder.delete_button.setVisibility(View.GONE);
        }

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
    public int getItemCount() {
        return appointments.size();
    }

    public static class UpcomingAppointmentViewholder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        public CardView cv;
        private TextView clinicName;
        private TextView date;
        private TextView start_and_end_times;
        private TextView recurring;
        private Button edit_button;
        private Button delete_button;

        public UpcomingAppointmentViewholder(@NonNull View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cardView);
            clinicName = itemView.findViewById(R.id.clinicName);
            date = itemView.findViewById(R.id.date);
            start_and_end_times = itemView.findViewById(R.id.start_and_end_times);
            recurring = itemView.findViewById(R.id.recurring);
            edit_button = itemView.findViewById(R.id.edit_button);
            delete_button = itemView.findViewById(R.id.delete_button);

            titleTextView = itemView.findViewById(R.id.appointment_title);
        }

        public void bind(String title) {
            titleTextView.setText(title);
        }
    }

}
