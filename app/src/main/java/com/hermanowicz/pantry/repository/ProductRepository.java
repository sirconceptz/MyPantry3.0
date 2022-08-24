package com.hermanowicz.pantry.repository;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.Database;

import java.util.ArrayList;
import java.util.List;

public interface ProductRepository {
    LiveData<List<Product>> getAllProducts(Database databaseMode);

    ArrayList<Product> getProductListToInsert();

    void insert(List<Product> productList, Database databaseMode);

    void update(List<Product> productList, Database databaseMode);

    void delete(List<Product> productList, Database databaseMode);

    void deleteAll(Database databaseMode);

    void setOnlineProductList(MutableLiveData<List<Product>> listMutableLiveData);

    int getIntValueFromObservableField(ObservableField<String> observableField);

    boolean checkIsInternetConnection();

    List<Product> getAllLocalProductsAsList();
}