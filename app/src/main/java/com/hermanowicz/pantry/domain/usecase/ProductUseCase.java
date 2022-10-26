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

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.FilterModel;
import com.hermanowicz.pantry.model.GroupProduct;

import java.util.List;

public interface ProductUseCase {
    LiveData<List<Product>> getAllProducts();

    List<GroupProduct> getAllGroupProductList(List<Product> productList);

    void setOnlineProductList(MutableLiveData<List<Product>> onlineProductList);

    boolean checkIsInternetConnection();

    void setFilterModel(FilterModel filterModel);

    LiveData<List<Product>> getFilteredProductList(List<Product> productList);

    void setDatabaseMode(DatabaseMode databaseMode);
}
