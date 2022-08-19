package com.hermanowicz.pantry.ui.new_product;

import android.content.res.Resources;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.Database;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.repository.ProductRepository;
import com.hermanowicz.pantry.repository.StorageLocationRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NewProductUseCaseImpl implements NewProductUseCase {

    private final ProductRepository productRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final OwnCategoryRepository ownCategoryRepository;
    private final Resources resources;
    private Database databaseMode;

    @Inject
    public NewProductUseCaseImpl(ProductRepository productRepository,
                                 StorageLocationRepository storageLocationRepository,
                                 OwnCategoryRepository ownCategoryRepository,
                                 Resources resources) {
        this.productRepository = productRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.ownCategoryRepository = ownCategoryRepository;
        this.resources = resources;
    }

    @Override
    public void insert(List<Product> productList) {
        productRepository.insert(productList, databaseMode);
    }

    @Override
    public LiveData<String[]> getAllStorageLocations() {
        return storageLocationRepository.getAllStorageLocationsNames();
    }

    @Override
    public LiveData<String[]> getAllOwnCategories() {
        return ownCategoryRepository.getOwnCategoriesNames();
    }

    @Override
    public ArrayList<Product> getProductListToInsert() {
        return productRepository.getProductListToInsert();
    }

    @Override
    public Resources getResources() {
        return resources;
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