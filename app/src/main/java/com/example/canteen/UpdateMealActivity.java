package com.example.canteen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdateMealActivity extends AppCompatActivity {

    DatabaseReference ref;
    DatabaseReference newref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meal);


        //Getting intent from BasketActivity
        Intent intent = getIntent();
        String mealName = intent.getStringExtra("mealName");
        String mealDescription = intent.getStringExtra("mealDescription");
        String mealPrice = intent.getStringExtra("mealPrice");
        String mealId = intent.getStringExtra("mealId");
        String mealIngredients = intent.getStringExtra("mealIngredients");
        String imageUrl = intent.getStringExtra("mealImage");
        String email = intent.getStringExtra("email");

        //Getting the views from the activity_meal_detail
        TextView name = (TextView) findViewById(R.id.meal_name_m);
        TextView description = (TextView) findViewById(R.id.meal_desc_m);
        TextView price = (TextView) findViewById(R.id.meal_price_m);
        ImageView image = (ImageView) findViewById(R.id.meal_image_m);
        TextView ingredients = (TextView) findViewById(R.id.meal_ingredients_m);

        //Attaching data to the view
        name.setText(mealName);
        description.setText(mealDescription);
        price.setText(mealPrice);
        Picasso.get().load(imageUrl).into(image);
        ingredients.setText(mealIngredients);

        // Declare buttons
        final TextView quantity = (TextView) findViewById(R.id.quantity_m); //The quantity of the meal, i.e. 2 pizzas
        Button increaseQuantity = (Button) findViewById(R.id.increase_quantity_m);
        Button decreaseQuantity = (Button) findViewById(R.id.decrease_quantity_m);
        Button updateQuantity = (Button) findViewById(R.id.update_quantity);
        Button removeItem = (Button) findViewById(R.id.remove_item_m);

        removeItem.setBackgroundColor(Color.rgb(246, 31, 65));

        // Handle Button Increase Click
        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = Integer.parseInt(quantity.getText().toString());
                newQuantity = newQuantity + 1;
                quantity.setText(String.valueOf(newQuantity));

//                String priceString = String.valueOf(newQuantity * Float.parseFloat(mealPrice));
//
//                price.setText(priceString);
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

//                    String priceString = String.valueOf(newQuantity * Float.parseFloat(mealPrice));
//                    price.setText(priceString);
                }
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("/customer");


        updateQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap hashMap = new HashMap();
                hashMap.put("name", mealName);
                hashMap.put("description", mealDescription);
                hashMap.put("quantity", quantity.getText().toString());

                ref.child(email.substring(0, 4)).child("basket").child("meal" + mealId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UpdateMealActivity.this, "Basket updated", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                });

            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref.child(email.substring(0, 4)).child("basket").child("meal" + mealId).removeValue();
                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Basket updated", Toast.LENGTH_SHORT).show();

            }
        });

    }
}

