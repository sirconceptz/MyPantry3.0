package com.hermanowicz.pantry.ui.database_mode;

import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.model.Database;
import com.hermanowicz.pantry.repository.SharedPreferencesRepository;

import javax.inject.Inject;

public class DatabaseModeUseCaseImpl implements DatabaseModeUseCase {

    private final SharedPreferencesRepository repository;

    @Inject
    public DatabaseModeUseCaseImpl(SharedPreferencesRepository sharedPreferencesRepository) {
        this.repository = sharedPreferencesRepository;

    }

    @Override
    public MutableLiveData<Database> getDatabaseMode() {
        return repository.getDatabaseMode();
    }

    @Override
    public void setDatabaseMode(Database databaseMode) {
        repository.setDatabaseMode(databaseMode);
    }

    @Override
    public Database getDatabaseModeFromSettings() {
        return repository.getDatabaseModeFromSettings();
    }

    @Override
    public boolean isDatabaseChanged() {
        Database oldDatabaseMode = getDatabaseMode().getValue();
        Database newDatabaseMode = getDatabaseModeFromSettings();
        return oldDatabaseMode != newDatabaseMode;
    }
}
