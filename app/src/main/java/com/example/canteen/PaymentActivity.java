package com.example.canteen;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

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
                BuildConfig.PUBLISHABLE_KEY
        );

    }





}