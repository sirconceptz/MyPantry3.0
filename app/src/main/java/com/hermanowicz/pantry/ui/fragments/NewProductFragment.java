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

import com.hermanowicz.pantry.databinding.FragmentNewProductBinding;
import com.hermanowicz.pantry.ui.new_product.NewProductViewModel;
import com.hermanowicz.pantry.util.DetailCategoryAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewProductFragment extends Fragment {

    private FragmentNewProductBinding binding;
    private NewProductViewModel viewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(NewProductViewModel.class);

        binding = FragmentNewProductBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        view = binding.getRoot();

        setObservers();
        setListeners();

        return view;
    }

    private void setListeners() {
        binding.buttonAddNewProduct.setOnClickListener(view -> onClickAddNewProduct());
        binding.buttonClearFields.setOnClickListener(view -> onClickClearFields());
    }

    private void setObservers() {
        viewModel.getMainCategoryValue().observe(getViewLifecycleOwner(), this::updateDetailCategoryAdapter);
        viewModel.getStorageLocations().observe(getViewLifecycleOwner(), this::updateStorageLocationAdapter);
        viewModel.getOwnCategoriesNamesLiveData().observe(getViewLifecycleOwner(), viewModel::setOwnCategoriesNamesArray);
    }

    private void updateDetailCategoryAdapter(String selectedMainCategory){
        ArrayAdapter<CharSequence> detailCategoryAdapter =
                DetailCategoryAdapter.getDetailCategoryAdapter(requireContext(), selectedMainCategory, viewModel.getOwnCategoriesNamesArray());
        binding.detailCategoryInput.setAdapter(detailCategoryAdapter);
    }

    private void updateStorageLocationAdapter(String[] storageLocations){
        ArrayAdapter<CharSequence> storageLocationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, storageLocations);
        binding.storageLocationInput.setAdapter(storageLocationAdapter);
    }

    private void onClickAddNewProduct(){
        int selectedTasteId = binding.radiogroupTaste.getCheckedRadioButtonId();
        RadioButton taste = view.findViewById(selectedTasteId);
        viewModel.setTaste(taste);
        viewModel.onClickAddProduct();
    }

    private void onClickClearFields() {
        binding.radiogroupTaste.clearCheck();
        viewModel.onClickClearFields();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}