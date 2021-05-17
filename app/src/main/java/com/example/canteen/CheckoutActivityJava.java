package com.example.canteen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
    This class has been taken from:
    https://stripe.com/docs/payments/accept-a-payment?platform=android&lang=java&ui=custom
 */
public class CheckoutActivityJava extends AppCompatActivity {
    // 10.0.2.2 is the Android emulator's alias to localhost
    private static final String BACKEND_URL = "http://10.0.2.2:4242/";
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        stripe = new Stripe(getApplicationContext(),
                Objects.requireNonNull("pk_test_51IHfbrGfF6sAEAR9wvJrBW61soe8DfrRc5hKyQsl8SkfimyT0rolH6bj2fLDwD3qm202aIh62SKZilBCE99xTYid00pAUA3uRR")
        );

        startCheckout();

        Button goBackFromPayment = (Button) findViewById(R.id.goBackFromPayment);
        goBackFromPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String universityInitials = intent.getStringExtra("universityInitials");
                String email = intent.getStringExtra("email");

                Intent newIntent = new Intent(getApplicationContext(), BasketActivity.class);
                newIntent.putExtra("universityInitials", universityInitials);
                newIntent.putExtra("email", email);
                startActivity(newIntent);
            }
        });

    }

    /*
        startCheckout() has been taken from:
        https://stripe.com/docs/payments/accept-a-payment?platform=android&lang=java&ui=custom
     */
    private void startCheckout() {
        //Get totalPrice
        Intent intent = getIntent();
        String price = intent.getStringExtra("mealPrice");

        //Convert price from string to double
        double amount = Double.parseDouble(price);

        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8"); //gets a JSON file

        //Declaring payMap and itemMap
        Map<String, Object> payMap = new HashMap<>();
        Map<String, Object> itemMap = new HashMap<>();

        List<Map<String, Object>> itemList = new ArrayList<>();
        payMap.put("currency", "gbp"); //place currency and gbp
        itemMap.put("id", "photo_subscription"); //put id and photo subscription
        itemMap.put("amount", amount * 100); // put amount and convert it into pounds

        //itemList will have ("id", "photo_subscription") and ("amount", amount * 100)
        itemList.add(itemMap);

        //items =  ("id", "photo_subscription") and ("amount", amount * 100)
        payMap.put("items", itemList);

        //Convert payMap object into Json String
        String json = new Gson().toJson(payMap);

        //RequestBody.create(content, contentType);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder().url(BACKEND_URL + "create-payment-intent").post(body).build();

        httpClient.newCall(request).enqueue(new PayCallback(this));

        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget); //Find the cardView
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);

                //Getting intent from BasketActivity, namely the universityInitials and email
                Intent intent1 = getIntent();
                String universityInitials = intent1.getStringExtra("universityInitials");
                String email = intent1.getStringExtra("email");

                //add the order to database
                addOrderToDatabase();

                //Create new intent and start activity: AwaitingOrderActivity
                Intent intent2 = new Intent(getApplicationContext(), AwaitingOrderActivity.class);
                intent2.putExtra("universityInitials", universityInitials);
                startActivity(intent2);
                CheckoutActivityJava.this.finish();
            }
        });

    }

    //Let the customer know that the payment has succeeded
    private void displayAlert(@NonNull String title, @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(title).setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    // Handle the result of stripe.confirmPayment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    //Shows the response of paymentSuccess message
    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
    }

    /*
        PayCallBack has been taken from:
        https://stripe.com/docs/payments/accept-a-payment?platform=android&lang=java&ui=custom
     */
    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<CheckoutActivityJava> activityRef;

        PayCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }

        //Handle failure
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }

        //Sends a response message
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    /*
        PaymentResultCallBack has been taken from:
        https://stripe.com/docs/payments/accept-a-payment?platform=android&lang=java&ui=custom

     */
    private static final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<CheckoutActivityJava> activityRef;

        PaymentResultCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert("Payment completed", gson.toJson(paymentIntent)
                );

            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert("Payment failed", Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }

        //Payment failed
        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }

    }

    /*
        This random() method has been taken from:
        https://www.baeldung.com/java-random-string
     */
    //Creates a unique ID;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String random() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    private void addOrderToDatabase() {
        //Getting intent from BasketActivity
        Intent intent = getIntent();
        String universityInitials = intent.getStringExtra("universityInitials");
        String email = intent.getStringExtra("email");

        //Creating references
        DatabaseReference basketRef = FirebaseDatabase.getInstance().getReference("/customer/" + universityInitials + "/basket");
        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference("/customerName");
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("/orders");
        DatabaseReference orderHistory = FirebaseDatabase.getInstance().getReference("/orderHistory");
        DatabaseReference orderCustomerRef = FirebaseDatabase.getInstance().getReference("/orderHistoryName");

        //Loop through basket and store data into database
        basketRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Assign variables
                    String randomGlobal = null;
                    String random = random();

                    //Loop through the baskets, and store them in the orderRef part of the database
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String quantity = dataSnapshot.child("quantity").getValue().toString();
                        String price = dataSnapshot.child("price").getValue().toString();
                        String mealID = dataSnapshot.child("mealID").getValue().toString();
                        String description = dataSnapshot.child("description").getValue().toString();
                        String ingredients = dataSnapshot.child("ingredients").getValue().toString();
                        String url = dataSnapshot.child("url").getValue().toString();

                        //Create basket object
                        Basket basket = new Basket();
                        basket.setMealID(mealID);
                        basket.setName(name);
                        basket.setQuantity(quantity);
                        basket.setPrice(price);
                        basket.setDescription(description);
                        basket.setIngredients(ingredients);
                        basket.setUrl(url);

                        //Re-assign random to global variable
                        randomGlobal = random;

                        //Push the order into orderRef, and also push the order into orderHistory.
                        orderRef.child(universityInitials + "_" + random).push().setValue(basket);
                        orderHistory.child(universityInitials + "_" + random).push().setValue(basket);

                    }

                    //Create a customer object and push into database to correspond with the order
                    Customer customer = new Customer();
                    customer.setCustomerEmail(email);
                    customer.setCustomerName(universityInitials + "_" + randomGlobal);
                    customerRef.push().setValue(customer);

                    orderCustomerRef.push().setValue(customer);


                    //Empty the basket of the user
                    basketRef.removeValue();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: " + " cannot get data");
            }
        });

    }
}