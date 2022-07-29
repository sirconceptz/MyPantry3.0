package com.hermanowicz.pantry.ui.edit_product;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.repository.ProductRepository;
import com.hermanowicz.pantry.repository.StorageLocationRepository;

import javax.inject.Inject;

public class EditProductUseCaseImpl implements EditProductUseCase {

    private final ProductRepository productRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final OwnCategoryRepository ownCategoryRepository;

    @Inject
    public EditProductUseCaseImpl(ProductRepository productRepository,
                                  StorageLocationRepository storageLocationRepository,
                                  OwnCategoryRepository ownCategoryRepository){
        this.productRepository = productRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.ownCategoryRepository = ownCategoryRepository;
    }

    @Override
    public void update(Product[] products){
    }

    @Override
    public LiveData<String[]> getAllStorageLocationsNames(){
        return storageLocationRepository.getAllStorageLocationsNames();
    }

    @Override
    public LiveData<String[]> getAllOwnCategoriesNames(){
        return ownCategoryRepository.getOwnCategoriesNames();
    }

}