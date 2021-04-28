package com.example.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    DatabaseReference ref;
    MealAdapter mealAdapter;
    Query newRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get intent from the LoginActivity and/or MealDetailActivity
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        ref = FirebaseDatabase.getInstance().getReference("/meals");

        recyclerView = findViewById(R.id.recycler1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // To display the Recycler view linearly

        FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>().setQuery(ref, Meal.class).build();  // Make a query

        mealAdapter = new MealAdapter(options);

        recyclerView.setAdapter(mealAdapter);

        //Passing data to MealDetailActivity
        mealAdapter.setOnItemClickListener(new MealAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Meal meal) {
                Intent intent = new Intent(MenuActivity.this, MealDetailActivity.class);
                intent.putExtra("mealId", meal.getMealId());
                intent.putExtra("mealName", meal.getName());
                intent.putExtra("mealDescription", meal.getDescription());
                intent.putExtra("mealIngredients", meal.getIngredients());
                intent.putExtra("mealPrice", meal.getPrice());
                intent.putExtra("mealImage", meal.getUrl());
                intent.putExtra("email", email);
                startActivity(intent);
            }

        });

        Button viewBasket = (Button) findViewById(R.id.viewBasket);
        viewBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu_view, menu);
        MenuItem food = menu.findItem(R.id.searchFood);
        MenuItem ingredients = menu.findItem(R.id.searchIngredient);



        SearchView searchView2 = (SearchView) ingredients.getActionView();

        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search2(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search2(newText);
                return true;
            }
        });


        SearchView searchView = (SearchView) food.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                    search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void search(String s) {


        if (s.isEmpty()) {
            FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>().setQuery(ref, Meal.class).build();
            mealAdapter = new MealAdapter(options);
            mealAdapter.startListening();
            recyclerView.setAdapter(mealAdapter);
        } else {
            FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("/meals").orderByChild("name").startAt(s).endAt(s + "\uf8ff"), Meal.class)
                    .build();
            mealAdapter = new MealAdapter(options);
            mealAdapter.startListening();
            recyclerView.setAdapter(mealAdapter);


        }


        mealAdapter.setOnItemClickListener(new MealAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Meal meal) {
                Intent intent = new Intent(MenuActivity.this, MealDetailActivity.class);
                intent.putExtra("mealId", meal.getMealId());
                intent.putExtra("mealName", meal.getName());
                intent.putExtra("mealDescription", meal.getDescription());
                intent.putExtra("mealPrice", meal.getPrice());
                intent.putExtra("mealImage", meal.getUrl());
                intent.putExtra("mealIngredients", meal.getIngredients());
                startActivity(intent);
            }
        });
    }

    private void search2(String s) {


        if(s.isEmpty()) {
            FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>().setQuery(ref, Meal.class).build();
            mealAdapter = new MealAdapter(options);
            mealAdapter.startListening();
            recyclerView.setAdapter(mealAdapter);
        } else {
            FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("/meals").orderByChild("ingredients")
                            .startAt(s).endAt(s + "\uf8ff"), Meal.class)
                    .build();

            mealAdapter = new MealAdapter(options);
            mealAdapter.startListening();
            recyclerView.setAdapter(mealAdapter);


        }




        mealAdapter.setOnItemClickListener(new MealAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Meal meal) {
                Intent intent = new Intent(MenuActivity.this, MealDetailActivity.class);
                intent.putExtra("mealId", meal.getMealId());
                intent.putExtra("mealName", meal.getName());
                intent.putExtra("mealDescription", meal.getDescription());
                intent.putExtra("mealPrice", meal.getPrice());
                intent.putExtra("mealImage", meal.getUrl());
                intent.putExtra("mealIngredients", meal.getIngredients());
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





















