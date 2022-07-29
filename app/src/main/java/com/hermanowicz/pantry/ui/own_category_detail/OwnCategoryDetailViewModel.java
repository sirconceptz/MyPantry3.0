package com.hermanowicz.pantry.ui.own_category_detail;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.dao.db.category.Category;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class OwnCategoryDetailViewModel extends ViewModel {

    private OwnCategoryDetailUseCaseImpl useCase;

    private Category category = new Category();
    public ObservableField<String> categoryName = new ObservableField<>();
    public ObservableField<String> categoryDescription = new ObservableField<>();

    @Inject
    public OwnCategoryDetailViewModel(OwnCategoryDetailUseCaseImpl ownCategoryDetailUseCase) {
        useCase = ownCategoryDetailUseCase;
    }

    public void showCategoryData(Category category) {
        this.category = category;
        categoryName.set(category.getName());
        categoryDescription.set(category.getDescription());
    }

    private Category getUpdatedCategory(){
        category.setName(categoryName.get());
        category.setDescription(categoryDescription.get());
        return category;
    }

    public void onClickUpdateCategory(){
        Category category = getUpdatedCategory();
        useCase.updateCategory(category);
    }

    public void onClickDeleteCategory(){
        useCase.deleteCategory(category);
    }
}