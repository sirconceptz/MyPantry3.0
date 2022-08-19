package com.hermanowicz.pantry.ui.edit_product;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.Database;

import java.util.List;

public interface EditProductUseCase {
    void updateProductList(List<Product> products);

    LiveData<String[]> getAllStorageLocationsNames();

    LiveData<String[]> getAllOwnCategoriesNames();

    void setDatabaseMode(Database databaseMode);

    int getIntValueFromObservableField(ObservableField<String> observableField);
}