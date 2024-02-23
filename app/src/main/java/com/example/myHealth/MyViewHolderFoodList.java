package com.example.myHealth;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderFoodList extends RecyclerView.ViewHolder {

    TextView foodName;

    public MyViewHolderFoodList(@NonNull View itemView){
        super(itemView);
        foodName = itemView.findViewById(R.id.text_view_food_name);

    }
}
