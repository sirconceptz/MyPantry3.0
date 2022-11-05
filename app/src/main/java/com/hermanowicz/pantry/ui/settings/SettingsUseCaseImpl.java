package com.hermanowicz.pantry.ui.settings;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hermanowicz.pantry.BuildConfig;
import com.hermanowicz.pantry.data.db.dao.category.Category;
import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.data.db.dao.storagelocation.StorageLocation;
import com.hermanowicz.pantry.domain.repository.DatabaseBackupRepository;
import com.hermanowicz.pantry.domain.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.domain.repository.ProductRepository;
import com.hermanowicz.pantry.domain.repository.SharedPreferencesRepository;
import com.hermanowicz.pantry.domain.repository.StorageLocationRepository;
import com.hermanowicz.pantry.domain.usecase.SettingsUseCase;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.util.Notifications;

import java.util.List;

import javax.inject.Inject;

public class SettingsUseCaseImpl implements SettingsUseCase {

    private final ProductRepository productRepository;
    private final OwnCategoryRepository ownCategoryRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final DatabaseBackupRepository databaseBackupRepository;
    private final SharedPreferencesRepository sharedPreferencesRepository;
    private final Notifications notifications;

    @Inject
    public SettingsUseCaseImpl(ProductRepository productRepository,
                               OwnCategoryRepository ownCategoryRepository,
                               StorageLocationRepository storageLocationRepository,
                               DatabaseBackupRepository databaseBackupRepository,
                               SharedPreferencesRepository sharedPreferencesRepository,
                               Notifications notifications) {
        this.productRepository = productRepository;
        this.ownCategoryRepository = ownCategoryRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.databaseBackupRepository = databaseBackupRepository;
        this.sharedPreferencesRepository = sharedPreferencesRepository;
        this.notifications = notifications;
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
        if (FirebaseAuth.getInstance().getUid() != null) {
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
        if (FirebaseAuth.getInstance().getUid() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("products").child(FirebaseAuth.getInstance().getUid());
            for (Product product : productList) {
                ref.child(String.valueOf(product.getId())).setValue(product);
            }
        }
    }

    private void importCategoryDb(List<Category> categoryList) {
        if (FirebaseAuth.getInstance().getUid() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("categories").child(FirebaseAuth.getInstance().getUid());
            for (Category category : categoryList) {
                ref.child(String.valueOf(category.getId())).setValue(category);
            }
        }
    }

    private void importStorageLocationDb(List<StorageLocation> storageLocationList) {
        if (FirebaseAuth.getInstance().getUid() != null) {
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

    @Override
    public void restoreNotificationsIfNeeded() {
        if (sharedPreferencesRepository.getIsNotificationsToRestore()) {
            notifications.cancelNotificationsForAllProducts();
            notifications.createNotificationsForAllProducts();
            sharedPreferencesRepository.setIsNotificationsToRestore(false);
        }
    }

    @Override
    public void setNotificationsToRestoreFlag() {
        sharedPreferencesRepository.setIsNotificationsToRestore(true);
    }

    @Override
    public void setProductList(List<Product> productList) {
        notifications.setProductList(productList);
    }
}