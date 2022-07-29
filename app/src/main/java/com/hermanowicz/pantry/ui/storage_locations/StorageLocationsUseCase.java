package com.hermanowicz.pantry.ui.storage_locations;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;

import java.util.List;

public interface StorageLocationsUseCase {
    LiveData<List<StorageLocation>> getAllStorageLocations();
}