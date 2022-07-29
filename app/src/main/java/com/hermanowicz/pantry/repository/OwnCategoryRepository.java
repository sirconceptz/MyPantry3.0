package com.hermanowicz.pantry.repository;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.category.Category;

import java.util.List;

public interface OwnCategoryRepository {
    LiveData<List<Category>> getAllCategories();
    LiveData<String[]> getOwnCategoriesNames();
    void insert(Category category);
    void update(Category category);
    void delete(Category category);
    void deleteAll();
}