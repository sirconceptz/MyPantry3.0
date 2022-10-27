package com.hermanowicz.pantry.ui.own_categories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.data.db.dao.category.Category;
import com.hermanowicz.pantry.domain.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.domain.usecase.CategoryUseCase;
import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.List;

import javax.inject.Inject;

public class OwnCategoriesUseCaseImpl implements CategoryUseCase {

    private final OwnCategoryRepository repository;

    @Inject
    public OwnCategoriesUseCaseImpl(OwnCategoryRepository ownCategoryRepository) {
        this.repository = ownCategoryRepository;
    }

    @Override
    public LiveData<List<Category>> getAllCategories(DatabaseMode databaseMode) {
        return repository.getAllCategories(databaseMode);
    }

    @Override
    public void setOnlineCategoryList(MutableLiveData<List<Category>> onlineCategoryList) {
        repository.setOnlineCategoryList(onlineCategoryList);
    }

    @Override
    public boolean checkIsInternetConnection() {
        return repository.checkIsInternetConnection();
    }
}