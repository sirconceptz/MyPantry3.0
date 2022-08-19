package com.hermanowicz.pantry.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.databinding.FragmentOwnCategoryDetailBinding;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.own_category_detail.OwnCategoryDetailViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OwnCategoryDetailFragment extends Fragment {

    private FragmentOwnCategoryDetailBinding binding;
    private DatabaseModeViewModel databaseModeViewModel;
    private OwnCategoryDetailViewModel ownCategoryDetailViewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        setObservers();
        setListeners();
        getCategoryAndShow();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        ownCategoryDetailViewModel = new ViewModelProvider(this).get(OwnCategoryDetailViewModel.class);
        binding = FragmentOwnCategoryDetailBinding.inflate(inflater, container, false);
        binding.setViewModel(ownCategoryDetailViewModel);
        view = binding.getRoot();
    }

    private void setObservers() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                ownCategoryDetailViewModel::setDatabaseMode);
    }

    private void setListeners() {
        binding.buttonUpdateCategory.setOnClickListener(this::onClickUpdateCategory);
        binding.buttonDeleteCategory.setOnClickListener(this::onClickDeleteCategory);
    }

    private void onClickUpdateCategory(View view) {
        ownCategoryDetailViewModel.onClickUpdateCategory();
        navigateToOwnCategories(view);
    }

    private void onClickDeleteCategory(View view) {
        ownCategoryDetailViewModel.onClickDeleteCategory();
        navigateToOwnCategories(view);
    }

    private void navigateToOwnCategories(View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_own_category_detail_to_nav_own_categories);
    }

    private void getCategoryAndShow() {
        Category category = getCategoryData();
        ownCategoryDetailViewModel.showCategoryData(category);
    }

    private Category getCategoryData() {
        return OwnCategoryDetailFragmentArgs.fromBundle(getArguments()).getCategory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}