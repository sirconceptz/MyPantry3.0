package com.hermanowicz.pantry.ui.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.domain.repository.ProductRepository;
import com.hermanowicz.pantry.domain.usecase.ProductUseCase;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.Filter;
import com.hermanowicz.pantry.model.FilterModel;
import com.hermanowicz.pantry.model.GroupProduct;

import java.util.List;

import javax.inject.Inject;

public class ProductUseCaseImpl implements ProductUseCase {

    private final ProductRepository repository;
    private final MutableLiveData<FilterModel> filterModel = new MutableLiveData<>(new FilterModel());
    private DatabaseMode databaseMode;

    @Inject
    public ProductUseCaseImpl(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    @Override
    public LiveData<List<Product>> getAllProducts() {
        return repository.getAllProducts(databaseMode);
    }

    @Override
    public List<GroupProduct> getAllGroupProductList(List<Product> productList) {
        return GroupProduct.getGroupProducts(productList);
    }

    @Override
    public void setOnlineProductList(MutableLiveData<List<Product>> onlineProductList) {
        repository.setOnlineProductList(onlineProductList);
    }

    @Override
    public boolean checkIsInternetConnection() {
        return repository.checkIsInternetConnection();
    }

    @Override
    public void setFilterModel(FilterModel filterModel) {
        this.filterModel.setValue(filterModel);
    }

    @Override
    public LiveData<List<Product>> getFilteredProductList(List<Product> productList) {
        Filter filter = new Filter(productList);
        return Transformations.switchMap(filterModel, filter::filterByProduct);
    }

    @Override
    public void setDatabaseMode(DatabaseMode databaseMode) {
        this.databaseMode = databaseMode;
    }
}