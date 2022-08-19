package com.hermanowicz.pantry.repository;

import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.model.Database;

public interface SharedPreferencesRepository {
    MutableLiveData<Database> getDatabaseMode();

    void setDatabaseMode(Database databaseMode);

    Database getDatabaseModeFromSettings();

    int getSelectedCameraType();

    boolean getSelectedSoundMode();

    boolean getIsBigPrintQRCode();
}