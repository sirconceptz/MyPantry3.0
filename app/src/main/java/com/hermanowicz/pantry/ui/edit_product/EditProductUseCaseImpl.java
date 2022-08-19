package com.hermanowicz.pantry.ui.edit_product;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.Database;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.repository.ProductRepository;
import com.hermanowicz.pantry.repository.StorageLocationRepository;

import java.util.List;

import javax.inject.Inject;

public class EditProductUseCaseImpl implements EditProductUseCase {

    private final ProductRepository productRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final OwnCategoryRepository ownCategoryRepository;
    private Database databaseMode;

    @Inject
    public EditProductUseCaseImpl(ProductRepository productRepository,
                                  StorageLocationRepository storageLocationRepository,
                                  OwnCategoryRepository ownCategoryRepository) {
        this.productRepository = productRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.ownCategoryRepository = ownCategoryRepository;
    }

    @Override
    public void updateProductList(List<Product> productList) {
        productRepository.update(productList, databaseMode);
    }

    @Override
    public LiveData<String[]> getAllStorageLocationsNames() {
        return storageLocationRepository.getAllStorageLocationsNames();
    }

    @Override
    public LiveData<String[]> getAllOwnCategoriesNames() {
        return ownCategoryRepository.getOwnCategoriesNames();
    }

    @Override
    public void setDatabaseMode(Database databaseMode) {
        this.databaseMode = databaseMode;
    }

    @Override
    public int getIntValueFromObservableField(ObservableField<String> observableField) {
        return productRepository.getIntValueFromObservableField(observableField);
    }
}