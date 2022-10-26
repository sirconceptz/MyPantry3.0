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

package com.hermanowicz.pantry.ui.my_pantry;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.databinding.FragmentMyPantryBinding;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.interfaces.FilteredDataListener;
import com.hermanowicz.pantry.model.GroupProduct;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.product.ProductViewModel;
import com.hermanowicz.pantry.ui.settings.SettingsViewModel;
import com.hermanowicz.pantry.util.ProductsAdapter;
import com.hermanowicz.pantry.util.RecyclerClickListener;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyPantryFragment extends Fragment implements AvailableDataListener, FilteredDataListener {

    private FragmentMyPantryBinding binding;
    private ProductViewModel productViewModel;
    private DatabaseModeViewModel databaseModeViewModel;
    private SettingsViewModel settingsViewModel;
    private View view;
    private ProductsAdapter productsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        setupFloatingActionButtons();
        setupRecyclerView();
        setListeners();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentMyPantryBinding.inflate(inflater, container, false);

        view = binding.getRoot();
    }

    private void setupProductViewModel() {
        productViewModel.setFilterModelFromArguments(getArguments());
        productViewModel.setFilteredDataListener(this);
        productViewModel.setAvailableDataListener(this);
        productViewModel.setDefaultDatabaseMode(databaseModeViewModel.getDatabaseModeFromSettings());
    }

    private void setupFloatingActionButtons() {
        binding.newProductFab.setColorFilter(Color.WHITE);
        binding.newProductFab.setOnClickListener(v -> onClickNewProduct());

        binding.filterProductFab.setColorFilter(Color.WHITE);
        binding.filterProductFab.setOnClickListener(v -> onClickFilterProduct());
    }

    private void setupRecyclerView() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(requireActivity());
        productsAdapter = new ProductsAdapter(sharedPreferences);
        binding.recyclerviewProducts.setAdapter(productsAdapter);
        binding.recyclerviewProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewProducts.setHasFixedSize(true);
        binding.recyclerviewProducts.setItemAnimator(new DefaultItemAnimator());
    }

    private void setListeners() {
        binding.recyclerviewProducts.addOnItemTouchListener(new RecyclerClickListener(getContext(),
                binding.recyclerviewProducts, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ArrayList<Product> productArrayList = productViewModel.getProductSimilarProductsList(position);
                navigateToProductDetails(view, productArrayList);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void setDataRecyclerViewAdapter(List<GroupProduct> groupProducts) {
        productsAdapter.setData(groupProducts);
        binding.recyclerviewProducts.setAdapter(productsAdapter);
    }

    private void navigateToProductDetails(View view, ArrayList<Product> productArrayList) {
        int productId = productArrayList.get(0).getId();
        String productHashCode = productArrayList.get(0).getHashCode();
        Bundle bundle = new Bundle();
        bundle.putInt("productId", productId);
        bundle.putString("productHashCode", productHashCode);
        bundle.putParcelableArrayList("productArrayList", productArrayList);
        Navigation.findNavController(view).navigate(R.id.action_nav_my_pantry_to_nav_product_details, bundle);
    }

    public void onClickNewProduct() {
        Navigation.findNavController(view).navigate(R.id.action_nav_my_pantry_to_nav_new_product);
    }

    public void onClickFilterProduct() {
        List<Product> productList = productViewModel.getAllProductList().getValue();
        assert productList != null;
        ArrayList<Product> productArrayList = new ArrayList<>(productList);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("productArrayList", productArrayList);
        Navigation.findNavController(view).navigate(R.id.action_nav_my_pantry_to_nav_filter_product, bundle);
    }

    @Override
    public void observeAvailableData() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                productViewModel::updateDataForSelectedDatabase);
        productViewModel.getAllProductList().observe(getViewLifecycleOwner(),
                this::filterProductList);
    }

    private void filterProductList(List<Product> productList) {
        settingsViewModel.setProductList(productList);
        productViewModel.filterProductList(productList);
    }

    @Override
    public void observeFilteredData() {
        settingsViewModel.restoreNotificationsIfNeeded();
        productViewModel.getFilteredProductsLiveData().observe(getViewLifecycleOwner(),
                productViewModel::convertProductListToGroupProductList);
        productViewModel.getAllGroupProductList().observe(getViewLifecycleOwner(),
                this::setDataRecyclerViewAdapter);
    }

    @Override
    public void onResume() {
        setupProductViewModel();
        if (productViewModel.isAvailableData())
            observeAvailableData();
        super.onResume();
    }

    @Override
    public void onPause() {
        productViewModel.clearDisposable();
        super.onPause();
    }
}