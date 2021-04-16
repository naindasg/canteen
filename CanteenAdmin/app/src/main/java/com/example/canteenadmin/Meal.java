package com.example.canteenadmin;

public class Meal {
    private String id;
    private String mealID;
    private String name;
    private String description;
    private String ingredients;
    private String price;
    private String url;

    public Meal(String mealID, String name, String description, String price, String url) {
        this.mealID = mealID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.url = url;
    }

    public Meal(String name, String description, String price, String url) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.url = url;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getMealId() {
        return mealID;
    }

    public void setMealId(String mealID) {
        this.mealID = mealID;
    }

    public Meal() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
