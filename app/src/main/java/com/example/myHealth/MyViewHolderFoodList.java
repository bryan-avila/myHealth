package com.example.myHealth;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderFoodList extends RecyclerView.ViewHolder {

    TextView foodName;
    ImageButton favoriteFoodButtonUnchecked;


    public MyViewHolderFoodList(@NonNull View itemView){
        super(itemView);
        foodName = itemView.findViewById(R.id.text_view_food_name);

        //Display heart button when searching for a food
        //favoriteFoodButtonUnchecked = favoriteFoodButtonUnchecked.findViewById(R.id.favorite_food_button_unchecked);
        favoriteFoodButtonUnchecked = itemView.findViewById(R.id.favorite_food_button_unchecked);

        //Set an onClick listener to change the color of the heart (user pressed the heart and it become red with the code below)

        /*favoriteFoodButtonUnchecked.setOnClickListener(new View.OnClickListener() {
            int check = 1;
            @Override
            public void onClick(View v) {
                //for changing the background color of the button
                if (check == 1) {
                    favoriteFoodButtonUnchecked.setImageResource(R.drawable.button_heart_red);
                    check = 0;
                } else  {
                    favoriteFoodButtonUnchecked.setImageResource(R.drawable.button_heart_shadow);
                    check = 1;
                }
            }
        });*/

    }

}
