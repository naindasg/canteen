package com.example.canteen;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Basket.class}, version = 8)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase db;
    public abstract BasketDAO basketDAO();

    public static AppDatabase getAppDatabase(Context context) {
        if (db == null) {
             db = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "database-name").fallbackToDestructiveMigration().build();
        }

        return db;
    }

}



