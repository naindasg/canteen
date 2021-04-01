package com.example.canteen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class UpdateMealActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meal);

        Intent intent = getIntent();
        String mealName = intent.getStringExtra("mealName");
        String mealDescription = intent.getStringExtra("mealDescription");
        String mealPrice = intent.getStringExtra("mealPrice");
        String mealId = intent.getStringExtra("mealId");
        String imageUrl = intent.getStringExtra("mealImage");

        //Getting the views from the activity_meal_detail
        TextView name = (TextView) findViewById(R.id.meal_name_m);
        TextView description = (TextView) findViewById(R.id.meal_desc_m);
        TextView price = (TextView) findViewById(R.id.meal_price_m);
        ImageView image = (ImageView) findViewById(R.id.meal_image_m);

        //Attaching data to the view
        name.setText(mealName);
        description.setText(mealDescription);
        price.setText(mealPrice);
        Picasso.get().load(imageUrl).into(image);

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
                // String priceString = String.valueOf(qty * Float.parseFloat(mealPrice));
                // price.setText(priceString);
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
                    //   String priceString = String.valueOf(qty * Float.parseFloat(mealPrice));
                    //  price.setText(priceString);
                }
            }
        });


        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBasket(Integer.parseInt(mealId));
            }
        });

        // Initialize DB
        db = AppDatabase.getAppDatabase(this);

        // Handle Button Add To Tray Click
        updateQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = Integer.parseInt(quantity.getText().toString());
                updateBasket(Integer.parseInt(mealId), newQuantity);
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    public void deleteBasket(final int basketId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.basketDAO().deleteBasket(basketId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Basket updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("screen", "tray");
                startActivity(intent);
                finish();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateBasket(final int basketId, final int mealQty) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.basketDAO().deleteBasketQuantity(basketId);
                db.basketDAO().updateBasket(basketId, mealQty);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("screen", "tray");
                startActivity(intent);
                finish();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "BASKET UPDATED", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }
}

