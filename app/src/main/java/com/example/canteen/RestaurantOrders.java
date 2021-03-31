package com.example.canteen;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class RestaurantOrders extends AppCompatActivity{

    private AppDatabase db;
    private ArrayList<Basket> basketArrayList;
    private BasketAdapter adapter;

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView textview = (TextView) findViewById(R.id.token);
            Bundle bundle = intent.getBundleExtra("msg");
            textview.setText(bundle.getString("msgBody"));
        }
    };

    @Override
    public void sendBroadcast(Intent intent) {
        TextView textview = (TextView) findViewById(R.id.token);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_orders);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new  IntentFilter("ACTION_STRING_ACTIVITY");
            registerReceiver(activityReceiver, intentFilter);
        }


        //Initialise DB
        db = AppDatabase.getAppDatabase(getApplicationContext());

        showBasket();

        basketArrayList = new ArrayList<Basket>();
        adapter = new BasketAdapter(this, basketArrayList);
        ListView listView = (ListView) findViewById(R.id.order_list);
        listView.setAdapter(adapter);


        Button orderReceived = (Button) findViewById(R.id.order_received);


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                String token = task.getResult();

                //String msg = getString(R.string.msg_token_fmt, token);
                Log.d("TAG", token);
                Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();

                TextView textView = (TextView) findViewById(R.id.token);
                textView.setText(token);

                MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
                myFirebaseInstanceIDService.sendRegistrationToServer(token);


            }
        });


    }








    @SuppressLint("StaticFieldLeak")
    private void showBasket() {
        new AsyncTask<Void, Void, List<Basket>>() {

            @Override
            protected List<Basket> doInBackground(Void... voids) {
                return db.basketDAO().getBasket();
            }

            @Override
            protected void onPostExecute(List<Basket> baskets) {
                super.onPostExecute(baskets);
                if (!baskets.isEmpty()) {
                    //Refresh the list view
                    basketArrayList.clear();
                    basketArrayList.addAll(baskets);
                    adapter.notifyDataSetChanged();


                }
            }
        }.execute();
    }



}