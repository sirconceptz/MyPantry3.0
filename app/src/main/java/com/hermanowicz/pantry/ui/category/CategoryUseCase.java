package com.hermanowicz.pantry.ui.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.List;

public interface CategoryUseCase {
    LiveData<List<Category>> getAllCategories(DatabaseMode databaseMode);

    void setOnlineCategoryList(MutableLiveData<List<Category>> onlineCategoryList);

    boolean checkIsInternetConnection();
}
