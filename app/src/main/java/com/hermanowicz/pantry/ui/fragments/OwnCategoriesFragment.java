package com.hermanowicz.pantry.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.databinding.FragmentOwnCategoriesBinding;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.ui.category.CategoryViewModel;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.util.OwnCategoriesAdapter;
import com.hermanowicz.pantry.util.RecyclerClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OwnCategoriesFragment extends Fragment implements AvailableDataListener {

    private FragmentOwnCategoriesBinding binding;
    private CategoryViewModel categoryViewModel;
    private DatabaseModeViewModel databaseModeViewModel;
    private View view;
    private final OwnCategoriesAdapter ownCategoriesAdapter = new OwnCategoriesAdapter();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        setupFloatingActionButton();
        setupRecyclerView();
        setListeners();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.setAvailableDataListener(this);
        categoryViewModel.setDefaultDatabaseMode(databaseModeViewModel.getDatabaseModeFromSettings());

        binding = FragmentOwnCategoriesBinding.inflate(inflater, container, false);
        binding.setViewModel(categoryViewModel);
        view = binding.getRoot();
    }

    private void setupFloatingActionButton() {
        binding.newCategoryFab.setColorFilter(Color.WHITE);
        binding.newCategoryFab.setOnClickListener(v -> onClickNewCategory());
    }

    private void setupRecyclerView() {
        binding.recyclerviewCategories.setAdapter(ownCategoriesAdapter);
        binding.recyclerviewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewCategories.setHasFixedSize(true);
        binding.recyclerviewCategories.setItemAnimator(new DefaultItemAnimator());
    }

    private void setListeners() {
        binding.recyclerviewCategories.addOnItemTouchListener(new RecyclerClickListener(getContext(),
                binding.recyclerviewCategories, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Category category = categoryViewModel.getCategory(position);
                navigateToCategoryDetails(view, category);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void setDataRecyclerViewAdapter(List<Category> categories) {
        ownCategoriesAdapter.setData(categories);
    }

    private void navigateToCategoryDetails(View view, Category category) {
        NavDirections action =
                OwnCategoriesFragmentDirections.actionNavOwnCategoriesToNavOwnCategoryDetail(category);
        Navigation.findNavController(view).navigate(action);
    }

    private void onClickNewCategory() {
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        if(categoryViewModel.getCategoryList() != null)
                categoryArrayList = new ArrayList<>(Objects.requireNonNull(categoryViewModel.getCategoryList().getValue()));
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("categoryArrayList", categoryArrayList);
        Navigation.findNavController(view)
                .navigate(R.id.action_nav_own_categories_to_nav_new_category, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void observeAvailableData() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                categoryViewModel::updateDataForSelectedDatabase);
        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(),
                this::setDataRecyclerViewAdapter);
    }

    @Override
    public void onStart(){
        if(categoryViewModel.isAvailableData())
            observeAvailableData();
        super.onStart();
    }

    @Override
    public void onPause() {
        categoryViewModel.clearDisposable();
        super.onPause();
    }
}