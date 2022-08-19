package com.hermanowicz.pantry.ui.settings;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.hermanowicz.pantry.BuildConfig;
import com.hermanowicz.pantry.interfaces.PricingListener;
import com.hermanowicz.pantry.repository.PricingRepository;
import com.hermanowicz.pantry.repository.PricingRepositoryImpl;
import com.hermanowicz.pantry.util.PremiumAccess;

import javax.inject.Inject;

public class SettingsUseCaseImpl implements SettingsUseCase {

    private final PricingRepository pricingRepository = new PricingRepositoryImpl();
    private final PremiumAccess premiumAccess;

    @Inject
    public SettingsUseCaseImpl(PremiumAccess premiumAccess) {
        this.premiumAccess = premiumAccess;
    }

    @Override
    public void setPremiumActivationListenerAndSetupBillingClient(Context context, PricingListener pricingListener) {
        pricingRepository.setPremiumActivationListenerAndBuildBillingClient(context, pricingListener);
    }

    @Override
    public void initPremiumPurchase(Activity activity) {
        pricingRepository.initPremiumPurchase(activity);
    }

    @Override
    public void activatePremiumAccess() {
        premiumAccess.enablePremiumAccess();
    }

    @Override
    public PremiumAccess getPremiumAccess() {
        return premiumAccess;
    }

    @Override
    public String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getActiveUserEmail() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            return FirebaseAuth.getInstance().getCurrentUser().getEmail();
        else
            return "";
    }
}