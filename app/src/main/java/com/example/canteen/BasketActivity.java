package com.example.canteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;

public class BasketActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    DatabaseReference ref; // Create object of the Firebase Realtime Database
    DatabaseReference newref;
    Button buttonAddPayment;
    DatabaseReference mbase;
    BasketAdapter adapter;
    DatabaseReference customerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        //Getting email from MenuActivity
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String emailSub = email.substring(0, 4);

        mbase = FirebaseDatabase.getInstance().getReference("/customer/" + emailSub + "/basket");

        recyclerView = findViewById(R.id.recycler_basket);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Basket> options = new FirebaseRecyclerOptions.Builder<Basket>().setQuery(mbase, Basket.class).build();

        adapter = new BasketAdapter(options);

        recyclerView.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference("/customer/" + emailSub + "/basket");
        newref = FirebaseDatabase.getInstance().getReference("/orders");
        customerRef = FirebaseDatabase.getInstance().getReference("/customerName");

        TextView textView = (TextView) findViewById(R.id.yourOrderTitle);
        textView.setTextColor(getResources().getColor(R.color.purple_500));

        ArrayList<Float> totalArray = new ArrayList();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String price = dataSnapshot.child("price").getValue().toString();
                        String quantity = dataSnapshot.child("quantity").getValue().toString();
                        totalArray.add(Float.parseFloat(price) * Integer.parseInt(quantity));

                    }

                    TextView priceView = (TextView) findViewById(R.id.basket_total);

                    float newPrice = 0;
                    for (int i = 0; i < totalArray.size(); i++) {
                        newPrice = newPrice + totalArray.get(i);

                    }

                    priceView.setText("£" + String.format("%.2f", newPrice));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapter.setOnItemClickListener(new BasketAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Basket basket) {

                Intent intent = new Intent(getApplicationContext(), UpdateMealActivity.class);
                intent.putExtra("mealId", basket.getMealID());
                intent.putExtra("mealName", basket.getName());
                intent.putExtra("mealDescription", basket.getDescription());
                intent.putExtra("mealIngredients", basket.getIngredients());
                intent.putExtra("mealImage", basket.getUrl());
                intent.putExtra("email", email);

                startActivity(intent);

            }
        });


        buttonAddPayment = (Button) findViewById(R.id.button_add_payment_new);
        buttonAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                String name = dataSnapshot.child("name").getValue().toString();
//                                String quantity = dataSnapshot.child("quantity").getValue().toString();
//                                String price = dataSnapshot.child("price").getValue().toString();
//                                String mealID = dataSnapshot.child("mealID").getValue().toString();
//                                String description = dataSnapshot.child("description").getValue().toString();
//                                String ingredients = dataSnapshot.child("ingredients").getValue().toString();
//                                String url = dataSnapshot.child("url").getValue().toString();
//
//
//
//                                Basket basket = new Basket();
//                                basket.setMealID(mealID);
//                                basket.setName(name);
//                                basket.setQuantity(quantity);
//                                basket.setPrice(price);
//                                basket.setDescription(description);
//                                basket.setIngredients(ingredients);
//                                basket.setUrl(url);
//
//                                newref.child(emailSub).push().setValue(basket);
//
//                            }
//
//                            customerRef.push().child("customerName").setValue(emailSub);
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.d("TAG", "onCancelled: " + " cannot get data");
//                    }
//                });

                TextView price = (TextView) findViewById(R.id.basket_total);

                String priceString = price.getText().toString();
                String priceSubString = priceString.substring(1, 5);

                Log.d("TAG", "onClick: " + priceSubString);

                Intent intent = new Intent(getApplicationContext(), CheckoutActivityJava.class);



                intent.putExtra("mealPrice", priceSubString);
                intent.putExtra("email", email);
                startActivity(intent);
            }

        });


        Button clearBasket = (Button) findViewById(R.id.clear_basket);
        clearBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.removeValue();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Basket cleared", Toast.LENGTH_SHORT).show();
            }
        });

        Button backToMenu = (Button) findViewById(R.id.BackToMenu);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


}