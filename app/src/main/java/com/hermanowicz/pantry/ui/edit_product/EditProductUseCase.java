package com.hermanowicz.pantry.ui.edit_product;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.ArrayList;
import java.util.List;

public interface EditProductUseCase {
    void updateProductList(List<Product> products);

    LiveData<String[]> getAllStorageLocationsNames();

    LiveData<String[]> getAllOwnCategoriesNames();

    void setDatabaseMode(DatabaseMode databaseMode);

    int getIntValueFromObservableField(ObservableField<String> observableField);

    void setExpirationDate(int year, int month, int day);

    void setExpirationDate(String expirationDate);

    void setProductionDate(int year, int month, int day);

    void setProductionDate(String productionDate);

    void clearExpirationDate();

    void clearProductionDate();

    String getExpirationDate();

    String getProductionDate();

    int[] getExpirationDateArray();

    int[] getProductionDateArray();

    void setProductArrayList(ArrayList<Product> productArrayList);

    List<Product> getAllProductList();

    int getDetailCategorySpinnerPosition(int productMainCategorySpinnerPosition);

    int getProductMainCategorySpinnerPosition();

    int getProductStorageLocationPosition();
}