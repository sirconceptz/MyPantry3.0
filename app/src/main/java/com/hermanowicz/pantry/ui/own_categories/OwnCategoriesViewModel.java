package com.hermanowicz.pantry.ui.own_categories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.dao.db.category.Category;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class OwnCategoriesViewModel extends ViewModel {

    @Inject
    OwnCategoriesUseCaseImpl useCase;
    private final LiveData<List<Category>> categoryList;

    @Inject
    public OwnCategoriesViewModel(OwnCategoriesUseCaseImpl ownCategoriesUseCase) {
        useCase = ownCategoriesUseCase;
        categoryList = useCase.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryList;
    }

    public Category getCategory(int id) {
        List<Category> categories = categoryList.getValue();
        assert categories != null;
        return categories.get(id);
    }
}