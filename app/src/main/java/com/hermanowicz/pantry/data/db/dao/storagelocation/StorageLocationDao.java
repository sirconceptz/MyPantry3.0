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

package com.hermanowicz.pantry.data.db.dao.storagelocation;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * <h1>StorageLocationDao</h1>
 * Storage location dao needed to support database.
 *
 * @author Mateusz Hermanowicz
 */

@Dao
public interface StorageLocationDao {
    @Query("SELECT * FROM storage_locations WHERE id = (:id)")
    StorageLocation getStorageLocation(int id);

    @Query("SELECT * FROM storage_locations")
    LiveData<List<StorageLocation>> getAllStorageLocations();

    @Query("SELECT name FROM storage_locations")
    LiveData<String[]> getAllStorageLocationsArray();

    @Insert
    void insert(StorageLocation... storageLocations);

    @Delete
    void delete(StorageLocation... storageLocations);

    @Update
    void update(StorageLocation... storageLocations);

    @Query("DELETE FROM storage_locations")
    void deleteAll();

    @Query("SELECT * FROM storage_locations")
    List<StorageLocation> getAllStorageLocationsAsList();
}