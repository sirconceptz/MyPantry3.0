package com.hermanowicz.pantry.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.List;

public interface StorageLocationRepository {
    LiveData<List<StorageLocation>> getAllStorageLocations(DatabaseMode databaseMode);

    LiveData<String[]> getAllStorageLocationsNames();

    void insert(StorageLocation storageLocation, DatabaseMode databaseMode);

    void update(StorageLocation storageLocation, DatabaseMode databaseMode);

    void delete(StorageLocation storageLocation, DatabaseMode databaseMode);

    void deleteAll(DatabaseMode databaseMode);

    void setOnlineStorageLocationList(MutableLiveData<List<StorageLocation>> onlineStorageLocationList);

    boolean checkIsInternetConnection();

    List<StorageLocation> getAllLocalStorageLocationsAsList();
}