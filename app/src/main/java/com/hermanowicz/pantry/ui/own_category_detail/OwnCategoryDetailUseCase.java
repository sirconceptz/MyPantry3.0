package com.hermanowicz.pantry.ui.own_category_detail;

import com.hermanowicz.pantry.dao.db.category.Category;

public interface OwnCategoryDetailUseCase {
    void deleteCategory(Category category);
    void updateCategory(Category category);
}
