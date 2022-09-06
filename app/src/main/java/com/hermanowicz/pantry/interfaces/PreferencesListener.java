package com.hermanowicz.pantry.interfaces;

public interface PreferencesListener {
    void showAppVersion(String version);

    void showActiveUserEmail(String email);

    void initPremiumPurchase();

    void showLoginAndRegisterForm();

    void showDialogClearProductDb();

    void showDialogClearOwnCategoryDb();

    void showDialogClearStorageLocationDb();

    void showInfoForPremiumUserOnly();

    void showDialogBackupProductDb();

    void showDialogRestoreProductDb();

    void showDialogBackupOwnCategoriesDb();

    void showDialogRestoreOwnCategoriesDb();

    void showDialogBackupStorageLocationDb();

    void showDialogRestoreStorageLocationDb();

    void showDialogImportLocalDatabaseToCloud();
}