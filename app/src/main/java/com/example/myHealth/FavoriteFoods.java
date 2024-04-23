package com.example.myHealth;

import java.io.Serializable;

public class FavoriteFoods implements Serializable {

    String foodName;

    public FavoriteFoods()
    {

    }

    public FavoriteFoods(String foodName)
    {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
