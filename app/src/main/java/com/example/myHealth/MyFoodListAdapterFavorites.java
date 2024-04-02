package com.example.myHealth;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyFoodListAdapterFavorites extends RecyclerView.Adapter<MyViewHolderFoodList> implements Filterable {


    Context context;
    ImageButton favorites_Btn;

    private View.OnClickListener mListenerImage;

    public MyFoodListAdapterFavorites(Context context, ImageButton favorites_Btn) {
        this.favorites_Btn = favorites_Btn;
    }


    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public MyViewHolderFoodList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderFoodList holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setOnClickListener(View.OnClickListener listenerImage) {
        mListenerImage = listenerImage;
    }
}
