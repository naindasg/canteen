package com.example.canteenadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerNamesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    CustomerNamesAdapter adapter; // Create Object of the Adapter class
    DatabaseReference ref; // Create object of the Firebase Realtime Database
    DatabaseReference newRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_names);

        ref = FirebaseDatabase.getInstance().getReference("/customerName");

        recyclerView = findViewById(R.id.customer_orders_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseRecyclerOptions<CustomerNames> options = new FirebaseRecyclerOptions.Builder<CustomerNames>().setQuery(ref, CustomerNames.class).build();

        // Connecting object of required Adapter class to the Adapter class itself
        adapter = new CustomerNamesAdapter(options);

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
                String name = adapter.getSnapshots().getSnapshot(viewHolder.getAdapterPosition()).child("customerName").getValue().toString();
                Log.d("TAG", "onSwiped: " + adapter.getSnapshots().getSnapshot(viewHolder.getAdapterPosition()).child("customerName").getValue().toString());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/orders/" + name);
                ref.removeValue();

//                DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("/customer/" + name + "/basket");
//                newRef.removeValue();





            }
        }).attachToRecyclerView(recyclerView);



        adapter.setOnItemClickListener(new CustomerNamesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CustomerNames customerNames) {
                Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
                intent.putExtra("customerName", customerNames.getCustomerName());
                Log.d("TAG", "onItemClick: " + customerNames.getCustomerName());
                startActivity(intent);

            }
        });


        Button viewMenu = (Button) findViewById(R.id.viewMenu);
        viewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
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


