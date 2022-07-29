package com.hermanowicz.pantry.ui.own_categories;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.category.Category;

import java.util.List;

public interface OwnCategoriesUseCase {
    LiveData<List<Category>> getAllCategories();
}