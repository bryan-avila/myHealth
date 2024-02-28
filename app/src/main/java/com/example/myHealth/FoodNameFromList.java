package com.example.myHealth;

import java.io.Serializable;

public class FoodNameFromList implements Serializable {

    String food_name;

    String food_id;

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

    public String getFood_id()
    {
        return food_id;
    }

    public void setFood_id(String foodid)
    {
        this.food_id = foodid;
    }

}
