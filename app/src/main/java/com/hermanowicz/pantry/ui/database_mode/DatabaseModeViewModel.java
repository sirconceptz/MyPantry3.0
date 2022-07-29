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
    DatabaseModeUseCaseImpl databaseModeUseCase;

    @Inject
    public DatabaseModeViewModel(DatabaseModeUseCaseImpl databaseModeUseCase){
        this.databaseModeUseCase = databaseModeUseCase;
        setDatabaseFromSettings();
    }

    public SharedPreferences.OnSharedPreferenceChangeListener sharedPreferencesListener = (sharedPreferences, key) -> {
        if(key.equals("DATABASE_MODE")){
            setDatabaseFromSettings();
        }
    };

    private void setDatabaseFromSettings() {
        DatabaseMode databaseMode = databaseModeUseCase.getDatabaseModeFromSettings();
        setDatabaseMode(databaseMode);
    }

    private void setDatabaseMode(DatabaseMode databaseMode){
        if(databaseModeUseCase.isDatabaseChanged())
            databaseModeUseCase.setDatabaseMode(databaseMode);
    }

    public LiveData<DatabaseMode> getDatabaseMode(){
        return databaseModeUseCase.getDatabaseMode();
    }
}