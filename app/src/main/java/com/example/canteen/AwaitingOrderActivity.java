package com.example.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AwaitingOrderActivity extends AppCompatActivity {

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awaiting_order);

        fAuth = FirebaseAuth.getInstance();

        //Getting universityInitials
        Intent intent = getIntent();
        String universityInitials = intent.getStringExtra("universityInitials");

        //Handle logout
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();

                //Clear basket upon logout
                DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference("/customer");
                customerRef.child(universityInitials).child("basket").removeValue();

                Intent intent = new Intent(AwaitingOrderActivity.this, LoginActivity.class);
                startActivity(intent);
                System.exit(0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //Nothing happens
    }

}