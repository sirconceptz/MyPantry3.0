package com.hermanowicz.pantry.repository;

import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.model.DatabaseMode;

public interface SharedPreferencesRepository {
    MutableLiveData<DatabaseMode> getDatabaseMode();

    void setDatabaseMode(DatabaseMode databaseMode);

    DatabaseMode getDatabaseModeFromSettings();

    int getSelectedCameraType();

    boolean getSelectedSoundMode();

    boolean getIsBigPrintQRCode();

    boolean getIsNotificationsToRestore();

    void setIsNotificationsToRestore(boolean notificationsToRestore);
}