package com.hermanowicz.pantry.ui.filter_product;

import androidx.lifecycle.LiveData;

public interface FilterProductUseCase {
    LiveData<String[]> getAllStorageLocationsNames();

    LiveData<String[]> getAllOwnCategoriesNames();
}
