package com.hermanowicz.pantry.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.GroupProduct;

import java.util.List;

public interface ProductRepository {
    LiveData<List<Product>> getAllLocalProducts();
    LiveData<List<Product>> getAllOnlineProducts();
    void insert(GroupProduct groupProduct, DatabaseMode databaseMode);
    void update(Product[] products, DatabaseMode databaseMode);
    void delete(Product[] product, DatabaseMode databaseMode);
    void deleteAll(DatabaseMode databaseMode);
    void setOnlineProductList(MutableLiveData<List<Product>> listMutableLiveData);
}