package com.example.canteen;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Basket {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "meal_id")
    private String mealId;

    @ColumnInfo(name = "meal_name")
    private String mealName;

    @ColumnInfo(name = "meal_price")
    private float mealPrice;

    @ColumnInfo(name = "meal_quantity")
    private int mealQuantity;

    @ColumnInfo(name = "meal_description")
    private String mealDescription;

    @ColumnInfo(name = "meal_image")
    private String mealImage;

    public String getMealImage() {
        return mealImage;
    }

    public void setMealImage(String mealImage) {
        this.mealImage = mealImage;
    }

    public String getMealDescription() {
        return mealDescription;
    }

    public void setMealDescription(String mealDescription) {
        this.mealDescription = mealDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public float getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(float mealPrice) {
        this.mealPrice = mealPrice;
    }

    public int getMealQuantity() {
        return mealQuantity;
    }

    public void setMealQuantity(int mealQuantity) {
        this.mealQuantity = mealQuantity;
    }
}
