package com.example.myHealth;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyFavoriteFoodNameFromListViewHolder extends RecyclerView.ViewHolder {

    TextView foodNameFavorite;
    TextView test;

    public MyFavoriteFoodNameFromListViewHolder (@NonNull View itemView)
    {
        super(itemView);
        foodNameFavorite = itemView.findViewById(R.id.text_view_food_name_favorite);
        test = itemView.findViewById(R.id.text_view_test);
    }
}
