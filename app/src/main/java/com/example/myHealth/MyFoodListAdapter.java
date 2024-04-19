package com.example.myHealth;

import static java.util.Objects.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyFoodListAdapter extends RecyclerView.Adapter<MyViewHolderFoodList> implements Filterable {

    //ImageButton favorites_Buttons;
    Context context;

    List<FoodNameFromList> food_names;

    List<FoodNameFromList> food_names_full;

    List<ImageButton> fav_Buttons;

    private OnItemClickListener mListener;

    private OnItemClickListener mListenerImage;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = MyFirestore.getmAuthInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();



    public MyFoodListAdapter(Context context, List<FoodNameFromList> foodnames) {
        this.context = context;
        this.food_names = foodnames;
        //had List<ImageButton> favButtons as an argument
        //this.fav_Buttons = favButtons;
        food_names_full = new ArrayList<>(foodnames); // A copy of the food list

        /*
        favorite_Food_Button_Unchecked.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    favorite_Food_Button_Unchecked.setBackgroundResource(R.drawable.button_heart_shadow);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    favorite_Food_Button_Unchecked.setBackgroundResource(R.drawable.button_heart_red);
                }
                return false;
            }
        });*/
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


        // The heart button will change from gray to red. It will also
        holder.favoriteFoodButtonUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                // Set up collection to hold documents that will be the list of foods
                CollectionReference patientFavoriteFoodsRef = db.collection("users").document(currentUser.getUid()).collection("favoriteFoods");

                if(food_names.get(position).checkHeartStatus() == false)
                {
                    holder.favoriteFoodButtonUnchecked.setImageResource(R.drawable.button_heart_red);
                    Toast.makeText(view.getContext(), food_names.get(position).getFood_name() + " testing lol", Toast.LENGTH_SHORT).show();
                    Map<String, Object> favoriteFoodAdded = new HashMap<>();
                    favoriteFoodAdded.put("phosphorus", 0);
                    favoriteFoodAdded.put("protein", 0);
                    favoriteFoodAdded.put("potassium", 0);
                    patientFavoriteFoodsRef.document(food_names.get(position).getFood_name()).set(favoriteFoodAdded);
                }

                else {
                    holder.favoriteFoodButtonUnchecked.setImageResource(R.drawable.button_heart_shadow);
                    Toast.makeText(view.getContext(), food_names.get(position).getFood_id() + " removed lol", Toast.LENGTH_SHORT).show();
                }
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
