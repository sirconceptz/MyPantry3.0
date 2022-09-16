package com.hermanowicz.pantry.ui.settings;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.interfaces.PricingListener;
import com.hermanowicz.pantry.util.PremiumAccess;

import java.util.List;

public interface SettingsUseCase {
    void setPremiumActivationListenerAndSetupBillingClient(Context context, PricingListener pricingListener);

    PremiumAccess getPremiumAccess();

    void initPremiumPurchase(Activity activity);

    void activatePremiumAccess();

    String getAppVersion();

    String getActiveUserEmail();

    void importLocalDataToCloud();

    void backupProductDbToFile();

    void restoreProductDbFromFile();

    void backupOwnCategoriesDbToFile();

    void restoreOwnCategoriesDbFromFile();

    void backupStorageLocationsDbToFile();

    void restoreStorageLocationDbFromFile();

    void clearProductDb();

    void clearOwnCategoriesDb();

    void clearStorageLocationsDb();

    void restoreNotificationsIfNeeded();

    void setNotificationsToRestoreFlag();

    void setProductList(List<Product> productList);
}