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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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
        String universityInitials = intent.getStringExtra("universityInitials");
        String email = intent.getStringExtra("email");

        //Getting the views from the activity_meal_detail
        TextView nameView = (TextView) findViewById(R.id.meal_name);
        TextView descriptionView = (TextView) findViewById(R.id.meal_desc);
        TextView priceView = (TextView) findViewById(R.id.meal_price);
        ImageView imageView = (ImageView) findViewById(R.id.meal_image);
        TextView mealIdView = (TextView) findViewById(R.id.meal_id);
        TextView ingredientsView = (TextView) findViewById(R.id.meal_ingredients);

        //Attaching data to the view
        nameView.setText(mealName);
        nameView.setTextColor(Color.rgb(98, 0, 238));
        descriptionView.setText(mealDescription);
        priceView.setText("£" + mealPrice);
        mealIdView.setText(mealId);
        Picasso.get().load(imageUrl).into(imageView);
        ingredientsView.setText(mealIngredients);

        // Declare quantity and buttons
        TextView quantity = (TextView) findViewById(R.id.quantity);
        Button increaseQuantity = (Button) findViewById(R.id.increase_quantity);
        Button decreaseQuantity = (Button) findViewById(R.id.decrease_quantity);
        Button addToBasket = (Button) findViewById(R.id.addToBasket);

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

//        //Calculating stock;
//        //If there is stock, then make button active.
//        //Else, make button inactive
//        DatabaseReference mealRef = FirebaseDatabase.getInstance().getReference("/meals");
//        mealRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                        String dataMealID = dataSnapshot.child("mealID").getValue().toString();
//                        if (dataMealID.equals(mealId)) {
//                            String stock = dataSnapshot.child("stock").getValue().toString();
//                            if (Integer.parseInt(stock) > 0) {
//                                addToBasket.setEnabled(true);
//                            } else {
//                                addToBasket.setEnabled(false);
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        ref = FirebaseDatabase.getInstance().getReference("/customer");
        addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Basket basket = new Basket();
                basket.setMealID(mealId);
                basket.setName(mealName);
                basket.setDescription(mealDescription);
                basket.setIngredients(mealIngredients);
                basket.setPrice(mealPrice);
                basket.setQuantity(quantity.getText().toString());
                basket.setUrl(imageUrl);

                ref.child(universityInitials).child("basket").child("meal" + mealId).setValue(basket);
                Toast.makeText(getApplicationContext(), "MEAL ADDED", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("universityInitials", universityInitials);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);

        MenuItem logout = menu.findItem(R.id.logoutFromActivity);

        Intent intent = getIntent();
        String universityInitials = intent.getStringExtra("universityInitials");
        String email = intent.getStringExtra("email");

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