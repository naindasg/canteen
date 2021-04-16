package com.example.canteenadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdateActivity extends AppCompatActivity {
    Meal meal;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_menu);

        Intent intent = getIntent();
        String mealId = intent.getStringExtra("mealId");
        String mealName = intent.getStringExtra("mealName");
        String mealDescription = intent.getStringExtra("mealDescription");
        String mealPrice = intent.getStringExtra("mealPrice");
        String imageUrl = intent.getStringExtra("mealImage");
        String mealIngredients = intent.getStringExtra("mealIngredients");

        Log.d("TAG", "onCreate: " + imageUrl);


        EditText name = (EditText) findViewById(R.id.nameu);
        EditText description = (EditText) findViewById(R.id.descriptionu);
        EditText price = (EditText) findViewById(R.id.priceu);
        EditText url = (EditText) findViewById(R.id.urlu);
        EditText id = (EditText) findViewById(R.id.mealIDu);
        EditText ingredients = (EditText) findViewById(R.id.ingredientsu);

        name.setText(mealName);
        description.setText(mealDescription);
        price.setText(mealPrice);
        id.setText(mealId);
        ingredients.setText(mealIngredients);
        url.setText(imageUrl);

        Button saveButton = (Button) findViewById(R.id.saveButtonu);




        String mealID = mealId; //meal1

        ref = FirebaseDatabase.getInstance().getReference("/meals");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.nameu);
                EditText description = (EditText) findViewById(R.id.descriptionu);
                EditText price = (EditText) findViewById(R.id.priceu);
                EditText url = (EditText) findViewById(R.id.urlu);
                EditText id = (EditText) findViewById(R.id.mealIDu);
                EditText ingredients = (EditText) findViewById(R.id.ingredientsu);

                String nameString = name.getText().toString();
                String descriptionString = description.getText().toString();
                String priceString = price.getText().toString();
                String ingredientsString = ingredients.getText().toString();
                String urlString = url.getText().toString();

                HashMap hashMap = new HashMap();
                hashMap.put("name", nameString);
                hashMap.put("description", descriptionString);
                hashMap.put("price", priceString);
                hashMap.put("ingredients", ingredientsString);
                hashMap.put("url", urlString);


                ref.child("meal" + mealID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UpdateActivity.this, "Meal updated successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });

        Button deletebutton = (Button) findViewById(R.id.deleteButton);

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("meal" + mealID).removeValue();
                Toast.makeText(UpdateActivity.this, "Meal deleted successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);

            }
        });


    }
}