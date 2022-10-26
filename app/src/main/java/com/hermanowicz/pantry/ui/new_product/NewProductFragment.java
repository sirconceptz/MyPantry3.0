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

package com.hermanowicz.pantry.ui.new_product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.databinding.FragmentNewProductBinding;
import com.hermanowicz.pantry.interfaces.NewProductViewActions;
import com.hermanowicz.pantry.interfaces.ProductToCopyView;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.dialogs.ChooseProductToCopyDialog;
import com.hermanowicz.pantry.util.DetailCategoryAdapter;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewProductFragment extends Fragment implements NewProductViewActions, ProductToCopyView {

    private FragmentNewProductBinding binding;
    private DatabaseModeViewModel databaseModeViewModel;
    private NewProductViewModel newProductViewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        initView(inflater, container);
        getArgumentsAndShowData();
        setObservers();
        setListeners();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        newProductViewModel = new ViewModelProvider(this).get(NewProductViewModel.class);
        binding = FragmentNewProductBinding.inflate(inflater, container, false);
        binding.setViewModel(newProductViewModel);
        view = binding.getRoot();
    }

    private void setListeners() {
        binding.buttonAddNewProduct.setOnClickListener(this::onClickAddNewProduct);
        binding.buttonClearFields.setOnClickListener(this::onClickClearFields);

        newProductViewModel.setNewProductDialogListener(this);
    }

    private void setObservers() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                newProductViewModel::setDatabaseMode);
        newProductViewModel.getMainCategoryValue().observe(getViewLifecycleOwner(), this::updateDetailCategoryAdapter);
        newProductViewModel.getStorageLocations().observe(getViewLifecycleOwner(), this::updateStorageLocationAdapter);
        newProductViewModel.getOwnCategoriesNamesLiveData().observe(getViewLifecycleOwner(), newProductViewModel::setOwnCategoriesNamesArray);
    }

    private void getArgumentsAndShowData() {
        newProductViewModel.showProductDataIfExists(getArguments());
    }

    private void updateDetailCategoryAdapter(String selectedMainCategory) {
        ArrayAdapter<CharSequence> detailCategoryAdapter =
                DetailCategoryAdapter.getDetailCategoryAdapter(requireContext(), selectedMainCategory, newProductViewModel.getOwnCategoriesNamesArray());
        binding.detailCategoryInput.setAdapter(detailCategoryAdapter);
    }

    private void updateStorageLocationAdapter(String[] storageLocations) {
        ArrayAdapter<CharSequence> storageLocationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, storageLocations);
        binding.storageLocationInput.setAdapter(storageLocationAdapter);
    }

    private void onClickAddNewProduct(View view) {
        if (newProductViewModel.isFormValid()) {
            int selectedTasteId = binding.radiogroupTaste.getCheckedRadioButtonId();
            RadioButton taste = view.findViewById(selectedTasteId);
            newProductViewModel.setTaste(taste);
            newProductViewModel.insertProducts();
            navigateToPrintQRCodes(newProductViewModel.getProductListToInsert());
        } else
            showErrorFormWrongFilled();
    }

    private void showErrorFormWrongFilled() {

    }

    private void navigateToPrintQRCodes(ArrayList<Product> productArrayList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("productArrayList", productArrayList);
        Navigation.findNavController(view).navigate(R.id.nav_print_qr_codes, bundle);
    }

    private void onClickClearFields(View view) {
        binding.radiogroupTaste.clearCheck();
        newProductViewModel.onClickClearFields();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void showDialogChooseProductToCopy(String[] groupProductNames) {
        ChooseProductToCopyDialog dialog = new ChooseProductToCopyDialog(groupProductNames);
        dialog.show(requireActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void showErrorNameIsTooShort() {
        binding.productNameText.setError(getString(R.string.error_product_name_is_required));
        Toast.makeText(requireActivity(), getString(R.string.error_product_name_is_required), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setSelectedProductToCopy(int position) {
        newProductViewModel.showSelectedProductData(position);
    }
}