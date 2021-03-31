package com.example.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    MealAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the Firebase Realtime Database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a instance of the database and get its reference
        mbase = FirebaseDatabase.getInstance().getReference();

        //Find the recylerView by it's ID
        recyclerView = findViewById(R.id.recycler1);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>().setQuery(mbase, Meal.class).build();

        // Connecting object of required Adapter class to the Adapter class itself
        adapter = new MealAdapter(options);

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        //Passing data to MealDetailActivity
        adapter.setOnItemClickListener(new MealAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Meal meal) {
                Intent intent = new Intent(MenuActivity.this, MealDetailActivity.class);
                intent.putExtra("mealId", meal.getMealId());
                intent.putExtra("mealName", meal.getName());
                intent.putExtra("mealDescription", meal.getDescription());
                intent.putExtra("mealPrice", meal.getPrice());
                intent.putExtra("mealImage", meal.getUrl());
                startActivity(intent);
            }

        });


        Button viewBasket = (Button) findViewById(R.id.viewBasket);
        viewBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentget = getIntent();
                String mealName = intentget.getStringExtra("mealName");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("screen", "tray");
                intent.putExtra("mealName", mealName);
                startActivity(intent);

            }
        });

    }

    // Function to tell the app to start getting data from database on starting of the activity
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting data from database on stoping of the activity
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {

    }


}





















