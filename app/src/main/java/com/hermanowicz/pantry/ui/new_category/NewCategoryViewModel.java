package com.hermanowicz.pantry.ui.new_category;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.model.DatabaseMode;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NewCategoryViewModel extends ViewModel {

    @Inject
    NewCategoryUseCaseImpl useCase;

    public ObservableField<String> categoryName = new ObservableField<>();
    public ObservableField<String> categoryDescription = new ObservableField<>();

    @Inject
    public NewCategoryViewModel(NewCategoryUseCaseImpl newCategoryUseCase) {
        useCase = newCategoryUseCase;
    }

    public void onClickAddCategory(){
        Category category = getCategory();
        useCase.insert(category);
        clearFields();
    }

    @NonNull
    private Category getCategory() {
        Category category = new Category();
        category.setName(categoryName.get());
        category.setDescription(categoryDescription.get());
        return category;
    }

    public void onClickClearFields(){
        clearFields();
    }

    private void clearFields() {
        categoryName.set("");
        categoryDescription.set("");
    }

    public void setDatabaseMode(DatabaseMode databaseMode) {

    }
}