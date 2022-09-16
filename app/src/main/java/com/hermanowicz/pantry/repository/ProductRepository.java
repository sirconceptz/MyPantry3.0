package com.hermanowicz.pantry.repository;

import android.graphics.Bitmap;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.photo.Photo;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.GroupProduct;

import java.util.ArrayList;
import java.util.List;

public interface ProductRepository {
    LiveData<List<Product>> getAllProducts(DatabaseMode databaseMode);

    ArrayList<Product> getProductListToInsert();

    void insert(List<Product> productList, DatabaseMode databaseMode);

    void update(List<Product> productList, DatabaseMode databaseMode);

    void delete(List<Product> productList, DatabaseMode databaseMode);

    void deleteAll(DatabaseMode databaseMode);

    void setOnlineProductList(MutableLiveData<List<Product>> listMutableLiveData);

    int getIntValueFromObservableField(ObservableField<String> observableField);

    boolean checkIsInternetConnection();

    List<Product> getAllLocalProductsAsList();

    void addOfflinePhoto(String photoDescription, ArrayList<Product> productArrayList, String fileName);

    void addOnlinePhoto(Bitmap bitmap, String photoDescription, List<Product> productList, List<Photo> currentPhotoList);

    void deleteOfflinePhoto(ArrayList<Product> productArrayList);

    void deleteOnlinePhoto(ArrayList<Product> productArrayList);

    String[] getGroupProductNames(ArrayList<Product> productArrayList);

    List<GroupProduct> getGroupProductListFromProductList(ArrayList<Product> productArrayList);

    List<GroupProduct> getGroupProductList();
}