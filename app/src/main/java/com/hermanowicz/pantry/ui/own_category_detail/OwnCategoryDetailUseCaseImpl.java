package com.hermanowicz.pantry.ui.own_category_detail;

import com.hermanowicz.pantry.data.db.dao.category.Category;
import com.hermanowicz.pantry.domain.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.domain.usecase.OwnCategoryDetailUseCase;
import com.hermanowicz.pantry.model.DatabaseMode;

import javax.inject.Inject;

public class OwnCategoryDetailUseCaseImpl implements OwnCategoryDetailUseCase {

    private final OwnCategoryRepository repository;
    private DatabaseMode databaseMode;

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

    public void setDatabaseMode(DatabaseMode databaseMode) {
        this.databaseMode = databaseMode;
    }
}