package com.hermanowicz.pantry.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.interfaces.PricingListener;
import com.hermanowicz.pantry.interfaces.PreferencesListener;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.settings.SettingsViewModel;

import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat implements PricingListener, PreferencesListener {

    private SettingsViewModel settingsViewModel;
    private DatabaseModeViewModel databaseModeViewModel;

    private Preference goPremium;
    private Preference databaseMode;
    private Preference importDb;
    private Preference activeUser;
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
    }

    @Override
    public void onResume(){
        initView();
        setListeners();

        super.onResume();
    }

    private void initView() {
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
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(requireActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(databaseModeViewModel.sharedPreferencesListener);
        
        goPremium.setOnPreferenceClickListener(preference -> {
            initPremiumPurchase();
            return false;
        });
        activeUser.setOnPreferenceClickListener(preference -> {
            showLoginAndRegisterForm();
            return false;
        });
        backupProductDb.setOnPreferenceClickListener(preference -> {
            settingsViewModel.onClickBackupProductDatabase();
            return false;
        });
        restoreProductDb.setOnPreferenceClickListener(preference -> {
            settingsViewModel.onClickRestoreProductDatabase();
            return false;
        });
        clearProductDb.setOnPreferenceClickListener(preference -> {
            settingsViewModel.onClickClearProductDatabase();
            return false;
        });
        backupCategoryDb.setOnPreferenceClickListener(preference -> {
            settingsViewModel.onClickBackupCategoryDatabase();
            return false;
        });
        restoreCategoryDb.setOnPreferenceClickListener(preference -> {
            settingsViewModel.onClickRestoreCategoryDatabase();
            return false;
        });
        clearCategoryDb.setOnPreferenceClickListener(preference -> {
            settingsViewModel.onClickClearCategoryDatabase();
            return false;
        });
        backupStorageLocationDb.setOnPreferenceClickListener(preference -> {
            settingsViewModel.onClickBackupStorageLocationDatabase();
            return false;
        });
        restoreStorageLocationDb.setOnPreferenceClickListener(preference -> {
            settingsViewModel.onClickRestoreStorageLocationDatabase();
            return false;
        });
        clearStorageLocationDb.setOnPreferenceClickListener(preference -> {
            settingsViewModel.onClickClearStorageLocationDatabase();
            return false;
        });
        importDb.setOnPreferenceClickListener(preference -> {
            settingsViewModel.onClickImportDb();
            return false;
        });
    }

    private void showLoginAndRegisterForm() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK)
            activeUser.setSummary(settingsViewModel.getActiveUserEmail());
    }

    private void initPremiumPurchase() {
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
}