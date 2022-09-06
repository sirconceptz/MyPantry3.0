package com.hermanowicz.pantry.ui.settings;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hermanowicz.pantry.BuildConfig;
import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.interfaces.PricingListener;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.repository.DatabaseBackupRepository;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.repository.PricingRepository;
import com.hermanowicz.pantry.repository.PricingRepositoryImpl;
import com.hermanowicz.pantry.repository.ProductRepository;
import com.hermanowicz.pantry.repository.SharedPreferencesRepository;
import com.hermanowicz.pantry.repository.StorageLocationRepository;
import com.hermanowicz.pantry.util.PremiumAccess;

import java.util.List;

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
        List<Product> productList = productRepository.getAllLocalProductsAsList();
        List<Category> categoryList = ownCategoryRepository.getAllLocalCategoriesAsList();
        List<StorageLocation> storageLocations = storageLocationRepository.getAllLocalStorageLocationsAsList();
        cleanOnlineDb();
        importDbOfflineToOnline(productList, categoryList, storageLocations);
    }

    private void cleanOnlineDb() {
        if(FirebaseAuth.getInstance().getUid() != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("products").child(FirebaseAuth.getInstance().getUid()).removeValue();
            FirebaseDatabase.getInstance().getReference()
                    .child("categories").child(FirebaseAuth.getInstance().getUid()).removeValue();
            FirebaseDatabase.getInstance().getReference()
                    .child("storage_locations").child(FirebaseAuth.getInstance().getUid()).removeValue();
        }
    }

    private void importDbOfflineToOnline(List<Product> productList, List<Category> categoryList, List<StorageLocation> storageLocationList) {
        importProductDb(productList);
        importCategoryDb(categoryList);
        importStorageLocationDb(storageLocationList);
    }

    private void importProductDb(List<Product> productList) {
        if(FirebaseAuth.getInstance().getUid() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("products").child(FirebaseAuth.getInstance().getUid());
            for(Product product : productList){
                ref.child(String.valueOf(product.getId())).setValue(product);
            }
        }
    }

    private void importCategoryDb(List<Category> categoryList) {
        if(FirebaseAuth.getInstance().getUid() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("categories").child(FirebaseAuth.getInstance().getUid());
            for (Category category : categoryList) {
                ref.child(String.valueOf(category.getId())).setValue(category);
            }
        }
    }

    private void importStorageLocationDb(List<StorageLocation> storageLocationList) {
        if(FirebaseAuth.getInstance().getUid() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("storage_locations").child(FirebaseAuth.getInstance().getUid());
            for (StorageLocation storageLocation : storageLocationList) {
                ref.child(String.valueOf(storageLocation.getId())).setValue(storageLocation);
            }
        }
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
        DatabaseMode currentDatabaseMode = sharedPreferencesRepository.getDatabaseModeFromSettings();
        productRepository.deleteAll(currentDatabaseMode);
    }

    @Override
    public void clearOwnCategoriesDb() {
        DatabaseMode currentDatabaseMode = sharedPreferencesRepository.getDatabaseModeFromSettings();
        ownCategoryRepository.deleteAll(currentDatabaseMode);
    }

    @Override
    public void clearStorageLocationsDb() {
        DatabaseMode currentDatabaseMode = sharedPreferencesRepository.getDatabaseModeFromSettings();
        storageLocationRepository.deleteAll(currentDatabaseMode);
    }
}