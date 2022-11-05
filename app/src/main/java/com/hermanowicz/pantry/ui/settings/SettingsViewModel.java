package com.hermanowicz.pantry.ui.settings;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.interfaces.PreferencesListener;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    @Inject
    SettingsUseCaseImpl useCase;
    private PreferencesListener preferencesListener;

    @Inject
    public SettingsViewModel(SettingsUseCaseImpl settingsUseCase) {
        useCase = settingsUseCase;
    }


    public void setPreferenceListener(PreferencesListener preferencesListener) {
        this.preferencesListener = preferencesListener;
    }

    public void showPreferences() {
        preferencesListener.showAppVersion(useCase.getAppVersion());
        preferencesListener.showActiveUserEmail(useCase.getActiveUserEmail());
    }

    public String getActiveUserEmail() {
        return useCase.getActiveUserEmail();
    }

    public void onClickBackupProductDatabase() {
        useCase.backupProductDbToFile();
    }

    public void onClickRestoreProductDatabase() {
        useCase.restoreProductDbFromFile();
    }

    public void onClickClearProductDatabase() {
        useCase.clearProductDb();
    }

    public void onClickBackupCategoryDatabase() {
        useCase.backupOwnCategoriesDbToFile();
    }

    public void onClickRestoreCategoryDatabase() {
        useCase.restoreOwnCategoriesDbFromFile();
    }

    public void onClickClearCategoryDatabase() {
        useCase.clearOwnCategoriesDb();
    }

    public void onClickBackupStorageLocationDatabase() {
        useCase.backupStorageLocationsDbToFile();
    }

    public void onClickRestoreStorageLocationDatabase() {
        useCase.restoreStorageLocationDbFromFile();
    }

    public void onClickClearStorageLocationDatabase() {
        useCase.clearStorageLocationsDb();
    }

    public void onClickImportLocalDatabaseToCloud() {
        useCase.importLocalDataToCloud();
    }

    public void setProductList(List<Product> productList) {
        useCase.setProductList(productList);
    }

    public void restoreNotificationsIfNeeded() {
        useCase.restoreNotificationsIfNeeded();
    }

    public void setNotificationsToRestoreFlag() {
        useCase.setNotificationsToRestoreFlag();
    }
}