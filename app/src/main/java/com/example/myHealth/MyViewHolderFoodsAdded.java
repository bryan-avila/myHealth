package com.example.myHealth;

import android.widget.TextView;
import android.view.View;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderFoodsAdded extends RecyclerView.ViewHolder {

    TextView foodAddedName, foodAddedQuantity;
    TextView thisFoodPhosHeader, thisFoodPhosAmount;

    public MyViewHolderFoodsAdded(@NonNull View itemView)
    {
        super(itemView);
        foodAddedName = itemView.findViewById(R.id.text_view_food_added_name);
        foodAddedQuantity = itemView.findViewById(R.id.text_view_food_added_quantity);
        thisFoodPhosHeader = itemView.findViewById(R.id.text_view_added_food_card_phos_header);
        thisFoodPhosAmount = itemView.findViewById(R.id.text_view_added_food_card_phos_amt);
    }
}
