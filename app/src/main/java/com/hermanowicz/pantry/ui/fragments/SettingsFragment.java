package com.hermanowicz.pantry.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import com.hermanowicz.pantry.interfaces.ShowPreferencesListener;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.settings.SettingsViewModel;

import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat implements PricingListener, ShowPreferencesListener {

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
        selectedTheme = findPreference(getString(R.string.preferences_key_selected_application_theme));
        activeUser = findPreference(getString(R.string.preferences_key_active_user));
        appVersion = findPreference(getString(R.string.preferences_key_version));

        settingsViewModel.setPremiumActivationListenerAndSetupBillingClient(requireActivity(), this);
        settingsViewModel.showPreferences(this);
    }

    private void setListeners() {
        goPremium.setOnPreferenceClickListener(preference -> {
            initPremiumPurchase();
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
    public void showAppVersion(String version) {
        appVersion.setSummary(version);
    }

    @Override
    public void showActiveUserEmail(String email) {
        activeUser.setSummary(email);
    }
}