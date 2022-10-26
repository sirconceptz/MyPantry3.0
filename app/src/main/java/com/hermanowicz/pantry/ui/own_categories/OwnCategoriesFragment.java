/*
 * Copyright (c) 2019-2022
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.ui.own_categories;

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
import com.hermanowicz.pantry.data.db.dao.category.Category;
import com.hermanowicz.pantry.databinding.FragmentOwnCategoriesBinding;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.util.OwnCategoriesAdapter;
import com.hermanowicz.pantry.util.RecyclerClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OwnCategoriesFragment extends Fragment implements AvailableDataListener {

    private final OwnCategoriesAdapter ownCategoriesAdapter = new OwnCategoriesAdapter();
    private FragmentOwnCategoriesBinding binding;
    private OwnCategoriesViewModel ownCategoriesViewModel;
    private DatabaseModeViewModel databaseModeViewModel;
    private View view;

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
        ownCategoriesViewModel = new ViewModelProvider(this).get(OwnCategoriesViewModel.class);
        ownCategoriesViewModel.setAvailableDataListener(this);
        ownCategoriesViewModel.setDefaultDatabaseMode(databaseModeViewModel.getDatabaseModeFromSettings());

        binding = FragmentOwnCategoriesBinding.inflate(inflater, container, false);
        binding.setViewModel(ownCategoriesViewModel);
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
                Category category = ownCategoriesViewModel.getCategory(position);
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
        if (ownCategoriesViewModel.getCategoryList() != null)
            categoryArrayList = new ArrayList<>(Objects.requireNonNull(ownCategoriesViewModel.getCategoryList().getValue()));
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
                ownCategoriesViewModel::updateDataForSelectedDatabase);
        ownCategoriesViewModel.getCategoryList().observe(getViewLifecycleOwner(),
                this::setDataRecyclerViewAdapter);
    }

    @Override
    public void onStart() {
        if (ownCategoriesViewModel.isAvailableData())
            observeAvailableData();
        super.onStart();
    }

    @Override
    public void onPause() {
        ownCategoriesViewModel.clearDisposable();
        super.onPause();
    }
}