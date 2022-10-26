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

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.data.db.dao.category.Category;
import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.List;

public interface OwnCategoryRepository {
    LiveData<List<Category>> getAllCategories(DatabaseMode databaseMode);

    LiveData<String[]> getOwnCategoriesNames();

    void insert(Category category, DatabaseMode databaseMode);

    void update(Category category, DatabaseMode databaseMode);

    void delete(Category category, DatabaseMode databaseMode);

    void deleteAll(DatabaseMode databaseMode);

    void setOnlineCategoryList(MutableLiveData<List<Category>> onlineCategoryList);

    boolean checkIsInternetConnection();

    List<Category> getAllLocalCategoriesAsList();

    String[] getAllCategoriesNameAsList(DatabaseMode databaseMode);
}