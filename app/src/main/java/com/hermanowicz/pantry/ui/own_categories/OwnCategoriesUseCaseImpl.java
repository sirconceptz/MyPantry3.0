package com.hermanowicz.pantry.ui.own_categories;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;

import java.util.List;

import javax.inject.Inject;

public class OwnCategoriesUseCaseImpl implements OwnCategoriesUseCase{

    private final OwnCategoryRepository repository;

    @Inject
    public OwnCategoriesUseCaseImpl(OwnCategoryRepository ownCategoryRepository){
        repository = ownCategoryRepository;
    }

    @Override
    public LiveData<List<Category>> getAllCategories(){
        return repository.getAllCategories();
    }
}