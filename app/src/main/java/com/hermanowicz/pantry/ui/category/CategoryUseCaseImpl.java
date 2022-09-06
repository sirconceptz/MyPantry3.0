package com.hermanowicz.pantry.ui.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;

import java.util.List;

import javax.inject.Inject;

public class CategoryUseCaseImpl implements CategoryUseCase {

    private final OwnCategoryRepository repository;

    @Inject
    public CategoryUseCaseImpl(OwnCategoryRepository ownCategoryRepository) {
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