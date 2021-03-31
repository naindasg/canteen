package com.example.canteen;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class MealDetailActivity extends AppCompatActivity {

    private AppDatabase db;

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

        //Getting the views from the activity_meal_detail
        TextView name = (TextView) findViewById(R.id.meal_name);
        TextView description = (TextView) findViewById(R.id.meal_desc);
        TextView price = (TextView) findViewById(R.id.meal_price);
        ImageView image = (ImageView) findViewById(R.id.meal_image);
        TextView mealid = (TextView) findViewById(R.id.meal_id);

        //Attaching data to the view
        name.setText(mealName);
        description.setText(mealDescription);
        price.setText("£" + String.valueOf(mealPrice));
        mealid.setText(mealId);
        Picasso.get().load(imageUrl).into(image);

        // Declare buttons
        TextView quantity = (TextView) findViewById(R.id.quantity);
        Button increaseQuantity = (Button) findViewById(R.id.increase_quantity);
        Button decreaseQuantity = (Button) findViewById(R.id.decrease_quantity);
        Button addToBasket = (Button) findViewById(R.id.addToBasket);

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

        name.setTextColor(Color.rgb(98, 0, 238));

        // Initialize DB
        db = AppDatabase.getAppDatabase(this);

        // Handle addToBasket button
        addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = Integer.parseInt(quantity.getText().toString());
                checkBasket(mealId, mealName, Float.parseFloat(mealPrice), newQuantity, mealDescription, imageUrl);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void checkout(final String mealId, final String mealName, final float mealPrice, final int mealQty, final String mealDescription, final String mealImage) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                    Basket basket = new Basket();
                    basket.setMealId(mealId);
                    basket.setMealName(mealName);
                    basket.setMealPrice(mealPrice);
                    basket.setMealQuantity(mealQty);
                    basket.setMealDescription(mealDescription);
                    basket.setMealImage(mealImage);
                    db.basketDAO().insertBasket(basket);
                    return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "MEAL ADDED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    public void deleteBasket() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.basketDAO().deleteAll();
                return null;
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
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "BASKET UPDATED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void checkBasket(final String mealId, final String mealName, final float mealPrice, final int mealQuantity, final String mealDescription, final String mealImage) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                Basket basket = db.basketDAO().getBasket(mealId);
                if (basket == null) {
                    return "BASKET_DOES_NOT_EXIST";
                } else {
                    return basket.getMealId();
                }
            }

            @Override
            protected void onPostExecute(final String result) {
                super.onPostExecute(result);
                if (result.equals("BASKET_DOES_NOT_EXIST")) {
                    checkout(mealId, mealName, mealPrice, mealQuantity, mealDescription, mealImage);
                } else {
                    // Show an alert
                    AlertDialog.Builder builder = new AlertDialog.Builder(MealDetailActivity.this);
                    builder.setTitle("Add More?");
                    builder.setMessage("Your basket already has this meal. Do you want to update?");
                    builder.setPositiveButton("No", null);
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateBasket(Integer.parseInt(result), mealQuantity);
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        }.execute();
    }

}