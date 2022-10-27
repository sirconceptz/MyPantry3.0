package com.hermanowicz.pantry.ui.new_storage_location;

import com.hermanowicz.pantry.data.db.dao.storagelocation.StorageLocation;
import com.hermanowicz.pantry.domain.repository.StorageLocationRepository;
import com.hermanowicz.pantry.domain.usecase.NewStorageLocationUseCase;
import com.hermanowicz.pantry.model.DatabaseMode;

import javax.inject.Inject;

public class NewStorageLocationUseCaseImpl implements NewStorageLocationUseCase {

    private final StorageLocationRepository repository;
    private DatabaseMode databaseMode;

    @Inject
    public NewStorageLocationUseCaseImpl(StorageLocationRepository storageLocationRepository) {
        repository = storageLocationRepository;
    }

    @Override
    public void insert(StorageLocation storageLocation) {
        repository.insert(storageLocation, databaseMode);
    }

    @Override
    public void setDatabase(DatabaseMode databaseMode) {
        this.databaseMode = databaseMode;
    }
}