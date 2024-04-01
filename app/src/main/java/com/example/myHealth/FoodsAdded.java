package com.example.myHealth;

import java.io.Serializable;

public class FoodsAdded implements Serializable {

    String food;
    String quantity;

    public FoodsAdded(){

    }

    public FoodsAdded(String fName, String fQuantity)
    {
        this.food = fName;
        this.quantity = fQuantity;
    }

    public String getFoodName()
    {
        return food;
    }

    public void setFoodName(String fName)
    {
        this.food = fName;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String fQuantity)
    {
        this.quantity = fQuantity;
    }

}
