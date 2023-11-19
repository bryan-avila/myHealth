package com.example.myHealth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder>{
    private ArrayList<String> timesList;

    public TimeAdapter(ArrayList<String> timesList) {
        this.timesList = timesList;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item_view, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        String time = timesList.get(position);
        holder.bind(time);
    }

    @Override
    public int getItemCount() {
        return timesList.size();
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder {

        private TextView timeTextView;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeslotLabel);
        }

        public void bind(String time) {
            timeTextView.setText(time);
        }
    }
}
