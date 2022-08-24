package com.hermanowicz.pantry.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.model.Database;

import java.util.List;

public interface StorageLocationRepository {
    LiveData<List<StorageLocation>> getAllStorageLocations(Database databaseMode);

    LiveData<String[]> getAllStorageLocationsNames();

    void insert(StorageLocation storageLocation, Database databaseMode);

    void update(StorageLocation storageLocation, Database databaseMode);

    void delete(StorageLocation storageLocation, Database databaseMode);

    void deleteAll(Database databaseMode);

    void setOnlineStorageLocationList(MutableLiveData<List<StorageLocation>> onlineStorageLocationList);

    boolean checkIsInternetConnection();

    List<StorageLocation> getAllLocalStorageLocationsAsList();
}