package com.hermanowicz.pantry.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.category.Category;
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