package com.hermanowicz.pantry.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocationDao;
import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocationDb;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StorageLocationRepositoryImpl implements StorageLocationRepository {

    private final StorageLocationDao storageLocationDao;
    private final LiveData<List<StorageLocation>> storageLocationList;
    private final LiveData<String[]> storageLocationArray;

    public StorageLocationRepositoryImpl(Context context){
        StorageLocationDb storageLocationDb = StorageLocationDb.getInstance(context);
        storageLocationDao = storageLocationDb.storageLocationDao();
        storageLocationList = storageLocationDao.getAllStorageLocations();
        storageLocationArray = storageLocationDao.getAllStorageLocationsArray();
    }

    @Override
    public LiveData<List<StorageLocation>> getAllStorageLocations(){
        return storageLocationList;
    }

    @Override
    public LiveData<String[]> getAllStorageLocationsNames() {
        return storageLocationArray;
    }

    @Override
    public void insert(StorageLocation storageLocation){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> storageLocationDao.insert(storageLocation));
    }

    @Override
    public void update(StorageLocation storageLocation){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> storageLocationDao.update(storageLocation));
    }

    @Override
    public void delete(StorageLocation storageLocation){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> storageLocationDao.delete(storageLocation));
    }

    @Override
    public void deleteAll(){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(storageLocationDao::deleteAll);
    }
}