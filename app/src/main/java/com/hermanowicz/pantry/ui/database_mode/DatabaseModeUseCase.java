package com.hermanowicz.pantry.ui.database_mode;

import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.model.DatabaseMode;

public interface DatabaseModeUseCase {
    MutableLiveData<DatabaseMode> getDatabaseMode();

    void setDatabaseMode(DatabaseMode databaseMode);

    DatabaseMode getDatabaseModeFromSettings();

    boolean isDatabaseChanged();
}