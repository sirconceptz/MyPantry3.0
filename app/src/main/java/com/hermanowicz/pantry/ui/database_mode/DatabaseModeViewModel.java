package com.hermanowicz.pantry.ui.database_mode;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.model.DatabaseMode;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DatabaseModeViewModel extends ViewModel {

    @Inject
    DatabaseModeUseCaseImpl useCase;
    public SharedPreferences.OnSharedPreferenceChangeListener sharedPreferencesListener = (sharedPreferences, key) -> {
        if (key.equals("DATABASE_MODE")) {
            setDatabaseFromSettings();
        }
    };

    @Inject
    public DatabaseModeViewModel(DatabaseModeUseCaseImpl databaseModeUseCase) {
        this.useCase = databaseModeUseCase;
        setDatabaseFromSettings();
    }

    private void setDatabaseFromSettings() {
        DatabaseMode databaseMode = useCase.getDatabaseModeFromSettings();
        setDatabaseMode(databaseMode);
    }

    public LiveData<DatabaseMode> getDatabaseMode() {
        return useCase.getDatabaseMode();
    }

    private void setDatabaseMode(DatabaseMode databaseMode) {
        if (useCase.isDatabaseChanged())
            useCase.setDatabaseMode(databaseMode);
    }

    public DatabaseMode getDatabaseModeFromSettings() {
        return useCase.getDatabaseModeFromSettings();
    }
}