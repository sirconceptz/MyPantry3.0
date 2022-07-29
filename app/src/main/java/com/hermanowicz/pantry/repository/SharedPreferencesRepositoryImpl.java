package com.hermanowicz.pantry.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.Objects;

public class SharedPreferencesRepositoryImpl implements SharedPreferencesRepository {

    private final SharedPreferences sharedPreferences;
    private MutableLiveData<DatabaseMode> databaseModeMutableLiveData = new MutableLiveData<>();

    public SharedPreferencesRepositoryImpl(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        databaseModeMutableLiveData.setValue(getDatabaseModeFromSettings());
    }

    @Override
    public DatabaseMode getDatabaseModeFromSettings(){
        String databaseModeString = sharedPreferences.getString("DATABASE_MODE", "local");
        DatabaseMode databaseMode = new DatabaseMode();
        if(Objects.equals(databaseModeString, "online"))
            databaseMode.setDatabaseMode(DatabaseMode.Mode.ONLINE);
        else
            databaseMode.setDatabaseMode(DatabaseMode.Mode.LOCAL);
        return databaseMode;
    }

    @Override
    public MutableLiveData<DatabaseMode> getDatabaseMode() {
        return databaseModeMutableLiveData;
    }

    @Override
    public void setDatabaseMode(DatabaseMode databaseMode) {
        databaseModeMutableLiveData.setValue(databaseMode);
    }
}