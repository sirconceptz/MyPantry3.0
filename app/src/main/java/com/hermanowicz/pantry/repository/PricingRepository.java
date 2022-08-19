package com.hermanowicz.pantry.repository;

import android.app.Activity;
import android.content.Context;

import com.hermanowicz.pantry.interfaces.PricingListener;

public interface PricingRepository {
    void setPremiumActivationListenerAndBuildBillingClient(Context context, PricingListener pricingListener);

    void setupBillingClient();

    void initPremiumPurchase(Activity activity);
}