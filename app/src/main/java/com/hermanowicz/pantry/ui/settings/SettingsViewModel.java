package com.hermanowicz.pantry.ui.settings;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.interfaces.PricingListener;
import com.hermanowicz.pantry.interfaces.PreferencesListener;
import com.hermanowicz.pantry.util.PremiumAccess;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel implements PricingListener {

    @Inject
    SettingsUseCaseImpl useCase;
    private PricingListener pricingListener;
    private final PremiumAccess isPremiumAccess;
    private PreferencesListener preferencesListener;

    @Inject
    public SettingsViewModel(SettingsUseCaseImpl settingsUseCase) {
        useCase = settingsUseCase;
        isPremiumAccess = useCase.getPremiumAccess();
    }

    private void activatePremiumFeaturesIfPremiumUser() {
        if (isPremiumAccess.isPremium())
            activatePremiumFeatures();
    }

    public void setPreferenceListener(PreferencesListener preferencesListener){
        this.preferencesListener = preferencesListener;
    }

    public void showPreferences() {
        preferencesListener.showAppVersion(useCase.getAppVersion());
        preferencesListener.showActiveUserEmail(useCase.getActiveUserEmail());
    }

    public void setPremiumActivationListenerAndSetupBillingClient(Context context, PricingListener pricingListener) {
        this.pricingListener = pricingListener;
        useCase.setPremiumActivationListenerAndSetupBillingClient(context, this);
        activatePremiumFeaturesIfPremiumUser();
    }

    public void initPremiumPurchase(Activity activity) {
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

    public void onClickBackupProductDatabase() {
        if(isPremiumAccess.isPremium())
            useCase.backupProductDbToFile();
        else
            preferencesListener.showInfoForPremiumUserOnly();
    }

    public void onClickRestoreProductDatabase() {
        if(isPremiumAccess.isPremium())
            useCase.restoreProductDbFromFile();
        else
            preferencesListener.showInfoForPremiumUserOnly();
    }

    public void onClickClearProductDatabase() {
        useCase.clearProductDb();
    }

    public void onClickBackupCategoryDatabase() {
        if(isPremiumAccess.isPremium())
            useCase.backupOwnCategoriesDbToFile();
        else
            preferencesListener.showInfoForPremiumUserOnly();
    }

    public void onClickRestoreCategoryDatabase() {
        if(isPremiumAccess.isPremium())
            useCase.restoreOwnCategoriesDbFromFile();
        else
            preferencesListener.showInfoForPremiumUserOnly();
    }

    public void onClickClearCategoryDatabase() {
        useCase.clearOwnCategoriesDb();
    }

    public void onClickBackupStorageLocationDatabase() {
       if(isPremiumAccess.isPremium())
            useCase.backupStorageLocationsDbToFile();
        else
            preferencesListener.showInfoForPremiumUserOnly();
    }

    public void onClickRestoreStorageLocationDatabase() {
        if(isPremiumAccess.isPremium())
            useCase.restoreStorageLocationDbFromFile();
        else
            preferencesListener.showInfoForPremiumUserOnly();
    }

    public void onClickClearStorageLocationDatabase() {
        useCase.clearStorageLocationsDb();
    }

    public void onClickImportLocalDatabaseToCloud() {
        useCase.importLocalDataToCloud();
    }
}