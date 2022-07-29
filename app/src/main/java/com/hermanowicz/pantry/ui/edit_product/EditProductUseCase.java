package com.hermanowicz.pantry.ui.edit_product;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.product.Product;

public interface EditProductUseCase {
    void update(Product[] products);
    LiveData<String[]> getAllStorageLocationsNames();
    LiveData<String[]> getAllOwnCategoriesNames();
}