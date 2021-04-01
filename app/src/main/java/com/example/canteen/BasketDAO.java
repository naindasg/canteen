package com.example.canteen;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BasketDAO {

//    @Query("SELECT * FROM Basket")
//    List<Basket> getAll();

    @Query("SELECT * FROM Basket")
    List<Basket> getBasket();

    @Insert
    void insertBasket(Basket... baskets);

    @Query("DELETE FROM Basket")
    void deleteAll();

    @Query("SELECT * FROM Basket WHERE meal_id = :mealId")
    Basket getBasket(String mealId);

    @Query("UPDATE Basket SET meal_quantity = meal_quantity + :mealQty WHERE meal_id = :basketId")
    void updateBasket(int basketId, int mealQty);

    @Query("UPDATE Basket SET meal_quantity = 0 WHERE meal_id = :basketId")
    void deleteBasketQuantity(int basketId);

    @Query("DELETE FROM Basket WHERE meal_id = :basketId")
    void deleteBasket(int basketId);
}
