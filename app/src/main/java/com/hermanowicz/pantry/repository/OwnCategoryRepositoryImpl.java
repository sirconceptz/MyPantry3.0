package com.hermanowicz.pantry.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.dao.db.category.CategoryDao;
import com.hermanowicz.pantry.dao.db.category.CategoryDb;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OwnCategoryRepositoryImpl implements OwnCategoryRepository {

    private final CategoryDao categoryDao;
    private final LiveData<List<Category>> categoryList;
    private final LiveData<String[]> categoryArray;

    public OwnCategoryRepositoryImpl(Context context){
        CategoryDb categoryDb = CategoryDb.getInstance(context);
        categoryDao = categoryDb.categoryDao();
        categoryList = categoryDao.getOwnCategoriesLiveData();
        categoryArray = categoryDao.getOwnCategoriesNames();
    }

    @Override
    public LiveData<List<Category>> getAllCategories(){
        return categoryList;
    }

    @Override
    public LiveData<String[]> getOwnCategoriesNames() {
        return categoryArray;
    }

    @Override
    public void insert(Category category){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> categoryDao.insert(category));
    }

    @Override
    public void update(Category category){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> categoryDao.update(category));
    }

    @Override
    public void delete(Category category){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> categoryDao.delete(category));
    }

    @Override
    public void deleteAll(){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(categoryDao::deleteAll);
    }
}