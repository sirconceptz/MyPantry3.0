package com.hermanowicz.pantry.util;

import android.content.Context;

import com.android.billingclient.api.BillingClient;

public class BillingClientProvider {

    private final Context context;

    public BillingClientProvider(Context context){
        this.context = context;

        BillingClient billingClient = BillingClient
                .newBuilder(context)
                .enablePendingPurchases()
                .build();
    }
}