package com.example.myHealth;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyFoodsAddedAdapter extends RecyclerView.Adapter<MyViewHolderFoodsAdded>{

    Context context;
    List<FoodsAdded> foodsAddedList;
    private OnItemClickListener myListener;
    public MyFoodsAddedAdapter(Context context, List<FoodsAdded> foodsAddedList)
    {
        this.context = context;
        this.foodsAddedList = foodsAddedList;
    }

    @NonNull
    @Override
    public MyViewHolderFoodsAdded onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolderFoodsAdded(LayoutInflater.from(context).inflate(R.layout.foods_added_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderFoodsAdded holder, int position) {
        holder.foodAddedName.setText(foodsAddedList.get(position).getFoodName());
        holder.foodAddedQuantity.setText(foodsAddedList.get(position).getQuantity());

        holder.itemView.setOnClickListener(view -> {
            if(myListener != null)
            {
                myListener.onItemClick(position, foodsAddedList.get(position));
            }
        });

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        myListener = listener;
    }

    @Override
    public int getItemCount() {
        return foodsAddedList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, FoodsAdded foodsAddedList);
    }
}
