/*
 * Copyright (c) 2019-2022
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.domain.repository.SharedPreferencesRepository;
import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.Objects;

public class SharedPreferencesRepositoryImpl implements SharedPreferencesRepository {

    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<DatabaseMode> databaseModeMutableLiveData = new MutableLiveData<>();

    public SharedPreferencesRepositoryImpl(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        databaseModeMutableLiveData.setValue(getDatabaseModeFromSettings());
    }

    @Override
    public DatabaseMode getDatabaseModeFromSettings() {
        String databaseModeString = sharedPreferences.getString("DATABASE_MODE", "local");
        DatabaseMode databaseMode = new DatabaseMode();
        if (Objects.equals(databaseModeString, "online"))
            databaseMode.setDatabaseMode(DatabaseMode.Mode.ONLINE);
        else
            databaseMode.setDatabaseMode(DatabaseMode.Mode.LOCAL);
        return databaseMode;
    }

    @Override
    public int getSelectedCameraType() {
        String cameraType = sharedPreferences.getString("SCAN_CAMERA", "0");
        return Integer.parseInt(cameraType);
    }

    @Override
    public boolean getSelectedSoundMode() {
        return sharedPreferences.getBoolean("SOUND_ON_SCANNER?", true);
    }

    @Override
    public boolean getIsBigPrintQRCode() {
        return sharedPreferences.getBoolean("QR_CODE_SIZE", false);
    }

    @Override
    public boolean getIsNotificationsToRestore() {
        return sharedPreferences.getBoolean("IS_NOTIFICATIONS_TO_RESTORE", false);
    }

    @Override
    public void setIsNotificationsToRestore(boolean notificationsToRestore) {
        sharedPreferences.edit().putBoolean("IS_NOTIFICATIONS_TO_RESTORE", notificationsToRestore).apply();
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