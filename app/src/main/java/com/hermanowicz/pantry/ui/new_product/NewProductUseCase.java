package com.hermanowicz.pantry.ui.new_product;

import android.content.res.Resources;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.Database;

import java.util.ArrayList;
import java.util.List;

public interface NewProductUseCase {
    LiveData<String[]> getAllStorageLocations();

    LiveData<String[]> getAllOwnCategories();

    ArrayList<Product> getProductListToInsert();

    void insert(List<Product> productList);

    Resources getResources();

    void setDatabaseMode(Database databaseMode);

    int getIntValueFromObservableField(ObservableField<String> observableField);
}