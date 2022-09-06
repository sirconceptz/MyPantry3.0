package com.hermanowicz.pantry.ui.storage_locations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.List;

public interface StorageLocationsUseCase {
    LiveData<List<StorageLocation>> getAllStorageLocations(DatabaseMode databaseMode);

    void setOnlineStorageLocationList(MutableLiveData<List<StorageLocation>> onlineStorageLocationList);

    boolean checkIsInternetConnection();
}