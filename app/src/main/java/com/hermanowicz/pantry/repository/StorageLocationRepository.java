package com.hermanowicz.pantry.repository;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;

import java.util.List;

public interface StorageLocationRepository {
    LiveData<List<StorageLocation>> getAllStorageLocations();
    LiveData<String[]> getAllStorageLocationsNames();
    void insert(StorageLocation storageLocation);
    void update(StorageLocation storageLocation);
    void delete(StorageLocation storageLocation);
    void deleteAll();
}