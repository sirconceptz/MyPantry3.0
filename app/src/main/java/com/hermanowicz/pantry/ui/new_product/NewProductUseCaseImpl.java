package com.hermanowicz.pantry.ui.new_product;

import android.content.res.Resources;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.GroupProduct;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.repository.ProductRepository;
import com.hermanowicz.pantry.repository.StorageLocationRepository;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class NewProductUseCaseImpl implements NewProductUseCase {

    private final ProductRepository productRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final OwnCategoryRepository ownCategoryRepository;
    private final Resources resources;
    private DatabaseMode databaseMode;
    private String expirationDate = "";
    private String productionDate = "";

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
    public void setDatabaseMode(DatabaseMode databaseMode) {
        this.databaseMode = databaseMode;
    }

    @Override
    public int getIntValueFromObservableField(ObservableField<String> observableField) {
        return productRepository.getIntValueFromObservableField(observableField);
    }

    @Override
    public String getDateInFormatToShow(int day, int month, int year) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

    @Override
    public List<GroupProduct> setAndGetGroupProductListFromProductList(ArrayList<Product> productArrayList) {
        return productRepository.getGroupProductListFromProductList(productArrayList);
    }

    @Override
    public String[] getGroupProductNames(ArrayList<Product> productArrayList) {
        return productRepository.getGroupProductNames(productArrayList);
    }

    @Override
    public List<GroupProduct> getGroupProductList() {
        return productRepository.getGroupProductList();
    }

    @Override
    public void setExpirationDate(int year, int month, int day) {
        expirationDate = year + "." + month + "." + day;
    }

    @Override
    public void setProductionDate(int year, int month, int day) {
        productionDate = year + "." + month + "." + day;
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
    public void clearExpirationDate() {
        expirationDate = "";
    }

    @Override
    public void clearProductionDate() {
        productionDate = "";
    }
}