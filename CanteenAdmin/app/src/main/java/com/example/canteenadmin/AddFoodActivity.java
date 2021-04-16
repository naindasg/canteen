package com.example.canteenadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFoodActivity extends AppCompatActivity {

    EditText mealID, name, description, ingredients, price, url;
    Meal meal;
    Button button;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        mealID = (EditText) findViewById(R.id.mealID);
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        ingredients = (EditText) findViewById(R.id.ingredients);
        price = (EditText) findViewById(R.id.price);
        url = (EditText) findViewById(R.id.url);

        button = (Button) findViewById(R.id.saveButton);

        meal = new Meal();

        ref = FirebaseDatabase.getInstance().getReference("/meals");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meal.setMealId(mealID.getText().toString());
                meal.setName(name.getText().toString());
                meal.setDescription(description.getText().toString());
                meal.setIngredients(ingredients.getText().toString());
                meal.setPrice(price.getText().toString());
                meal.setUrl(url.getText().toString());

                String mealId = mealID.getText().toString();
                ref.child("meal" + mealId).setValue(meal);
                Toast.makeText(AddFoodActivity.this, "Meal added successfully", Toast.LENGTH_LONG).show();
            }
        });

        Button goBack = (Button) findViewById(R.id.goBackFromAdd);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });










    }
}