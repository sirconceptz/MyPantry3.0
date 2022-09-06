package com.hermanowicz.pantry.ui.fragments;


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
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.databinding.FragmentFilterProductBinding;
import com.hermanowicz.pantry.model.FilterModel;
import com.hermanowicz.pantry.ui.filter_product.FilterProductViewModel;
import com.hermanowicz.pantry.util.DetailCategoryAdapter;

import java.util.ArrayList;

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
        setProductListFromArguments();
    }

    private void setProductListFromArguments() {
        if(getArguments() != null) {
            ArrayList<Product> productArrayList = getArguments().getParcelableArrayList("productArrayList");
            filterProductViewModel.updateProductArrayList(productArrayList);
        }
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