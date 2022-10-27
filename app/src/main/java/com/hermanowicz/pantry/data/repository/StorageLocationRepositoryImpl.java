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

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.hermanowicz.pantry.data.db.dao.storagelocation.StorageLocation;
import com.hermanowicz.pantry.data.db.dao.storagelocation.StorageLocationDao;
import com.hermanowicz.pantry.data.db.dao.storagelocation.StorageLocationDb;
import com.hermanowicz.pantry.domain.repository.StorageLocationRepository;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.util.InternetConnection;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StorageLocationRepositoryImpl implements StorageLocationRepository {

    private final StorageLocationDao storageLocationDao;
    private final LiveData<List<StorageLocation>> localStorageLocationList;
    private final LiveData<String[]> storageLocationArray;
    private final InternetConnection internetConnection;
    private LiveData<List<StorageLocation>> onlineStorageLocationList;

    public StorageLocationRepositoryImpl(Context context) {
        StorageLocationDb storageLocationDb = StorageLocationDb.getInstance(context);
        storageLocationDao = storageLocationDb.storageLocationDao();
        localStorageLocationList = storageLocationDao.getAllStorageLocations();
        storageLocationArray = storageLocationDao.getAllStorageLocationsArray();
        internetConnection = new InternetConnection(context);
    }

    @Override
    public LiveData<List<StorageLocation>> getAllStorageLocations(DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            return onlineStorageLocationList;
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            return localStorageLocationList;
        return new MutableLiveData<>();
    }

    @Override
    public LiveData<String[]> getAllStorageLocationsNames() {
        return storageLocationArray;
    }

    @Override
    public void insert(StorageLocation storageLocation, DatabaseMode databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> insertToSelectedDatabase(storageLocation, databaseMode));
    }

    private void insertToSelectedDatabase(StorageLocation storageLocation, DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            insertLocalStorageLocation(storageLocation);
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            insertOnlineStorageLocation(storageLocation);
    }

    private void insertLocalStorageLocation(StorageLocation storageLocation) {
        storageLocationDao.insert(storageLocation);
    }

    private void insertOnlineStorageLocation(StorageLocation storageLocation) {
        DatabaseReference dbRef = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTableStorageLocations);
        dbRef.child(String.valueOf(storageLocation.getId())).setValue(storageLocation);
    }

    @Override
    public void update(StorageLocation storageLocation, DatabaseMode databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> updateToSelectedDatabase(storageLocation, databaseMode));
    }

    private void updateToSelectedDatabase(StorageLocation storageLocation, DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            updateLocalStorageLocation(storageLocation);
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            insertOnlineStorageLocation(storageLocation);
    }

    private void updateLocalStorageLocation(StorageLocation storageLocation) {
        storageLocationDao.insert(storageLocation);
    }

    @Override
    public void delete(StorageLocation storageLocation, DatabaseMode databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteToSelectedDatabase(storageLocation, databaseMode));
    }

    private void deleteToSelectedDatabase(StorageLocation storageLocation, DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            deleteLocalStorageLocation(storageLocation);
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            deleteOnlineStorageLocation(storageLocation);
    }

    private void deleteLocalStorageLocation(StorageLocation storageLocation) {
        storageLocationDao.delete(storageLocation);
    }

    private void deleteOnlineStorageLocation(StorageLocation storageLocation) {
        DatabaseReference dbRef = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTableStorageLocations);
        dbRef.child(String.valueOf(storageLocation.getId())).removeValue();
    }

    @Override
    public void deleteAll(DatabaseMode databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteAllToSelectedDatabase(databaseMode));
    }

    private void deleteAllToSelectedDatabase(DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            deleteAllLocalStorageLocations();
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            deleteAllOnlineStorageLocations();
    }

    private void deleteAllLocalStorageLocations() {
        storageLocationDao.deleteAll();
    }

    private void deleteAllOnlineStorageLocations() {
        DatabaseReference dbRef = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTableStorageLocations);
        dbRef.removeValue();
    }

    @Override
    public void setOnlineStorageLocationList(MutableLiveData<List<StorageLocation>> onlineStorageLocationList) {
        this.onlineStorageLocationList = onlineStorageLocationList;
    }

    @Override
    public boolean checkIsInternetConnection() {
        return internetConnection.isNetworkConnected();
    }

    @Override
    public List<StorageLocation> getAllLocalStorageLocationsAsList() {
        return storageLocationDao.getAllStorageLocationsAsList();
    }
}