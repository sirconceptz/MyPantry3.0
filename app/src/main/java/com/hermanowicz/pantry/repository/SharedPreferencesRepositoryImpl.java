package com.hermanowicz.pantry.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.model.Database;

import java.util.Objects;

public class SharedPreferencesRepositoryImpl implements SharedPreferencesRepository {

    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<Database> databaseModeMutableLiveData = new MutableLiveData<>();

    public SharedPreferencesRepositoryImpl(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        databaseModeMutableLiveData.setValue(getDatabaseModeFromSettings());
    }

    @Override
    public Database getDatabaseModeFromSettings() {
        String databaseModeString = sharedPreferences.getString("DATABASE_MODE", "local");
        Database databaseMode = new Database();
        if (Objects.equals(databaseModeString, "online"))
            databaseMode.setDatabaseMode(Database.DatabaseMode.ONLINE);
        else
            databaseMode.setDatabaseMode(Database.DatabaseMode.LOCAL);
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
    public MutableLiveData<Database> getDatabaseMode() {
        return databaseModeMutableLiveData;
    }

    @Override
    public void setDatabaseMode(Database databaseMode) {
        databaseModeMutableLiveData.setValue(databaseMode);
    }
}