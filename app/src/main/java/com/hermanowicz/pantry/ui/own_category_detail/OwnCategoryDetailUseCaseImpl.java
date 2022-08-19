package com.hermanowicz.pantry.ui.own_category_detail;

import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.model.Database;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;

import javax.inject.Inject;

public class OwnCategoryDetailUseCaseImpl implements OwnCategoryDetailUseCase {

    private final OwnCategoryRepository repository;
    private Database databaseMode;

    @Inject
    public OwnCategoryDetailUseCaseImpl(OwnCategoryRepository ownCategoryRepository) {
        repository = ownCategoryRepository;
    }

    @Override
    public void deleteCategory(Category category) {
        repository.delete(category, databaseMode);
    }

    @Override
    public void updateCategory(Category category) {
        repository.update(category, databaseMode);
    }

    public void setDatabaseMode(Database databaseMode) {
        this.databaseMode = databaseMode;
    }
}