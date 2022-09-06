package com.hermanowicz.pantry.ui.storage_locations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.repository.StorageLocationRepository;

import java.util.List;

import javax.inject.Inject;

public class StorageLocationsUseCaseImpl implements StorageLocationsUseCase {

    private final StorageLocationRepository repository;

    @Inject
    public StorageLocationsUseCaseImpl(StorageLocationRepository storageLocationRepository) {
        repository = storageLocationRepository;
    }

    @Override
    public LiveData<List<StorageLocation>> getAllStorageLocations(DatabaseMode databaseMode) {
        return repository.getAllStorageLocations(databaseMode);
    }

    @Override
    public void setOnlineStorageLocationList(MutableLiveData<List<StorageLocation>> onlineStorageLocationList) {
        repository.setOnlineStorageLocationList(onlineStorageLocationList);
    }

    @Override
    public boolean checkIsInternetConnection() {
        return repository.checkIsInternetConnection();
    }
}