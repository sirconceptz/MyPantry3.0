package com.hermanowicz.pantry.ui.own_category_detail;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.data.db.dao.category.Category;
import com.hermanowicz.pantry.model.DatabaseMode;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class OwnCategoryDetailViewModel extends ViewModel {

    private final OwnCategoryDetailUseCaseImpl useCase;
    public ObservableField<String> categoryName = new ObservableField<>();
    public ObservableField<String> categoryDescription = new ObservableField<>();
    private Category category = new Category();

    @Inject
    public OwnCategoryDetailViewModel(OwnCategoryDetailUseCaseImpl ownCategoryDetailUseCase) {
        useCase = ownCategoryDetailUseCase;
    }

    public void showCategoryData(Category category) {
        this.category = category;
        categoryName.set(category.getName());
        categoryDescription.set(category.getDescription());
    }

    private Category getUpdatedCategory() {
        category.setName(categoryName.get());
        category.setDescription(categoryDescription.get());
        return category;
    }

    public void onClickUpdateCategory() {
        Category category = getUpdatedCategory();
        useCase.updateCategory(category);
    }

    public void onClickDeleteCategory() {
        useCase.deleteCategory(category);
    }

    public void setDatabaseMode(DatabaseMode databaseMode) {
        useCase.setDatabaseMode(databaseMode);
    }
}