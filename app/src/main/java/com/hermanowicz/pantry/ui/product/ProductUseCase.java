package com.hermanowicz.pantry.ui.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.Database;
import com.hermanowicz.pantry.model.GroupProduct;

import java.util.List;

public interface ProductUseCase {
    LiveData<List<Product>> getAllProducts(Database databaseMode);

    List<GroupProduct> getAllGroupProductList(List<Product> productList);

    void setOnlineProductList(MutableLiveData<List<Product>> onlineProductList);

    boolean checkIsInternetConnection();
}
