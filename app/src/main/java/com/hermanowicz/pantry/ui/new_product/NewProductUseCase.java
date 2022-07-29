package com.hermanowicz.pantry.ui.new_product;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.model.GroupProduct;

public interface NewProductUseCase {
    void insert(GroupProduct groupProduct);
    LiveData<String[]> getAllStorageLocations();
    LiveData<String[]> getAllOwnCategories();
    Resources getResources();
}