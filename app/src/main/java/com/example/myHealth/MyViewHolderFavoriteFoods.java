package com.example.myHealth;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderFavoriteFoods extends RecyclerView.ViewHolder {

    TextView favFood;
    public MyViewHolderFavoriteFoods(@NonNull View itemView)
    {
        super(itemView);
        favFood = itemView.findViewById(R.id.text_view_food_name_favorite);
    }
}
