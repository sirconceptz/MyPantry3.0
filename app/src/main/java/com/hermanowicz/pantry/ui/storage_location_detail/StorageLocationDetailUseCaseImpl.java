package com.hermanowicz.pantry.ui.storage_location_detail;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.repository.StorageLocationRepository;

import javax.inject.Inject;

public class StorageLocationDetailUseCaseImpl implements StorageLocationDetailUseCase {

    private final StorageLocationRepository repository;
    private DatabaseMode databaseMode;

    @Inject
    public StorageLocationDetailUseCaseImpl(StorageLocationRepository storageLocationRepository) {
        repository = storageLocationRepository;
    }

    @Override
    public void deleteStorageLocation(StorageLocation storageLocation) {
        repository.delete(storageLocation, databaseMode);
    }

    @Override
    public void updateStorageLocation(StorageLocation storageLocation) {
        repository.update(storageLocation, databaseMode);
    }

    public void setDatabaseMode(DatabaseMode databaseMode) {
        this.databaseMode = databaseMode;
    }
}