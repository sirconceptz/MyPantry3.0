package com.hermanowicz.pantry.ui.settings;

import android.app.Activity;
import android.content.Context;

import com.hermanowicz.pantry.interfaces.PricingListener;
import com.hermanowicz.pantry.util.PremiumAccess;

public interface SettingsUseCase {
    void setPremiumActivationListenerAndSetupBillingClient(Context context, PricingListener pricingListener);

    PremiumAccess getPremiumAccess();

    void initPremiumPurchase(Activity activity);

    void activatePremiumAccess();

    String getAppVersion();

    String getActiveUserEmail();
}