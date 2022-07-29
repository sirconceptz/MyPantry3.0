package com.hermanowicz.pantry.ui.category;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;

import java.util.List;

import javax.inject.Inject;

public class CategoryUseCaseImpl implements CategoryUseCase {

    private final OwnCategoryRepository repository;

    @Inject
    public CategoryUseCaseImpl(OwnCategoryRepository ownCategoryRepository){
        this.repository = ownCategoryRepository;
    }

    @Override
    public LiveData<List<Category>> getAllCategories() {
        return repository.getAllCategories();
    }
}