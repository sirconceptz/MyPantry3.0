package com.hermanowicz.pantry.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocationDao;
import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocationDb;
import com.hermanowicz.pantry.model.Database;
import com.hermanowicz.pantry.util.InternetConnection;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StorageLocationRepositoryImpl implements StorageLocationRepository {

    private final StorageLocationDao storageLocationDao;
    private final LiveData<List<StorageLocation>> localStorageLocationList;
    private LiveData<List<StorageLocation>> onlineStorageLocationList;
    private final LiveData<String[]> storageLocationArray;
    private final InternetConnection internetConnection;

    public StorageLocationRepositoryImpl(Context context) {
        StorageLocationDb storageLocationDb = StorageLocationDb.getInstance(context);
        storageLocationDao = storageLocationDb.storageLocationDao();
        localStorageLocationList = storageLocationDao.getAllStorageLocations();
        storageLocationArray = storageLocationDao.getAllStorageLocationsArray();
        internetConnection = new InternetConnection(context);
    }

    @Override
    public LiveData<List<StorageLocation>> getAllStorageLocations(Database databaseMode) {
        if (databaseMode.getDatabaseMode() == Database.DatabaseMode.ONLINE)
            return onlineStorageLocationList;
        else if (databaseMode.getDatabaseMode() == Database.DatabaseMode.LOCAL)
            return localStorageLocationList;
        return new MutableLiveData<>();
    }

    @Override
    public LiveData<String[]> getAllStorageLocationsNames() {
        return storageLocationArray;
    }

    @Override
    public void insert(StorageLocation storageLocation, Database databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> insertToSelectedDatabase(storageLocation, databaseMode));
    }

    private void insertToSelectedDatabase(StorageLocation storageLocation, Database databaseMode) {
        if (databaseMode.getDatabaseMode() == Database.DatabaseMode.LOCAL)
            insertLocalStorageLocation(storageLocation);
        else if (databaseMode.getDatabaseMode() == Database.DatabaseMode.ONLINE)
            insertOnlineStorageLocation(storageLocation);
    }

    private void insertLocalStorageLocation(StorageLocation storageLocation) {
        storageLocationDao.insert(storageLocation);
    }

    private void insertOnlineStorageLocation(StorageLocation storageLocation) {
        DatabaseReference dbRef = Database.getOnlineDatabaseReference("storage_locations");
        dbRef.child(String.valueOf(storageLocation.getId())).setValue(storageLocation);
    }

    @Override
    public void update(StorageLocation storageLocation, Database databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> updateToSelectedDatabase(storageLocation, databaseMode));
    }

    private void updateToSelectedDatabase(StorageLocation storageLocation, Database databaseMode) {
        if (databaseMode.getDatabaseMode() == Database.DatabaseMode.LOCAL)
            updateLocalStorageLocation(storageLocation);
        else if (databaseMode.getDatabaseMode() == Database.DatabaseMode.ONLINE)
            insertOnlineStorageLocation(storageLocation);
    }

    private void updateLocalStorageLocation(StorageLocation storageLocation) {
        storageLocationDao.insert(storageLocation);
    }

    @Override
    public void delete(StorageLocation storageLocation, Database databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteToSelectedDatabase(storageLocation, databaseMode));
    }

    private void deleteToSelectedDatabase(StorageLocation storageLocation, Database databaseMode) {
        if (databaseMode.getDatabaseMode() == Database.DatabaseMode.LOCAL)
            deleteLocalStorageLocation(storageLocation);
        else if (databaseMode.getDatabaseMode() == Database.DatabaseMode.ONLINE)
            deleteOnlineStorageLocation(storageLocation);
    }

    private void deleteLocalStorageLocation(StorageLocation storageLocation) {
        storageLocationDao.delete(storageLocation);
    }

    private void deleteOnlineStorageLocation(StorageLocation storageLocation) {
        DatabaseReference dbRef = Database.getOnlineDatabaseReference("storage_locations");
        dbRef.child(String.valueOf(storageLocation.getId())).removeValue();
    }

    @Override
    public void deleteAll(Database databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteAllToSelectedDatabase(databaseMode));
    }

    private void deleteAllToSelectedDatabase(Database databaseMode) {
        if (databaseMode.getDatabaseMode() == Database.DatabaseMode.LOCAL)
            deleteAllLocalStorageLocations();
        else if (databaseMode.getDatabaseMode() == Database.DatabaseMode.ONLINE)
            deleteAllOnlineStorageLocations();
    }

    private void deleteAllLocalStorageLocations() {
        storageLocationDao.deleteAll();
    }

    private void deleteAllOnlineStorageLocations() {
        DatabaseReference dbRef = Database.getOnlineDatabaseReference("storage_locations");
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
}