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

import android.content.res.Resources;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.data.db.dao.product.Product;
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