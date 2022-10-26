package com.hermanowicz.pantry.ui.new_category;

import com.hermanowicz.pantry.data.db.dao.category.Category;
import com.hermanowicz.pantry.data.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.domain.usecase.NewCategoryUseCase;
import com.hermanowicz.pantry.model.DatabaseMode;

import javax.inject.Inject;

public class NewCategoryUseCaseImpl implements NewCategoryUseCase {

    private final OwnCategoryRepository repository;
    private DatabaseMode databaseMode;

    @Inject
    public NewCategoryUseCaseImpl(OwnCategoryRepository ownCategoryRepository) {
        repository = ownCategoryRepository;
    }

    @Override
    public void insert(Category category) {
        repository.insert(category, databaseMode);
    }

    @Override
    public void setDatabaseMode(DatabaseMode databaseMode) {
        this.databaseMode = databaseMode;
    }
}