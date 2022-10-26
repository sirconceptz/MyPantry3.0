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

package com.hermanowicz.pantry.ui.filter_product;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.FragmentFilterProductBinding;
import com.hermanowicz.pantry.model.FilterModel;
import com.hermanowicz.pantry.util.DetailCategoryAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FilterProductFragment extends Fragment {

    private FragmentFilterProductBinding binding;
    private FilterProductViewModel filterProductViewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        setListeners();
        setObservers();

        return view;
    }

    private void setListeners() {
        binding.buttonFilterProduct.setOnClickListener(this::onClickFilterProduct);
        filterProductViewModel.getFilterModel();
    }

    private void onClickFilterProduct(View view) {
        FilterModel filterModel = filterProductViewModel.getFilterModel();
        navigateToMyPantry(filterModel);
    }

    private void navigateToMyPantry(FilterModel filterModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("filterModel", filterModel);
        Navigation.findNavController(view).navigate(R.id.action_nav_filter_product_to_nav_my_pantry, bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        filterProductViewModel = new ViewModelProvider(this).get(FilterProductViewModel.class);
        binding = FragmentFilterProductBinding.inflate(inflater, container, false);
        binding.setViewModel(filterProductViewModel);
        view = binding.getRoot();
    }

    private void setObservers() {
        filterProductViewModel.getMainCategoryValue().observe(getViewLifecycleOwner(), this::updateDetailCategoryAdapter);
        filterProductViewModel.getStorageLocations().observe(getViewLifecycleOwner(), this::updateStorageLocationAdapter);
        filterProductViewModel.getOwnCategoriesNamesLiveData().observe(getViewLifecycleOwner(), filterProductViewModel::setOwnCategoriesNamesArray);
    }

    private void updateDetailCategoryAdapter(String selectedMainCategory) {
        ArrayAdapter<CharSequence> detailCategoryAdapter =
                DetailCategoryAdapter.getDetailCategoryAdapter(requireContext(), selectedMainCategory, filterProductViewModel.getOwnCategoriesNamesArray());
        binding.detailCategoryInput.setAdapter(detailCategoryAdapter);
    }

    private void updateStorageLocationAdapter(String[] storageLocations) {
        ArrayAdapter<CharSequence> storageLocationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, storageLocations);
        binding.storageLocationInput.setAdapter(storageLocationAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}