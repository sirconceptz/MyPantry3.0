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

package com.hermanowicz.pantry.data.repository;

import android.graphics.Bitmap;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.data.db.dao.photo.Photo;
import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.GroupProduct;

import java.util.ArrayList;
import java.util.List;

public interface ProductRepository {
    LiveData<List<Product>> getAllProducts(DatabaseMode databaseMode);

    ArrayList<Product> getProductListToInsert();

    void insert(List<Product> productList, DatabaseMode databaseMode);

    void update(List<Product> productList, DatabaseMode databaseMode);

    void delete(List<Product> productList, DatabaseMode databaseMode);

    void deleteAll(DatabaseMode databaseMode);

    void setOnlineProductList(MutableLiveData<List<Product>> listMutableLiveData);

    int getIntValueFromObservableField(ObservableField<String> observableField);

    boolean checkIsInternetConnection();

    List<Product> getAllLocalProductsAsList();

    void addOfflinePhoto(String photoDescription, ArrayList<Product> productArrayList, String fileName);

    void addOnlinePhoto(Bitmap bitmap, String photoDescription, List<Product> productList, List<Photo> currentPhotoList);

    void deleteOfflinePhoto(ArrayList<Product> productArrayList);

    void deleteOnlinePhoto(ArrayList<Product> productArrayList);

    String[] getGroupProductNames(ArrayList<Product> productArrayList);

    List<GroupProduct> getGroupProductListFromProductList(ArrayList<Product> productArrayList);

    List<GroupProduct> getGroupProductList();
}