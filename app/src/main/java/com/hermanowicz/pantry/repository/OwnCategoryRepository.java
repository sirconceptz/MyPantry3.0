package com.hermanowicz.pantry.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.model.Database;

import java.util.List;

public interface OwnCategoryRepository {
    LiveData<List<Category>> getAllCategories(Database databaseMode);

    LiveData<String[]> getOwnCategoriesNames();

    void insert(Category category, Database databaseMode);

    void update(Category category, Database databaseMode);

    void delete(Category category, Database databaseMode);

    void deleteAll(Database databaseMode);

    void setOnlineCategoryList(MutableLiveData<List<Category>> onlineCategoryList);

    boolean checkIsInternetConnection();

    List<Category> getAllLocalCategoriesAsList();
}