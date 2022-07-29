package com.hermanowicz.pantry.ui.new_category;

import com.hermanowicz.pantry.dao.db.category.Category;

public interface NewCategoryUseCase {
    void insert(Category category);
}