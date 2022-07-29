package com.hermanowicz.pantry.ui.new_product;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.model.GroupProduct;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.repository.ProductRepository;
import com.hermanowicz.pantry.repository.StorageLocationRepository;

import javax.inject.Inject;

public class NewProductUseCaseImpl implements NewProductUseCase {

    private final ProductRepository productRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final OwnCategoryRepository ownCategoryRepository;
    private final Resources resources;

    @Inject
    public NewProductUseCaseImpl(ProductRepository productRepository,
                                 StorageLocationRepository storageLocationRepository,
                                 OwnCategoryRepository ownCategoryRepository,
                                 Resources resources){
        this.productRepository = productRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.ownCategoryRepository = ownCategoryRepository;
        this.resources = resources;
    }

    @Override
    public void insert(GroupProduct groupProduct) {

    }

    @Override
    public LiveData<String[]> getAllStorageLocations(){
        return storageLocationRepository.getAllStorageLocationsNames();
    }

    @Override
    public LiveData<String[]> getAllOwnCategories(){
        return ownCategoryRepository.getOwnCategoriesNames();
    }

    @Override
    public Resources getResources(){
        return resources;
    }
}