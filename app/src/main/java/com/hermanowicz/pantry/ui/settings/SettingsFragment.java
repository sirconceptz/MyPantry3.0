/*
 * Copyright (c) 2019-2022
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.ui.settings;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.interfaces.PreferencesListener;
import com.hermanowicz.pantry.interfaces.PricingListener;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.dialogs.SettingsDialogs;
import com.hermanowicz.pantry.ui.product.ProductViewModel;

import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat implements PricingListener, PreferencesListener, AvailableDataListener {

    private SettingsViewModel settingsViewModel;
    private DatabaseModeViewModel databaseModeViewModel;
    private ProductViewModel productViewModel;

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private SharedPreferences sharedPreferences;

    private Preference goPremium;
    private Preference databaseMode;
    private Preference importDb;
    private Preference activeUser;
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );
    private Preference appVersion;
    private Preference backupProductDb;
    private Preference restoreProductDb;
    private Preference clearProductDb;
    private Preference backupCategoryDb;
    private Preference restoreCategoryDb;
    private Preference clearCategoryDb;
    private Preference backupStorageLocationDb;
    private Preference restoreStorageLocationDb;
    private Preference clearStorageLocationDb;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    @Override
    public void onResume() {
        initView();
        setListeners();
        if (productViewModel.isAvailableData())
            observeAvailableData();

        super.onResume();
    }

    private void initView() {
        productViewModel.setAvailableDataListener(this);
        productViewModel.setDefaultDatabaseMode(databaseModeViewModel.getDatabaseModeFromSettings());

        setPreferencesFromResource(R.xml.preferences, null);
        goPremium = findPreference(getString(R.string.preferences_key_go_premium));
        databaseMode = findPreference(getString(R.string.preferences_key_database_mode));
        importDb = findPreference(getString(R.string.preferences_key_import_db));
        activeUser = findPreference(getString(R.string.preferences_key_active_user));
        appVersion = findPreference(getString(R.string.preferences_key_version));
        restoreProductDb = findPreference(getString(R.string.preferences_key_restore_product_db));
        backupProductDb = findPreference(getString(R.string.preferences_key_backup_product_db));
        clearProductDb = findPreference(getString(R.string.preferences_key_clear_product_db));
        restoreCategoryDb = findPreference(getString(R.string.preferences_key_restore_category_db));
        backupCategoryDb = findPreference(getString(R.string.preferences_key_backup_category_db));
        clearCategoryDb = findPreference(getString(R.string.preferences_key_clear_category_db));
        restoreStorageLocationDb = findPreference(getString(R.string.preferences_key_restore_storage_location_db));
        backupStorageLocationDb = findPreference(getString(R.string.preferences_key_backup_storage_location_db));
        clearStorageLocationDb = findPreference(getString(R.string.preferences_key_clear_storage_location_db));

        settingsViewModel.setPremiumActivationListenerAndSetupBillingClient(requireActivity(), this);
        settingsViewModel.setPreferenceListener(this);
        settingsViewModel.showPreferences();
    }

    private void setListeners() {
        goPremium.setOnPreferenceClickListener(preference -> {
            initPremiumPurchase();
            return false;
        });
        activeUser.setOnPreferenceClickListener(this::showLoginAndRegisterForm);
        backupProductDb.setOnPreferenceClickListener(this::showDialogBackupProductDb);
        restoreProductDb.setOnPreferenceClickListener(this::showDialogRestoreProductDb);
        clearProductDb.setOnPreferenceClickListener(this::showDialogClearProductDb);
        backupCategoryDb.setOnPreferenceClickListener(this::showDialogBackupOwnCategoriesDb);
        restoreCategoryDb.setOnPreferenceClickListener(this::showDialogRestoreOwnCategoriesDb);
        clearCategoryDb.setOnPreferenceClickListener(this::showDialogClearOwnCategoryDb);
        backupStorageLocationDb.setOnPreferenceClickListener(this::showDialogBackupStorageLocationDb);
        restoreStorageLocationDb.setOnPreferenceClickListener(this::showDialogRestoreStorageLocationDb);
        clearStorageLocationDb.setOnPreferenceClickListener(this::showDialogClearStorageLocationDb);
        importDb.setOnPreferenceClickListener(this::showDialogImportLocalDatabaseToCloud);

        preferenceChangeListener = (prefs, key) -> {
            if (key.equals(getString(R.string.preferences_key_email_address)) ||
                    key.equals(getString(R.string.preferences_key_push_notifications)) ||
                    key.equals(getString(R.string.preferences_key_email_notifications)) ||
                    key.equals(getString(R.string.preferences_key_notification_days_before_expiration))) {
                settingsViewModel.setNotificationsToRestoreFlag();
            }
        };

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private boolean showLoginAndRegisterForm(Preference preference) {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
        return false;
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK)
            activeUser.setSummary(settingsViewModel.getActiveUserEmail());
    }

    public void initPremiumPurchase() {
        settingsViewModel.initPremiumPurchase(requireActivity());
        activeUser.setEnabled(true);
    }

    @Override
    public void isBillingClientReady() {
        goPremium.setEnabled(true);
    }

    @Override
    public void activatePremiumFeatures() {
        enablePremiumFeatures();
    }

    private void enablePremiumFeatures() {
        activeUser.setEnabled(true);
        databaseMode.setEnabled(true);
        importDb.setEnabled(true);
    }

    @Override
    public void showAppVersion(String version) {
        appVersion.setSummary(version);
    }

    @Override
    public void showActiveUserEmail(String email) {
        activeUser.setSummary(email);
    }

    @Override
    public void showInfoForPremiumUserOnly() {
        Toast.makeText(getContext(), getString(R.string.error_for_premium_users_only), Toast.LENGTH_SHORT).show();
    }

    public boolean showDialogBackupProductDb(Preference preference) {
        AlertDialog.Builder dialog = SettingsDialogs.getBackupProductDbDialog(requireActivity());
        dialog.setPositiveButton(getString(android.R.string.ok), (d, click) ->
                        settingsViewModel.onClickBackupProductDatabase())
                .show();
        return false;
    }

    public boolean showDialogRestoreProductDb(Preference preference) {
        AlertDialog.Builder dialog = SettingsDialogs.getRestoreProductDbDialog(requireActivity());
        dialog.setPositiveButton(getString(android.R.string.ok), (d, click) ->
                        settingsViewModel.onClickRestoreProductDatabase())
                .show();
        return false;
    }

    public boolean showDialogBackupOwnCategoriesDb(Preference preference) {
        AlertDialog.Builder dialog = SettingsDialogs.getBackupOwnCategoriesDbDialog(requireActivity());
        dialog.setPositiveButton(getString(android.R.string.ok), (d, click) ->
                        settingsViewModel.onClickBackupCategoryDatabase())
                .show();
        return false;
    }

    public boolean showDialogRestoreOwnCategoriesDb(Preference preference) {
        AlertDialog.Builder dialog = SettingsDialogs.getRestoreOwnCategoriesDbDialog(requireActivity());
        dialog.setPositiveButton(getString(android.R.string.ok), (d, click) ->
                        settingsViewModel.onClickRestoreCategoryDatabase())
                .show();
        return false;
    }

    public boolean showDialogBackupStorageLocationDb(Preference preference) {
        AlertDialog.Builder dialog = SettingsDialogs.getBackupStorageLocationsDbDialog(requireActivity());
        dialog.setPositiveButton(getString(android.R.string.ok), (d, click) ->
                        settingsViewModel.onClickBackupStorageLocationDatabase())
                .show();
        return false;
    }

    public boolean showDialogRestoreStorageLocationDb(Preference preference) {
        AlertDialog.Builder dialog = SettingsDialogs.getRestoreStorageLocationsDbDialog(requireActivity());
        dialog.setPositiveButton(getString(android.R.string.ok), (d, click) ->
                        settingsViewModel.onClickRestoreStorageLocationDatabase())
                .show();
        return false;
    }

    public boolean showDialogImportLocalDatabaseToCloud(Preference preference) {
        AlertDialog.Builder dialog = SettingsDialogs.getImportProductLocalDbToCloud(requireActivity());
        dialog.setPositiveButton(getString(android.R.string.ok), (d, click) ->
                        settingsViewModel.onClickImportLocalDatabaseToCloud())
                .show();
        return false;
    }

    public boolean showDialogClearProductDb(Preference preference) {
        AlertDialog.Builder dialog = SettingsDialogs.getImportProductLocalDbToCloud(requireActivity());
        dialog.setPositiveButton(getString(android.R.string.ok), (d, click) ->
                        settingsViewModel.onClickClearProductDatabase())
                .show();
        return false;
    }

    public boolean showDialogClearOwnCategoryDb(Preference preference) {
        AlertDialog.Builder dialog = SettingsDialogs.getImportProductLocalDbToCloud(requireActivity());
        dialog.setPositiveButton(getString(android.R.string.ok), (d, click) ->
                        settingsViewModel.onClickClearCategoryDatabase())
                .show();
        return false;
    }

    public boolean showDialogClearStorageLocationDb(Preference preference) {
        AlertDialog.Builder dialog = SettingsDialogs.getImportProductLocalDbToCloud(requireActivity());
        dialog.setPositiveButton(getString(android.R.string.ok), (d, click) ->
                        settingsViewModel.onClickClearStorageLocationDatabase())
                .show();
        return false;
    }

    @Override
    public void observeAvailableData() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                productViewModel::updateDataForSelectedDatabase);
        productViewModel.getAllProductList().observe(getViewLifecycleOwner(),
                settingsViewModel::setProductList);
    }

    @Override
    public void onPause() {
        productViewModel.clearDisposable();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onPause();
    }
}