package com.hermanowicz.pantry.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hermanowicz.pantry.databinding.FragmentFilterProductBinding;
import com.hermanowicz.pantry.ui.filter_product.FilterProductViewModel;
import com.hermanowicz.pantry.util.DetailCategoryAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FilterProductFragment extends Fragment {

    private FragmentFilterProductBinding binding;
    private FilterProductViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(FilterProductViewModel.class);

        binding = FragmentFilterProductBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        View root = binding.getRoot();

        setObservers();

        return root;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}