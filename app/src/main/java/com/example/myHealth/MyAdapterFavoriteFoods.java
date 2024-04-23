package com.example.myHealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterFavoriteFoods extends RecyclerView.Adapter<MyViewHolderFavoriteFoods> {


    // Global stuff
    Context context;
    List<FavoriteFoods> favoriteFoods;
    private MyAdapterFavoriteFoods.OnItemClickListener mListener;

    public MyAdapterFavoriteFoods(Context context, List<FavoriteFoods> favoriteFoods)
    {
        this.context = context;
        this.favoriteFoods = favoriteFoods;
    }


    @NonNull
    @Override
    public MyViewHolderFavoriteFoods onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolderFavoriteFoods(LayoutInflater.from(context).inflate(R.layout.item_view_foods_list_favorites, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderFavoriteFoods holder, int position) {
        holder.favFood.setText(favoriteFoods.get(position).getFoodName());

        holder.itemView.setOnClickListener(view -> {

                if(mListener != null)
                {
                    mListener.onItemClick(position, favoriteFoods.get(position));
                }


        });
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(int position, FavoriteFoods favoriteFoods);
    }

    @Override
    public int getItemCount() {
        return favoriteFoods.size();
    }
}
