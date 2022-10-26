package com.hermanowicz.pantry.ui.database_mode;

import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.data.repository.SharedPreferencesRepository;
import com.hermanowicz.pantry.domain.usecase.DatabaseModeUseCase;
import com.hermanowicz.pantry.model.DatabaseMode;

import javax.inject.Inject;

public class DatabaseModeUseCaseImpl implements DatabaseModeUseCase {

    private final SharedPreferencesRepository repository;

    @Inject
    public DatabaseModeUseCaseImpl(SharedPreferencesRepository sharedPreferencesRepository) {
        this.repository = sharedPreferencesRepository;

    }

    @Override
    public MutableLiveData<DatabaseMode> getDatabaseMode() {
        return repository.getDatabaseMode();
    }

    @Override
    public void setDatabaseMode(DatabaseMode databaseMode) {
        repository.setDatabaseMode(databaseMode);
    }

    @Override
    public DatabaseMode getDatabaseModeFromSettings() {
        return repository.getDatabaseModeFromSettings();
    }

    @Override
    public boolean isDatabaseChanged() {
        DatabaseMode oldDatabaseMode = getDatabaseMode().getValue();
        DatabaseMode newDatabaseMode = getDatabaseModeFromSettings();
        return oldDatabaseMode != newDatabaseMode;
    }
}
