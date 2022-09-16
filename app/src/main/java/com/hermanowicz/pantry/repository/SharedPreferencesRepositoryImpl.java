package com.hermanowicz.pantry.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.Objects;

public class SharedPreferencesRepositoryImpl implements SharedPreferencesRepository {

    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<DatabaseMode> databaseModeMutableLiveData = new MutableLiveData<>();

    public SharedPreferencesRepositoryImpl(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        databaseModeMutableLiveData.setValue(getDatabaseModeFromSettings());
    }

    @Override
    public DatabaseMode getDatabaseModeFromSettings() {
        String databaseModeString = sharedPreferences.getString("DATABASE_MODE", "local");
        DatabaseMode databaseMode = new DatabaseMode();
        if (Objects.equals(databaseModeString, "online"))
            databaseMode.setDatabaseMode(DatabaseMode.Mode.ONLINE);
        else
            databaseMode.setDatabaseMode(DatabaseMode.Mode.LOCAL);
        return databaseMode;
    }

    @Override
    public int getSelectedCameraType() {
        String cameraType = sharedPreferences.getString("SCAN_CAMERA", "0");
        return Integer.parseInt(cameraType);
    }

    @Override
    public boolean getSelectedSoundMode() {
        return sharedPreferences.getBoolean("SOUND_ON_SCANNER?", true);
    }

    @Override
    public boolean getIsBigPrintQRCode() {
        return sharedPreferences.getBoolean("QR_CODE_SIZE", false);
    }

    @Override
    public boolean getIsNotificationsToRestore() {
        return sharedPreferences.getBoolean("IS_NOTIFICATIONS_TO_RESTORE", false);
    }

    @Override
    public void setIsNotificationsToRestore(boolean notificationsToRestore) {
        sharedPreferences.edit().putBoolean("IS_NOTIFICATIONS_TO_RESTORE", notificationsToRestore).apply();
    }

    @Override
    public MutableLiveData<DatabaseMode> getDatabaseMode() {
        return databaseModeMutableLiveData;
    }

    @Override
    public void setDatabaseMode(DatabaseMode databaseMode) {
        databaseModeMutableLiveData.setValue(databaseMode);
    }
}