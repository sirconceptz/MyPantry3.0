package com.hermanowicz.pantry.ui.filter_product;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;

public interface FilterProductUseCase {
    void update(Product[] products, DatabaseMode databaseMode);
    LiveData<String[]> getAllStorageLocationsNames();
    LiveData<String[]> getAllOwnCategoriesNames();
}
