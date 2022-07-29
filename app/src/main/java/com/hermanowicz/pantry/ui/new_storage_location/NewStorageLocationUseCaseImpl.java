package com.hermanowicz.pantry.ui.new_storage_location;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.repository.StorageLocationRepository;

import javax.inject.Inject;

public class NewStorageLocationUseCaseImpl implements NewStorageLocationUseCase{

    private final StorageLocationRepository repository;

    @Inject
    public NewStorageLocationUseCaseImpl(StorageLocationRepository storageLocationRepository){
        repository = storageLocationRepository;
    }

    @Override
    public void insert(StorageLocation storageLocation){
        repository.insert(storageLocation);
    }
}