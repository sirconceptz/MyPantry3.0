package com.hermanowicz.pantry.ui.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.FilterModel;
import com.hermanowicz.pantry.model.GroupProduct;

import java.util.ArrayList;
import java.util.List;

public interface ProductUseCase {
    LiveData<List<Product>> getAllProducts();

    List<GroupProduct> getAllGroupProductList(List<Product> productList);

    void setOnlineProductList(MutableLiveData<List<Product>> onlineProductList);

    boolean checkIsInternetConnection();

    void setFilterModel(FilterModel filterModel);

    LiveData<List<Product>> getFilteredProductList(List<Product> productList);

    void setDatabaseMode(DatabaseMode databaseMode);
}
