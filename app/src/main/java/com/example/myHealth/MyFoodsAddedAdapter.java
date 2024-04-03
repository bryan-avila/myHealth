package com.example.myHealth;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyFoodsAddedAdapter extends RecyclerView.Adapter<MyViewHolderFoodsAdded>{

    Context context;
    List<FoodsAdded> foodsAddedList;
    private OnItemClickListener myListener;
    RecyclerView recyclerView;
    int myExpandedPosition = -1;
    int previousExpandedPosition = -1;

    public MyFoodsAddedAdapter(Context context, List<FoodsAdded> foodsAddedList, RecyclerView recyclerView)
    {
        this.context = context;
        this.foodsAddedList = foodsAddedList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MyViewHolderFoodsAdded onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foods_added_item_view, parent, false);
        MyViewHolderFoodsAdded new_view_holder = null;
        new_view_holder = new MyViewHolderFoodsAdded(LayoutInflater.from(context).inflate(R.layout.foods_added_item_view, parent, false));

        if(viewType == 1)
        {
           // new_view_holder.foodAddedName.setVisibility(View.VISIBLE);
           // new_view_holder.foodAddedQuantity.setVisibility(View.VISIBLE);
            new_view_holder.thisFoodPhosHeader.setVisibility(View.VISIBLE);
            new_view_holder.thisFoodPhosAmount.setVisibility(View.VISIBLE);

        }
        else {
          //  new_view_holder.foodAddedName.setVisibility(View.VISIBLE);
           //  new_view_holder.foodAddedQuantity.setVisibility(View.VISIBLE);
            new_view_holder.thisFoodPhosHeader.setVisibility(View.GONE);
            new_view_holder.thisFoodPhosAmount.setVisibility(View.GONE);

        }

        return new_view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderFoodsAdded holder, int position) {
        holder.foodAddedName.setText(foodsAddedList.get(position).getFoodName());
        holder.foodAddedQuantity.setText(foodsAddedList.get(position).getQuantity());
      //  holder.thisFoodPhosHeader.setText(foodsAddedList.get(position).getCard_phos_header());
        //holder.thisFoodPhosAmount.setText(foodsAddedList.get(position).getPhosphorus());

        final boolean isExpanded = position == myExpandedPosition;
      //  holder.foodAddedName.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
       //  holder.foodAddedQuantity.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.thisFoodPhosHeader.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.thisFoodPhosAmount.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
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

    public List<FoodsAdded> getFoodsAddedList()
    {
        return foodsAddedList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, FoodsAdded foodsAddedList);
    }
}
