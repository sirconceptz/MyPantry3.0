package com.hermanowicz.pantry.ui.new_product;

import android.content.res.Resources;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.GroupProduct;

import java.util.ArrayList;
import java.util.List;

public interface NewProductUseCase {
    LiveData<String[]> getAllStorageLocations();

    LiveData<String[]> getAllOwnCategories();

    ArrayList<Product> getProductListToInsert();

    void insert(List<Product> productList);

    Resources getResources();

    void setDatabaseMode(DatabaseMode databaseMode);

    int getIntValueFromObservableField(ObservableField<String> observableField);

    String getDateInFormatToShow(int day, int month, int year);

    List<GroupProduct> setAndGetGroupProductListFromProductList(ArrayList<Product> productArrayList);

    String[] getGroupProductNames(ArrayList<Product> productArrayList);

    List<GroupProduct> getGroupProductList();

    void setExpirationDate(int year, int month, int day);

    void setProductionDate(int year, int month, int day);

    String getExpirationDate();

    String getProductionDate();

    void clearExpirationDate();

    void clearProductionDate();
}