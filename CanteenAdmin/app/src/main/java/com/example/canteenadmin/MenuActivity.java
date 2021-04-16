package com.example.canteenadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    //MealAdapterClass adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the Firebase Realtime Database
    SearchView searchView;
    ArrayList<Meal> list;
    MealAdapter mealAdapter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_recycler);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
//          setSupportActionBar(toolbar);
////          ActionBar actionBar = getSupportActionBar();
////          actionBar.setDisplayHomeAsUpEnabled(true); //Shows the menu icon
////          //actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24); // menu icon
////        getSupportActionBar().setDisplayShowTitleEnabled(false); //Removes the 'Canteen' subtitle

        // Create a instance of the database and get its reference
        mbase = FirebaseDatabase.getInstance().getReference("/meals");

        //Find the recylerView by it's ID
        recyclerView = findViewById(R.id.menu_recyclerview);
//        searchView = findViewById(R.id.searchView);


        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>().setQuery(mbase, Meal.class).build();
//
        // Connecting object of required Adapter class to the Adapter class itself
        mealAdapter = new MealAdapter(options);

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(mealAdapter);


        //Passing data to MealDetailActivity
        mealAdapter.setOnItemClickListener(new MealAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Meal meal) {
                Intent intent = new Intent(MenuActivity.this, UpdateActivity.class);
                intent.putExtra("mealId", meal.getMealId());
                intent.putExtra("mealName", meal.getName());
                intent.putExtra("mealDescription", meal.getDescription());
                intent.putExtra("mealPrice", meal.getPrice());
                intent.putExtra("mealImage", meal.getUrl());
                intent.putExtra("mealIngredients", meal.getIngredients());
                startActivity(intent);
            }

        });

        Button addFood = (Button) findViewById(R.id.addFood);
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentget = getIntent();
                String mealName = intentget.getStringExtra("mealName");
                Intent intent = new Intent(getApplicationContext(), AddFoodActivity.class);
                intent.putExtra("screen", "tray");
                intent.putExtra("mealName", mealName);
                startActivity(intent);

            }
        });

        Button goBack = (Button) findViewById(R.id.goBackToOrders);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CustomerNamesActivity.class);
                startActivity(intent);
            }
        });


    }




    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        mealAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mealAdapter.startListening();
    }
}





















