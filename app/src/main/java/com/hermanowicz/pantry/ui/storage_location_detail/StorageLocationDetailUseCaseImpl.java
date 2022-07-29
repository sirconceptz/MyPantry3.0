package com.hermanowicz.pantry.ui.storage_location_detail;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.repository.StorageLocationRepository;

import javax.inject.Inject;

public class StorageLocationDetailUseCaseImpl implements StorageLocationDetailUseCase{

    private final StorageLocationRepository repository;

    @Inject
    public StorageLocationDetailUseCaseImpl(StorageLocationRepository storageLocationRepository){
        repository = storageLocationRepository;
    }

    @Override
    public void deleteStorageLocation(StorageLocation storageLocation) {
        repository.delete(storageLocation);
    }

    @Override
    public void updateStorageLocation(StorageLocation storageLocation){
        repository.update(storageLocation);
    }
}