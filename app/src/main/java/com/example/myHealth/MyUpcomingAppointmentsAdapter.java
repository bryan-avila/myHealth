package com.example.myHealth;

import android.content.Context;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// MyAdapter.java
public class MyUpcomingAppointmentsAdapter extends RecyclerView.Adapter<MyUpcomingAppointmentsAdapter.UpcomingAppointmentViewholder>{
    Context context;
    List<String> UpcomingAppointments;
    RecyclerView recyclerView;
    int mExpandedPosition = -1;
    int previousExpandedPosition = -1;
    private OnItemClickListener mListener;

    public MyUpcomingAppointmentsAdapter(Context context, List<String> UpcomingAppointments, RecyclerView recyclerView) {
        this.context = context;
        this.UpcomingAppointments = UpcomingAppointments;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public UpcomingAppointmentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_appointment_item_view, parent, false);

        // create a new view
        UpcomingAppointmentViewholder vh = null;
        vh = new UpcomingAppointmentViewholder(view);

        Log.e("viewType: ", ""+viewType);
        if (viewType == 1)
            vh.details.setVisibility(View.VISIBLE);
        else
            vh.details.setVisibility(View.GONE);

        return vh;
    }

    public void setOnItemClickListener(OnItemClickListener listener) { mListener = listener; }

    public interface OnItemClickListener {
        void onItemClick(int position, Clinic clinic);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingAppointmentViewholder holder, int position) {
        String date = UpcomingAppointments.get(position);
        holder.bind(date);

        final boolean isExpanded = position == mExpandedPosition;
        holder.details.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return UpcomingAppointments.size();
    }

    public static class UpcomingAppointmentViewholder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        public CardView cv;
        private TextView details;

        public UpcomingAppointmentViewholder(@NonNull View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cardView);
            details = itemView.findViewById(R.id.details);

            dateTextView = itemView.findViewById(R.id.text_view_date);
        }

        public void bind(String date) {
            dateTextView.setText(date);
        }
    }
}
