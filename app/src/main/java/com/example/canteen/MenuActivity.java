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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    DatabaseReference ref;

    MealAdapter mealAdapter;
    ArrayList<String> urlList;
    ArrayList<String> ingredientList;
    ArrayList<String> priceList;
    ArrayList<String> mealIDList;
    ArrayList<String> mealNameList;
    ArrayList<String> descriptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get intent from the LoginActivity
        Intent intent = getIntent();
        String universityInitials = intent.getStringExtra("universityInitials");
        String email = intent.getStringExtra("email");

        //Create reference and insert data into recyclerview
        ref = FirebaseDatabase.getInstance().getReference("/meals");
        recyclerView = findViewById(R.id.menu_reycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>().setQuery(ref, Meal.class).build(); // Make a query
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
                intent.putExtra("universityInitials", universityInitials);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        //View basket
        Button viewBasket = (Button) findViewById(R.id.viewBasket);
        viewBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                intent.putExtra("universityInitials", universityInitials);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu_view, menu);

        //Getting intent from loginActivity
        Intent intent = getIntent();
        String universityInitials = intent.getStringExtra("universityInitials");
        String email = intent.getStringExtra("email");

        //Finding foodsearch and ingredientsearch view, and also Logout
        MenuItem food = menu.findItem(R.id.searchFood);
        MenuItem ingredients = menu.findItem(R.id.searchIngredient);
        MenuItem logout = menu.findItem(R.id.logoutFromMenu);

        //Handle logout
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseAuth.getInstance().signOut();

                //Clear basket upon logout
                DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference("/customer");
                customerRef.child(universityInitials).child("basket").removeValue();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                System.exit(0);
                return true;

            }
        });

        //Adding a listener to the searchIngredientView
        SearchView searchIngredientView = (SearchView) ingredients.getActionView();
        searchIngredientView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchIngredient(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchIngredient(newText.toLowerCase(), universityInitials, email);
                return true;
            }
        });

        //Adding a listener to the searchFoodView
        SearchView searchFoodView = (SearchView) food.getActionView();
        searchFoodView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                    searchFood(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFood(newText.toLowerCase(), universityInitials, email);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /*
        If there is no text in the search bar, then show the recyclerview as normal
        If there is text in the search bar, then change the recyclerview so that it shows the food that is searched for
    */
    private void searchFood(String enteredText, String universityInitials, String email) {
        if (enteredText.isEmpty()) {
            FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>().setQuery(ref, Meal.class).build();
            mealAdapter = new MealAdapter(options);
            mealAdapter.startListening();
            recyclerView.setAdapter(mealAdapter);
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
                    intent.putExtra("universityInitials", universityInitials);
                    intent.putExtra("email", email);
                    Log.d("TAG", "onItemClick: " + email);
                    startActivity(intent);
                }
            });
        } else {
            FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("/meals").orderByChild("name").startAt(enteredText).endAt(enteredText + "\uf8ff"), Meal.class)
                    .build();
            mealAdapter = new MealAdapter(options);
            mealAdapter.startListening();
            recyclerView.setAdapter(mealAdapter);
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
                    intent.putExtra("universityInitials", universityInitials);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            });
        }
    }

    /*
        If there is no text in the search bar, then show the recyclerview as normal
        If there is text in the search bar, then change the recyclerview so that it shows the food that contains the ingredients searched for.
    */
    private void searchIngredient(String enteredText, String universityInitials, String email) {
        if (enteredText.isEmpty()) {
            FirebaseRecyclerOptions<Meal> options = new FirebaseRecyclerOptions.Builder<Meal>().setQuery(ref, Meal.class).build();
            mealAdapter = new MealAdapter(options);
            mealAdapter.startListening();
            recyclerView.setAdapter(mealAdapter);
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
                    intent.putExtra("universityInitials", universityInitials);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            });
        } else {
            //Declaring reference to meals
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/meals");

            //Creating a list to store each detail of the searched food via ingredient
            mealIDList = new ArrayList<>();
            mealNameList = new ArrayList<>();
            descriptionList = new ArrayList<>();
            ingredientList = new ArrayList<>();
            priceList = new ArrayList<>();
            urlList = new ArrayList<>();

            //Add a listener to the reference
            //Search through the database, if any meal has ingredients that contains the ingredient that is searched for, save the meals details into arrayList.
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String ingredients = dataSnapshot.child("ingredients").getValue().toString();
                            if (ingredients.contains(enteredText)) {
                                String mealID = dataSnapshot.child("mealID").getValue().toString();
                                String name = dataSnapshot.child("name").getValue().toString();
                                String description = dataSnapshot.child("description").getValue().toString();
                                String ingredientsFound = dataSnapshot.child("ingredients").getValue().toString();
                                String price = dataSnapshot.child("price").getValue().toString();
                                String url = dataSnapshot.child("url").getValue().toString();

                                mealIDList.add(mealID);
                                mealNameList.add(name);
                                descriptionList.add(description);
                                ingredientList.add(ingredientsFound);
                                priceList.add(price);
                                urlList.add(url);
                            }

                            IngredientAdapter ingredientAdapter = new IngredientAdapter(mealIDList,
                                    mealNameList, descriptionList, ingredientList,
                                    priceList, urlList, MenuActivity.this, universityInitials, email);
                            recyclerView.setAdapter(ingredientAdapter);
                        }
                    }

                }

                //Error searching for ingredient
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("searchIngredient", "onCancelled: " + error.getMessage());
                }
            });
        }
    }

    //To avoid pressing back to loginPage
    @Override
    public void onBackPressed() {
        //Nothing happens
    }

    //Retrieve data from the database
    @Override
    protected void onStop() {
        super.onStop();
        mealAdapter.stopListening();
    }

    //Stop retrieving data from the database.
    @Override
    protected void onStart() {
        super.onStart();
        mealAdapter.startListening();
    }
}





















