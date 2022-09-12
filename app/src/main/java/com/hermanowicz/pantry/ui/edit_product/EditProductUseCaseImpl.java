package com.hermanowicz.pantry.ui.edit_product;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.repository.ProductRepository;
import com.hermanowicz.pantry.repository.StorageLocationRepository;
import com.hermanowicz.pantry.util.DateHelper;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class EditProductUseCaseImpl implements EditProductUseCase {

    private final ProductRepository productRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final OwnCategoryRepository ownCategoryRepository;
    private DatabaseMode databaseMode;
    private String expirationDate;
    private String productionDate;

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
    public void setDatabaseMode(DatabaseMode databaseMode) {
        this.databaseMode = databaseMode;
    }

    @Override
    public int getIntValueFromObservableField(ObservableField<String> observableField) {
        return productRepository.getIntValueFromObservableField(observableField);
    }

    @Override
    public void setExpirationDate(int year, int month, int day) {
        expirationDate = year + "." + month + "." + day;
    }

    @Override
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public void setProductionDate(int year, int month, int day) {
        productionDate = year + "." + month + "." + day;
    }

    @Override
    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    @Override
    public void clearExpirationDate() {
        expirationDate = "";
    }

    @Override
    public void clearProductionDate() {
        productionDate = "";
    }

    @Override
    public String getExpirationDate() {
        return expirationDate;
    }

    @Override
    public String getProductionDate() {
        return productionDate;
    }

    @Override
    public int[] getExpirationDateArray() {
        String[] dateArrayString = expirationDate.split("\\.");
        return Arrays.stream(dateArrayString).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public int[] getProductionDateArray() {
        String[] dateArrayString = productionDate.split("\\.");
        return Arrays.stream(dateArrayString).mapToInt(Integer::parseInt).toArray();
    }
}