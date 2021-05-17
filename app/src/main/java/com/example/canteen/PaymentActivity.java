package com.example.canteen;

import android.app.Application;

import com.stripe.android.PaymentConfiguration;

public class PaymentActivity extends Application {

    /*
        This sample of code has been taken from:
        https://stripe.com/docs/payments/accept-a-payment?platform=android&lang=java&ui=custom
     */
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51IHfbrGfF6sAEAR9wvJrBW61soe8DfrRc5hKyQsl8SkfimyT0rolH6bj2fLDwD3qm202aIh62SKZilBCE99xTYid00pAUA3uRR"
        );
    }

}