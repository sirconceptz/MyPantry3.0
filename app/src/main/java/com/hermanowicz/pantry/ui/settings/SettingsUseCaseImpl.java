package com.hermanowicz.pantry.ui.settings;

import android.app.Activity;
import android.content.Context;


import androidx.multidex.BuildConfig;

import com.google.firebase.auth.FirebaseAuth;
import com.hermanowicz.pantry.interfaces.PricingListener;
import com.hermanowicz.pantry.model.Database;
import com.hermanowicz.pantry.repository.DatabaseBackupRepository;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.repository.PricingRepository;
import com.hermanowicz.pantry.repository.PricingRepositoryImpl;
import com.hermanowicz.pantry.repository.ProductRepository;
import com.hermanowicz.pantry.repository.SharedPreferencesRepository;
import com.hermanowicz.pantry.repository.StorageLocationRepository;
import com.hermanowicz.pantry.util.PremiumAccess;

import javax.inject.Inject;

public class SettingsUseCaseImpl implements SettingsUseCase {

    private final PricingRepository pricingRepository = new PricingRepositoryImpl();
    private final PremiumAccess premiumAccess;
    private final ProductRepository productRepository;
    private final OwnCategoryRepository ownCategoryRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final DatabaseBackupRepository databaseBackupRepository;
    private final SharedPreferencesRepository sharedPreferencesRepository;

    @Inject
    public SettingsUseCaseImpl(PremiumAccess premiumAccess, ProductRepository productRepository, OwnCategoryRepository ownCategoryRepository, StorageLocationRepository storageLocationRepository, DatabaseBackupRepository databaseBackupRepository, SharedPreferencesRepository sharedPreferencesRepository) {
        this.premiumAccess = premiumAccess;
        this.productRepository = productRepository;
        this.ownCategoryRepository = ownCategoryRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.databaseBackupRepository = databaseBackupRepository;
        this.sharedPreferencesRepository = sharedPreferencesRepository;
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

    @Override
    public void importLocalDataToCloud() {

    }

    @Override
    public void backupProductDbToFile() {
        databaseBackupRepository.backupProductDb();
    }

    @Override
    public void restoreProductDbFromFile() {
        databaseBackupRepository.restoreProductDb();
    }

    @Override
    public void backupOwnCategoriesDbToFile() {
        databaseBackupRepository.backupCategoryDb();
    }

    @Override
    public void restoreOwnCategoriesDbFromFile() {
        databaseBackupRepository.restoreCategoryDb();
    }

    @Override
    public void backupStorageLocationsDbToFile() {
        databaseBackupRepository.backupStorageLocationDb();
    }

    @Override
    public void restoreStorageLocationDbFromFile() {
        databaseBackupRepository.restoreStorageLocationDb();
    }

    @Override
    public void clearProductDb() {
        Database currentDatabaseMode = sharedPreferencesRepository.getDatabaseModeFromSettings();
        productRepository.deleteAll(currentDatabaseMode);
    }

    @Override
    public void clearOwnCategoriesDb() {
        Database currentDatabaseMode = sharedPreferencesRepository.getDatabaseModeFromSettings();
        ownCategoryRepository.deleteAll(currentDatabaseMode);
    }

    @Override
    public void clearStorageLocationsDb() {
        Database currentDatabaseMode = sharedPreferencesRepository.getDatabaseModeFromSettings();
        storageLocationRepository.deleteAll(currentDatabaseMode);
    }
}