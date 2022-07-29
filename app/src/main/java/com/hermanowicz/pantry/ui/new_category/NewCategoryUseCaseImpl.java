package com.hermanowicz.pantry.ui.new_category;

import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;

import javax.inject.Inject;

public class NewCategoryUseCaseImpl implements NewCategoryUseCase {

    private final OwnCategoryRepository repository;

    @Inject
    public NewCategoryUseCaseImpl(OwnCategoryRepository ownCategoryRepository){
        repository = ownCategoryRepository;
    }

    @Override
    public void insert(Category category){
        repository.insert(category);
    }
}