package com.example.myHealth;

import java.io.Serializable;
import java.util.HashMap;

public class FavoriteFoodNameFromList implements Serializable {

    String fav_food_name;
    String test;

    public FavoriteFoodNameFromList()
    {
    }

    public FavoriteFoodNameFromList(String foodName, String test) {
        this.fav_food_name = foodName;
        this.test = test;
    }

    public String getFavFood_name()
    {
        return fav_food_name;
    }

    public void setFavFood_name(String foodName)
    {
        this.fav_food_name = foodName;
    }

}
