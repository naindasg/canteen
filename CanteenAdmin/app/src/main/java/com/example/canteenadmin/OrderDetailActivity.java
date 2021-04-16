package com.example.canteenadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    OrderDetailAdapter adapter; // Create Object of the Adapter class
    DatabaseReference ref; // Create object of the Firebase Realtime Database
    Button sendMessage;
    DatabaseReference customerRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String customerName = intent.getStringExtra("customerName");



        ref = FirebaseDatabase.getInstance().getReference("/orders/" + customerName);

        Log.d("TAG", "onCreate: " + ref);


        recyclerView = findViewById(R.id.order_detail_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<OrderDetail> options = new FirebaseRecyclerOptions.Builder<OrderDetail>().setQuery(ref, OrderDetail.class).build();

        adapter = new OrderDetailAdapter(options);

        recyclerView.setAdapter(adapter);

        Button sendMessage = (Button) findViewById(R.id.sendMessageOrderDetail);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    }