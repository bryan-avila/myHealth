package com.example.myHealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyFoodListAdapterFavorites extends RecyclerView.Adapter<MyViewHolderFoodListFavorites> {


    Context context;
    ImageButton favorites_Btn;

    private OnClickListener mListenerImage;

    public MyFoodListAdapterFavorites(Context context, ImageButton favoritesBtn) {
        this.context = context;
        this.favorites_Btn = favoritesBtn;
    }


    @NonNull
    @Override
    public MyViewHolderFoodListFavorites onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolderFoodListFavorites(LayoutInflater.from(context).inflate(R.layout.item_view_foods_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderFoodListFavorites holder, int position) {
        holder.favoriteFoodButtonChecked.setImageResource(R.drawable.button_heart_red);

        holder.itemView.setOnClickListener(v -> {
            if (mListenerImage != null) {
                mListenerImage.onClick(favorites_Btn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /*public void setOnClickListener(View.OnClickListener listenerImage) {
        mListenerImage = listenerImage;
    }*/

    public void setOnClickListener(OnClickListener onClickListener) {
        mListenerImage = onClickListener;
    }

    public interface OnClickListener {
        void onClick(ImageButton favoritesBtn);
    }
}
