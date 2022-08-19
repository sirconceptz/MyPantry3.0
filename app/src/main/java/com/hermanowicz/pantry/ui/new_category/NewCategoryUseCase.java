package com.hermanowicz.pantry.ui.new_category;

import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.model.Database;

public interface NewCategoryUseCase {
    void insert(Category category);

    void setDatabaseMode(Database databaseMode);
}