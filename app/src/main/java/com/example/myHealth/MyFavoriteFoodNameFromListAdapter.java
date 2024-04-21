package com.example.myHealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyFavoriteFoodNameFromListAdapter extends RecyclerView.Adapter<MyFavoriteFoodNameFromListViewHolder> {

    Context context;

    List<FavoriteFoodNameFromList> favoriteFoodsList;

    private MyFavoriteFoodNameFromListAdapter.OnItemClickListener myListener;

    public MyFavoriteFoodNameFromListAdapter(Context context, List<FavoriteFoodNameFromList> favoriteFoodsList)
    {
            this.context = context;
            this.favoriteFoodsList = favoriteFoodsList;
    }

    @NonNull
    @Override
    public MyFavoriteFoodNameFromListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyFavoriteFoodNameFromListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view_foods_list_favorites, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyFavoriteFoodNameFromListViewHolder holder, int position) {
        holder.foodNameFavorite.setText(favoriteFoodsList.get(position).getFavFood_name());
        holder.test.setText(favoriteFoodsList.get(position).getFavFood_name());

        holder.itemView.setOnClickListener(view -> {
            if(myListener != null)
            {
                myListener.onItemClick(position, favoriteFoodsList.get(position));
            }
        });

    }

    public interface OnItemClickListener
    {
        void onItemClick(int position, FavoriteFoodNameFromList favoriteFoodsList);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        myListener = listener;
    }
    @Override
    public int getItemCount() {
        return favoriteFoodsList.size();
    }

}
