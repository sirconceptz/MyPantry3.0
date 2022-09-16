package com.hermanowicz.pantry.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.FragmentEditProductBinding;
import com.hermanowicz.pantry.interfaces.EditProductViewActions;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.edit_product.EditProductViewModel;
import com.hermanowicz.pantry.util.DetailCategoryAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditProductFragment extends Fragment implements EditProductViewActions {

    private FragmentEditProductBinding binding;
    private DatabaseModeViewModel databaseModeViewModel;
    private EditProductViewModel editProductViewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        setObservers();
        setListeners();
        getProductAndShow();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        editProductViewModel = new ViewModelProvider(this).get(EditProductViewModel.class);
        editProductViewModel.setViewActionsListener(this);

        binding = FragmentEditProductBinding.inflate(inflater, container, false);
        binding.setViewModel(editProductViewModel);

        view = binding.getRoot();
    }

    private void setListeners() {
        binding.buttonUpdateProduct.setOnClickListener(this::onClickUpdateProduct);
    }

    private void onClickUpdateProduct(View view) {
        int selectedTasteId = binding.radiogroupTaste.getCheckedRadioButtonId();
        RadioButton taste = view.findViewById(selectedTasteId);
        editProductViewModel.setTaste(taste);
        editProductViewModel.onClickUpdateProduct();
        navigateToMyPantry();
    }

    private void navigateToMyPantry() {
        Navigation.findNavController(view).navigate(R.id.action_nav_edit_product_to_nav_my_pantry);
    }

    private void setObservers() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                editProductViewModel::setDatabaseMode);
        editProductViewModel.getStorageLocations().observe(getViewLifecycleOwner(), this::updateStorageLocationAdapter);
        editProductViewModel.getOwnCategoriesNamesLiveData().observe(getViewLifecycleOwner(), editProductViewModel::setOwnCategoriesNamesArray);
    }

    private void getProductAndShow() {
        editProductViewModel.getProductAndShow(getArguments());
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

    @Override
    public void setSpinnerSelections(int productMainCategorySpinner, int productDetailCategorySpinner, int productStorageLocationSpinnerPosition) {
        binding.mainCategoryInput.setSelection(productMainCategorySpinner);
        setDetailCategoryAdapter(productDetailCategorySpinner);
        binding.storageLocationInput.setSelection(productStorageLocationSpinnerPosition);
    }

    private void setDetailCategoryAdapter(int productDetailCategorySpinner) {
        String mainCategory = binding.mainCategoryInput.getSelectedItem().toString();
        String[] detailCategoryArray = editProductViewModel.getDetailCategoryArray(mainCategory);
        ArrayAdapter<CharSequence> detailCategoryAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, detailCategoryArray);
        binding.detailCategoryInput.setAdapter(detailCategoryAdapter);
        binding.detailCategoryInput.setSelection(productDetailCategorySpinner);
    }
}