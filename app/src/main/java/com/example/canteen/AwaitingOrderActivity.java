package com.example.canteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

//        //Handle logoutwwwjjj
//        Button logout = (Button) findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fAuth.signOut();
//
//                //Clear basket upon logout
//                DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference("/customer");
//                customerRef.child(universityInitials).child("basket").removeValue();
//
//                Intent intent = new Intent(AwaitingOrderActivity.this, LoginActivity.class);
//                startActivity(intent);
//                System.exit(0);
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        //Nothing happens
    }

}
