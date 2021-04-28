package com.example.canteen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MealDetailActivity extends AppCompatActivity {

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        //Getting intents from the MenuActivity
        Intent intent = getIntent();
        String mealId = intent.getStringExtra("mealId");
        String mealName = intent.getStringExtra("mealName");
        String mealDescription = intent.getStringExtra("mealDescription");
        String mealPrice = intent.getStringExtra("mealPrice");
        String imageUrl = intent.getStringExtra("mealImage");
        String mealIngredients = intent.getStringExtra("mealIngredients");
        String email = intent.getStringExtra("email");

        Log.d("TAG", "onCreate: " + email);

        //Getting the views from the activity_meal_detail
        TextView name = (TextView) findViewById(R.id.meal_name);
        TextView description = (TextView) findViewById(R.id.meal_desc);
        TextView price = (TextView) findViewById(R.id.meal_price);
        ImageView image = (ImageView) findViewById(R.id.meal_image);
        TextView mealid = (TextView) findViewById(R.id.meal_id);
        TextView ingredients = (TextView) findViewById(R.id.meal_ingredients);

        //Attaching data to the view
        name.setText(mealName);
        name.setTextColor(Color.rgb(98, 0, 238));
        description.setText(mealDescription);
        price.setText("£" + String.valueOf(mealPrice));
        mealid.setText(mealId);
        Picasso.get().load(imageUrl).into(image);
        ingredients.setText(mealIngredients);

        // Declare buttons
        TextView quantity = (TextView) findViewById(R.id.quantity);
        Button increaseQuantity = (Button) findViewById(R.id.increase_quantity);
        Button decreaseQuantity = (Button) findViewById(R.id.decrease_quantity);
        Button addToBasket = (Button) findViewById(R.id.addToBasket);
        Button clearBasket = (Button) findViewById(R.id.clear_basket);

        // Handle Button Increase Click
        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = Integer.parseInt(quantity.getText().toString());
                newQuantity = newQuantity + 1;
                quantity.setText(String.valueOf(newQuantity));

                String priceString = String.valueOf(newQuantity * Float.parseFloat(mealPrice));
                price.setText("£" + priceString);

            }
        });

        // Handle Button Decrease Click
        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = Integer.parseInt(quantity.getText().toString());
                if (newQuantity > 1) {
                    newQuantity = newQuantity - 1;
                    quantity.setText(String.valueOf(newQuantity));

                    String priceString = String.valueOf(newQuantity * Float.parseFloat(mealPrice));
                    price.setText("£" + priceString);
                }
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("/customer");

        addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                                String name = dataSnapshot.child("email").getValue().toString();
//
//                                if (name.equals(email)) {
//                                    Log.d("TAG", "onDataChange: " + name);
//
//                                    Basket basket = new Basket();
//                                    basket.setMealID(mealId);
//                                    basket.setName(mealName);
//                                    basket.setDescription(mealDescription);
//                                    basket.setIngredients(mealIngredients);
//                                    basket.setPrice(mealPrice);
//                                    basket.setQuantity(quantity.getText().toString());
//                                    basket.setUrl(imageUrl);
//
//                                    ref.child(dataSnapshot.getKey()).child("basket").child("meal" + mealId).setValue(basket);
//
//                                }
//
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                Basket basket = new Basket();
                basket.setMealID(mealId);
                basket.setName(mealName);
                basket.setDescription(mealDescription);
                basket.setIngredients(mealIngredients);
                basket.setPrice(mealPrice);
                basket.setQuantity(quantity.getText().toString());
                basket.setUrl(imageUrl);




                ref.child(email.substring(0, 4)).child("basket").child("meal" + mealId).setValue(basket);
                Toast.makeText(getApplicationContext(), "MEAL ADDED", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });











//        ref = FirebaseDatabase.getInstance().getReference("/customer");
//
//        addToBasket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Basket basket = new Basket();
//                basket.setMealID(mealId);
//                basket.setName(mealName);
//                basket.setDescription(mealDescription);
//                basket.setIngredients(mealIngredients);
//                basket.setPrice(mealPrice);
//                basket.setQuantity(quantity.getText().toString());
//                basket.setUrl(imageUrl);
//
//                Log.d("TAG", "onClick:  " + email);
//
//
//                ref.child(email.substring(0, 4)).child("basket").child("meal" + mealId).setValue(basket);
//                Toast.makeText(getApplicationContext(), "MEAL ADDED", Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
//                intent.putExtra("email", email);
//                startActivity(intent);
//            }
//        });

    }
}