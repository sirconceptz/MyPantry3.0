/*
 * Copyright (c) 2019-2021
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.dao.db.category;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * <h1>CategoryDao</h1>
 * Categories dao needed to support database.
 *
 * @author Mateusz Hermanowicz
 */

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories WHERE id = (:id)")
    Category get(int id);

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getOwnCategoriesLiveData();

    @Query("SELECT name FROM categories")
    LiveData<String[]> getOwnCategoriesNames();

    @Insert
    void insert(Category... categories);

    @Delete
    void delete(Category... categories);

    @Update
    void update(Category... categories);

    @Query("DELETE FROM categories")
    void deleteAll();

}