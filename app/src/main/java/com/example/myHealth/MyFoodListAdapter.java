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

public class MyFoodListAdapter extends RecyclerView.Adapter<MyViewHolderFoodList> implements Filterable {

    Context context;

    List<FoodNameFromList> food_names;

    List<FoodNameFromList> food_names_full;

    private OnItemClickListener mListener;

    public MyFoodListAdapter(Context context, List<FoodNameFromList> foodnames) {
        this.context = context;
        this.food_names = foodnames;
        food_names_full = new ArrayList<>(foodnames); // A copy of the food list
    }

    @NonNull
    @Override
    public MyViewHolderFoodList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolderFoodList(LayoutInflater.from(context).inflate(R.layout.item_view_foods_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderFoodList holder, int position) {
        holder.foodName.setText(food_names.get(position).getFood_name());


        // Set click listener
        holder.itemView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onItemClick(position, food_names.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return food_names.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    public Filter getFilter() {
        return pFilter; // this returns the Filter object directly below, named pFilter
    }

    private Filter pFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FoodNameFromList> filteredList = new ArrayList<>(); // Create New List to act as the filtered one

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(food_names_full); // If search is zero or null, return the list when it has all items
            }
            else{
                // Else, for each food name, check if the filterPattern that the user entered is equal to the food name of the food
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(FoodNameFromList food_name: food_names_full)
                {
                    if(food_name.getFood_name().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(food_name); // Add that food name to the filtered list
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
            food_names.clear(); // Remove any items in it
            food_names.addAll((List)results.values); // Update food names with the new results
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener {
        void onItemClick(int position, FoodNameFromList foodnames);
    }

}
