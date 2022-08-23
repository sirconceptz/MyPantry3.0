package com.hermanowicz.pantry.ui.database_mode;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.model.Database;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DatabaseModeViewModel extends ViewModel {

    @Inject
    DatabaseModeUseCaseImpl useCase;

    @Inject
    public DatabaseModeViewModel(DatabaseModeUseCaseImpl databaseModeUseCase) {
        this.useCase = databaseModeUseCase;
        setDatabaseFromSettings();
    }

    public SharedPreferences.OnSharedPreferenceChangeListener sharedPreferencesListener = (sharedPreferences, key) -> {
        if (key.equals("DATABASE_MODE")) {
            setDatabaseFromSettings();
        }
    };

    private void setDatabaseFromSettings() {
        Database databaseMode = useCase.getDatabaseModeFromSettings();
        setDatabaseMode(databaseMode);
    }

    private void setDatabaseMode(Database databaseMode) {
        if (useCase.isDatabaseChanged())
            useCase.setDatabaseMode(databaseMode);
    }

    public LiveData<Database> getDatabaseMode() {
        return useCase.getDatabaseMode();
    }

    public Database getDatabaseModeFromSettings() {
        return useCase.getDatabaseModeFromSettings();
    }
}