package com.example.myHealth;

import java.util.HashMap;

public class FavoriteFoodNameFromList {

    String food_name;
    HashMap<String, Object> favoriteFoodNutrientInfo = new HashMap<>();

    public FoodNameFromList()
    {
        favoriteFoodNutrientInfo = null;
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

    public boolean checkHeartStatus() {return isHeartRed;}

    public void setHeartRed(boolean value){
        this.isHeartRed = value;
    }

}
