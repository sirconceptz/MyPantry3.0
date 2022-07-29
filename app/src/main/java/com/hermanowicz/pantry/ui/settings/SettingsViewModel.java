package com.hermanowicz.pantry.ui.settings;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.interfaces.PricingListener;
import com.hermanowicz.pantry.interfaces.ShowPreferencesListener;
import com.hermanowicz.pantry.util.PremiumAccess;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel implements PricingListener {

    @Inject
    SettingsUseCaseImpl useCase;
    private PricingListener pricingListener;
    private final PremiumAccess isPremiumAccess;

    @Inject
    public SettingsViewModel(SettingsUseCaseImpl settingsUseCase){
        useCase = settingsUseCase;
        isPremiumAccess = useCase.getPremiumAccess();
    }

    private void activatePremiumFeaturesIfPremiumUser() {
        if(isPremiumAccess.isPremium())
            activatePremiumFeatures();
    }

    public void showPreferences(ShowPreferencesListener showPreferencesListener){
        showPreferencesListener.showAppVersion(useCase.getAppVersion());
        showPreferencesListener.showActiveUserEmail(useCase.getActiveUserEmail());
    }

    public void setPremiumActivationListenerAndSetupBillingClient(Context context, PricingListener pricingListener){
        this.pricingListener = pricingListener;
        useCase.setPremiumActivationListenerAndSetupBillingClient(context, this);
        activatePremiumFeaturesIfPremiumUser();
    }

    public void initPremiumPurchase(Activity activity){
        useCase.initPremiumPurchase(activity);
    }

    @Override
    public void isBillingClientReady() {
        pricingListener.isBillingClientReady();
    }

    @Override
    public void activatePremiumFeatures() {
        useCase.activatePremiumAccess();
        pricingListener.activatePremiumFeatures();
    }

    public String getActiveUserEmail() {
        return useCase.getActiveUserEmail();
    }
}