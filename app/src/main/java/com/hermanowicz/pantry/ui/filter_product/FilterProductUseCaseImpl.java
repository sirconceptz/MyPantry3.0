package com.hermanowicz.pantry.ui.filter_product;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.repository.StorageLocationRepository;

import javax.inject.Inject;

public class FilterProductUseCaseImpl implements FilterProductUseCase {

    private final StorageLocationRepository storageLocationRepository;
    private final OwnCategoryRepository ownCategoryRepository;

    @Inject
    public FilterProductUseCaseImpl(StorageLocationRepository storageLocationRepository,
                                    OwnCategoryRepository ownCategoryRepository) {
        this.storageLocationRepository = storageLocationRepository;
        this.ownCategoryRepository = ownCategoryRepository;
    }

    @Override
    public LiveData<String[]> getAllStorageLocationsNames() {
        return storageLocationRepository.getAllStorageLocationsNames();
    }

    @Override
    public LiveData<String[]> getAllOwnCategoriesNames() {
        return ownCategoryRepository.getOwnCategoriesNames();
    }
}