package com.example.canteen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdateMealActivity extends AppCompatActivity {

    DatabaseReference ref;

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
        String universityInitials = intent.getStringExtra("universityInitials");
        String email = intent.getStringExtra("email");

        //Getting the views from the activity_meal_detail
        TextView nameView = (TextView) findViewById(R.id.meal_name_m);
        TextView descriptionView = (TextView) findViewById(R.id.meal_desc_m);
        TextView priceView = (TextView) findViewById(R.id.meal_price_m);
        ImageView imageView = (ImageView) findViewById(R.id.meal_image_m);
        TextView ingredientsView = (TextView) findViewById(R.id.meal_ingredients_m);

        //Attaching data to the view
        nameView.setText(mealName);
        descriptionView.setText(mealDescription);
        priceView.setText("£" + mealPrice);
        Picasso.get().load(imageUrl).into(imageView);
        ingredientsView.setText(mealIngredients);

        // Declare quantity buttons
        TextView quantity = (TextView) findViewById(R.id.quantity_m);
        Button increaseQuantity = (Button) findViewById(R.id.increase_quantity_m);
        Button decreaseQuantity = (Button) findViewById(R.id.decrease_quantity_m);
        Button updateQuantity = (Button) findViewById(R.id.update_quantity);
        Button removeItem = (Button) findViewById(R.id.remove_item_m);
        removeItem.setBackgroundColor(Color.rgb(246, 31, 65));

        // Handle Button Increase Click
        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mealQuantity = Integer.parseInt(quantity.getText().toString());
                mealQuantity = mealQuantity + 1;
                quantity.setText(String.valueOf(mealQuantity));

                String totalPrice = String.valueOf(mealQuantity * Float.parseFloat(mealPrice));
                priceView.setText("£" + totalPrice);
            }
        });

        // Handle Button Decrease Click
        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mealQuantity = Integer.parseInt(quantity.getText().toString());
                if (mealQuantity > 1) {
                    mealQuantity = mealQuantity - 1;
                    quantity.setText(String.valueOf(mealQuantity));

                    String totalPrice = String.valueOf(mealQuantity * Float.parseFloat(mealPrice));
                    priceView.setText("£" + totalPrice);
                }
            }
        });

        //Update Quantity
        ref = FirebaseDatabase.getInstance().getReference("/customer");
        updateQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("name", mealName);
                hashMap.put("description", mealDescription);
                hashMap.put("quantity", quantity.getText().toString());

                ref.child(universityInitials).child("basket").child("meal" + mealId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UpdateMealActivity.this, "Basket updated", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                        intent.putExtra("universityInitials", universityInitials);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                });
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(universityInitials).child("basket").child("meal" + mealId).removeValue();
                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                intent.putExtra("universityInitials", universityInitials);
                intent.putExtra("email", email);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Basket updated", Toast.LENGTH_SHORT).show();
            }
        });
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

