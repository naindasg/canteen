package com.example.canteen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AwaitingOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awaiting_order);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        Log.d("AwaitingOrder", "onCreate: " + email);


        Button backToMenu = (Button) findViewById(R.id.backToMenu);

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
    public void onBackPressed() {

    }
}