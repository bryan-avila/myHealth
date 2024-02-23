package com.example.myHealth;

import java.io.Serializable;

public class FoodNameFromList implements Serializable {

    String food_name;

    public FoodNameFromList()
    {

    }

    public FoodNameFromList(String foodName) {
        this.food_name = foodName;
    }

    public String getFood_name()
    {
        return food_name;
    }

    public void setFood_name(String foodName)
    {
        this.food_name = foodName;
    }

}
