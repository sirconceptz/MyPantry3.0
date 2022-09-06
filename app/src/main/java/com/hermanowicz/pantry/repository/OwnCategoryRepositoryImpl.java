package com.hermanowicz.pantry.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.dao.db.category.CategoryDao;
import com.hermanowicz.pantry.dao.db.category.CategoryDb;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.util.InternetConnection;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OwnCategoryRepositoryImpl implements OwnCategoryRepository {

    private final CategoryDao categoryDao;
    private final LiveData<List<Category>> localCategoryList;
    private LiveData<List<Category>> onlineCategoryList;
    private final LiveData<String[]> categoryArray;
    private final InternetConnection internetConnection;

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
        DatabaseReference dbRef = DatabaseMode.getOnlineDatabaseReference("categories");
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
        DatabaseReference dbRef = DatabaseMode.getOnlineDatabaseReference("categories");
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
        DatabaseReference dbRef = DatabaseMode.getOnlineDatabaseReference("categories");
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
}