/*
 * Copyright (c) 2019-2022
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.domain.usecase;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.data.db.dao.product.Product;
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

    void setProductionDate(int year, int month, int day);

    void clearExpirationDate();

    void clearProductionDate();

    String getExpirationDate();

    void setExpirationDate(String expirationDate);

    String getProductionDate();

    void setProductionDate(String productionDate);

    int[] getExpirationDateArray();

    int[] getProductionDateArray();

    void setProductArrayList(ArrayList<Product> productArrayList);

    List<Product> getAllProductList();

    int getDetailCategorySpinnerPosition(int productMainCategorySpinnerPosition);

    int getProductMainCategorySpinnerPosition();

    int getProductStorageLocationPosition();
}