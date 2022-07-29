package com.hermanowicz.pantry.ui.category;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.category.Category;

import java.util.List;

public interface CategoryUseCase {
    LiveData<List<Category>> getAllCategories();
}
