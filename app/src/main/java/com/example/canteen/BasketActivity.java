package com.example.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class BasketActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    DatabaseReference basketRef;
    DatabaseReference orderRef;
    Button buttonAddPayment;
    DatabaseReference mbase;
    BasketAdapter adapter;
    DatabaseReference customerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        //Getting intent from MenuActivity
        Intent intent = getIntent();
        String universityInitials = intent.getStringExtra("universityInitials");
        String email = intent.getStringExtra("email");

        //Create reference and insert data into recyclerView
        mbase = FirebaseDatabase.getInstance().getReference("/customer/" + universityInitials + "/basket");
        recyclerView = findViewById(R.id.recycler_basket);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Basket> options = new FirebaseRecyclerOptions.Builder<Basket>().setQuery(mbase, Basket.class).build();
        adapter = new BasketAdapter(options);
        recyclerView.setAdapter(adapter);

        //Create references
        basketRef = FirebaseDatabase.getInstance().getReference("/customer/" + universityInitials + "/basket");

        //Change colour of title
        TextView textView = (TextView) findViewById(R.id.yourOrderTitle);
        textView.setTextColor(getResources().getColor(R.color.purple_500));

        //Calculate total
        ArrayList<Float> totalArray = new ArrayList();
        basketRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Look through each item in the basket, obtain the price and quantity, and then add it to the totalArray
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String price = dataSnapshot.child("price").getValue().toString();
                        String quantity = dataSnapshot.child("quantity").getValue().toString();
                        totalArray.add(Float.parseFloat(price) * Integer.parseInt(quantity));
                    }

                    //Get priceView and calculate total by adding all values in totalArrayList
                    TextView priceView = (TextView) findViewById(R.id.basket_total);
                    float totalPrice = 0;
                    for (int i = 0; i < totalArray.size(); i++) {
                        totalPrice = totalPrice + totalArray.get(i);
                    }

                    priceView.setText("£" + String.format("%.2f", totalPrice));
                }
            }

            //Error retrieving data from database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("BasketActivity", "onCancelled: " + error.getMessage());
            }
        });

        //Create intent and attach data for UpdateMealActivity
        adapter.setOnItemClickListener(new BasketAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Basket basket) {
                Intent intent = new Intent(getApplicationContext(), UpdateMealActivity.class);
                intent.putExtra("mealId", basket.getMealID());
                intent.putExtra("mealName", basket.getName());
                intent.putExtra("mealPrice", basket.getPrice());
                intent.putExtra("mealDescription", basket.getDescription());
                intent.putExtra("mealIngredients", basket.getIngredients());
                intent.putExtra("mealImage", basket.getUrl());
                intent.putExtra("universityInitials", universityInitials);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        buttonAddPayment = (Button) findViewById(R.id.button_add_payment_new);
        buttonAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the total price from priceView
                TextView priceView = (TextView) findViewById(R.id.basket_total);
                String price = priceView.getText().toString();

                if(!price.equals("The basket is empty")) {
                    //Create intent and pass price and email to CheckoutActivityJava
                    Intent intent = new Intent(getApplicationContext(), CheckoutActivityJava.class);
                    String priceSubString = price.substring(1, 5);
                    intent.putExtra("mealPrice", priceSubString);
                    intent.putExtra("universityInitials", universityInitials);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Your basket is empty", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });

        //Handle clear basket
        Button clearBasket = (Button) findViewById(R.id.clear_basket);
        clearBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basketRef.removeValue();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("universityInitials", universityInitials);
                intent.putExtra("email", email);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Basket cleared", Toast.LENGTH_SHORT).show();
            }
        });

        //Handle back to menu
        Button backToMenu = (Button) findViewById(R.id.BackToMenu);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("universityInitials", universityInitials);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    //Retrieve data from database
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    //Stop retrieving data from database
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);

        MenuItem logout = menu.findItem(R.id.logoutFromActivity);

        Intent getIntent = getIntent();
        String universityInitials = getIntent.getStringExtra("universityInitials");

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

        return super.onCreateOptionsMenu(menu);
    }


}