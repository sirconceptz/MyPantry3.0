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

package com.hermanowicz.pantry.ui.new_category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.FragmentNewCategoryBinding;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewCategoryFragment extends Fragment {

    private FragmentNewCategoryBinding binding;
    private NewCategoryViewModel newCategoryViewModel;
    private DatabaseModeViewModel databaseModeViewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        initView(inflater, container);
        setObservers();
        setListeners();
        setCategoryListFromArguments();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        newCategoryViewModel = new ViewModelProvider(this).get(NewCategoryViewModel.class);

        binding = FragmentNewCategoryBinding.inflate(inflater, container, false);
        binding.setViewModel(newCategoryViewModel);
        view = binding.getRoot();
    }


    private void setListeners() {
        binding.buttonAddNewCategory.setOnClickListener(this::onClickAddNewCategory);
        binding.buttonClearFields.setOnClickListener(this::onClickClearFields);
    }

    private void setObservers() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                newCategoryViewModel::setDatabaseMode);
    }

    private void setCategoryListFromArguments() {
        assert getArguments() != null;
        newCategoryViewModel.setCategoryList(getArguments()
                .getParcelableArrayList("categoryArrayList"));
    }

    private void onClickAddNewCategory(View view) {
        newCategoryViewModel.onClickAddCategory();
        navigateToOwnCategories();
    }

    private void onClickClearFields(View view) {
        newCategoryViewModel.onClickClearFields();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void navigateToOwnCategories() {
        Navigation.findNavController(view).navigate(R.id.nav_own_categories);
    }
}