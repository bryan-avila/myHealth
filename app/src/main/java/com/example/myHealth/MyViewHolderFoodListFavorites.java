package com.example.myHealth;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderFoodListFavorites extends RecyclerView.ViewHolder {
    TextView favoriteFoodName;

    ImageButton favoriteFoodButtonChecked;

    public MyViewHolderFoodListFavorites(@NonNull View itemView) {
        super(itemView);
        favoriteFoodName = itemView.findViewById(R.id.text_view_food_name_favorite);

        //Display heart button checked when favorites tab is accessed
        favoriteFoodButtonChecked = itemView.findViewById(R.id.favorite_food_button_checked);
    }
}
