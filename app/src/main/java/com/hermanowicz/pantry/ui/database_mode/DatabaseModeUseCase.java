package com.hermanowicz.pantry.ui.database_mode;

import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.model.Database;

public interface DatabaseModeUseCase {
    MutableLiveData<Database> getDatabaseMode();

    void setDatabaseMode(Database databaseMode);

    Database getDatabaseModeFromSettings();

    boolean isDatabaseChanged();
}