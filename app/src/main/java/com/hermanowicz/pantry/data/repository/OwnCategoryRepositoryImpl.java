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
import com.hermanowicz.pantry.data.db.dao.category.Category;
import com.hermanowicz.pantry.data.db.dao.category.CategoryDao;
import com.hermanowicz.pantry.data.db.dao.category.CategoryDb;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.util.InternetConnection;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OwnCategoryRepositoryImpl implements OwnCategoryRepository {

    private final CategoryDao categoryDao;
    private final LiveData<List<Category>> localCategoryList;
    private final LiveData<String[]> categoryArray;
    private final InternetConnection internetConnection;
    private LiveData<List<Category>> onlineCategoryList;

    public OwnCategoryRepositoryImpl(Context context) {
        CategoryDb categoryDb = CategoryDb.getInstance(context);
        categoryDao = categoryDb.categoryDao();
        localCategoryList = categoryDao.getOwnCategoriesLiveData();
        categoryArray = categoryDao.getOwnCategoriesNames();
        internetConnection = new InternetConnection(context);
    }

    @Override
    public LiveData<List<Category>> getAllCategories(DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            return onlineCategoryList;
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            return localCategoryList;
        else
            return new MutableLiveData<>();
    }

    @Override
    public LiveData<String[]> getOwnCategoriesNames() {
        return categoryArray;
    }

    @Override
    public void insert(Category category, DatabaseMode databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> insertToSelectedDatabase(category, databaseMode));
    }

    private void insertToSelectedDatabase(Category category, DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            insertLocalCategory(category);
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            insertOnlineCategory(category);
    }

    private void insertLocalCategory(Category category) {
        categoryDao.insert(category);
    }

    private void insertOnlineCategory(Category category) {
        DatabaseReference dbRef = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTableCategories);
        dbRef.child(String.valueOf(category.getId())).setValue(category);
    }

    @Override
    public void update(Category category, DatabaseMode databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> updateToSelectedDatabase(category, databaseMode));
    }

    private void updateToSelectedDatabase(Category category, DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            updateLocalCategory(category);
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            insertOnlineCategory(category);
    }

    private void updateLocalCategory(Category category) {
        categoryDao.update(category);
    }

    @Override
    public void delete(Category category, DatabaseMode databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteToSelectedDatabase(category, databaseMode));
    }

    private void deleteToSelectedDatabase(Category category, DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            deleteLocalCategory(category);
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            deleteOnlineCategory(category);
    }

    private void deleteLocalCategory(Category category) {
        categoryDao.delete(category);
    }

    private void deleteOnlineCategory(Category category) {
        DatabaseReference dbRef = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTableCategories);
        dbRef.child(String.valueOf(category.getId())).removeValue();
    }

    @Override
    public void deleteAll(DatabaseMode databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteAllToSelectedDatabase(databaseMode));
    }

    private void deleteAllToSelectedDatabase(DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            deleteAllLocalCategories();
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            deleteAllOnlineCategories();
    }

    private void deleteAllLocalCategories() {
        categoryDao.deleteAll();
    }

    private void deleteAllOnlineCategories() {
        DatabaseReference dbRef = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTableCategories);
        dbRef.removeValue();
    }

    @Override
    public void setOnlineCategoryList(MutableLiveData<List<Category>> onlineCategoryList) {
        this.onlineCategoryList = onlineCategoryList;
    }

    @Override
    public boolean checkIsInternetConnection() {
        return internetConnection.isNetworkConnected();
    }

    @Override
    public List<Category> getAllLocalCategoriesAsList() {
        return categoryDao.getOwnCategoriesAsList();
    }

    @Override
    public String[] getAllCategoriesNameAsList(DatabaseMode databaseMode) {
        LiveData<List<Category>> categoryLiveDataList = getAllCategories(databaseMode);
        List<Category> categoryList = categoryLiveDataList.getValue();
        String[] allCategoriesNames = new String[0];
        if (categoryList != null) {
            allCategoriesNames = new String[categoryList.size()];
            for (int counter = 0; counter <= categoryList.size(); counter++) {
                allCategoriesNames[counter] = categoryList.get(counter).getName();
            }
        }
        return allCategoriesNames;
    }
}