package com.hermanowicz.pantry.ui.filter_product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.Filter;
import com.hermanowicz.pantry.model.FilterModel;

import java.util.List;

public interface FilterProductUseCase {

    LiveData<String[]> getAllStorageLocationsNames();

    LiveData<String[]> getAllOwnCategoriesNames();

    void setExpirationDateSince(int year, int month, int day);

    void setExpirationDateFor(int year, int month, int day);

    void setProductionDateSince(int year, int month, int day);

    void setProductionDateFor(int year, int month, int day);

    void clearExpirationDateSince();

    void clearExpirationDateFor();

    void clearProductionDateSince();

    void clearProductionDateFor();

    String getExpirationDateSince();

    String getExpirationDateFor();

    String getProductionDateSince();

    String getProductionDateFor();
}
