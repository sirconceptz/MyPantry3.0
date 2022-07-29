package com.hermanowicz.pantry.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.interfaces.PricingListener;
import com.hermanowicz.pantry.interfaces.ShowPreferencesListener;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.settings.SettingsViewModel;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat implements PricingListener, PurchasesUpdatedListener, ShowPreferencesListener {

    private SettingsViewModel settingsViewModel;
    private DatabaseModeViewModel databaseModeViewModel;

    private Preference goPremium;
    private Preference databaseMode;
    private Preference importDb;
    private Preference selectedTheme;
    private Preference activeUser;
    private Preference appVersion;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        initView(rootKey);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.setPremiumActivationListenerAndSetupBillingClient(requireActivity(), this);
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);

        setListeners();
        settingsViewModel.showPreferences(this);
    }

    private void initView(String rootKey){
        setPreferencesFromResource(R.xml.app_preferences, rootKey);
        goPremium = findPreference(getString(R.string.PreferencesKey_go_premium));
        databaseMode = findPreference(getString(R.string.PreferencesKey_database_mode));
        importDb = findPreference(getString(R.string.PreferencesKey_import_db));
        selectedTheme = findPreference(getString(R.string.PreferencesKey_selected_application_theme));
        activeUser = findPreference(getString(R.string.PreferencesKey_active_user));
        appVersion = findPreference(getString(R.string.PreferencesKey_version));
    }

    private void setListeners() {
        goPremium.setOnPreferenceClickListener(preference -> {
            initPremiumPurchase();
            return false;
        });

        selectedTheme.setOnPreferenceClickListener(preference -> {
            return false;
        });

        activeUser.setOnPreferenceClickListener(preference -> {
            showLoginAndRegisterForm();
            return false;
        });

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(requireActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(databaseModeViewModel.sharedPreferencesListener);
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
        if (result.getResultCode() == RESULT_OK) {
            activeUser.setSummary(settingsViewModel.getActiveUserEmail());
        }
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
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
    }

    @Override
    public void showAppVersion(String version) {
        appVersion.setSummary(version);
    }

    @Override
    public void showActiveUserEmail(String email) {
        activeUser.setSummary(email);
    }
}