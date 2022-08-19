package com.hermanowicz.pantry.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.databinding.FragmentProductDetailsBinding;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.product_details.ProductDetailsViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding binding;
    private DatabaseModeViewModel databaseModeViewModel;
    private ProductDetailsViewModel productDetailsViewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        setObservers();
        setListeners();
        getArgumentsAndShowData();

        view = binding.getRoot();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        productDetailsViewModel = new ViewModelProvider(this).get(ProductDetailsViewModel.class);

        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        binding.setViewModel(productDetailsViewModel);
    }

    private void setObservers() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                productDetailsViewModel::setDatabaseMode);
    }

    private void setListeners() {
        binding.buttonEditProduct.setOnClickListener(this::navigateToEditProduct);
        binding.buttonDeleteProduct.setOnClickListener(this::onClickDeleteProduct);
    }


    private void getArgumentsAndShowData() {
        productDetailsViewModel.setArguments(getArguments());
        productDetailsViewModel.showProductDataIfExists();
    }

    private void onClickDeleteProduct(View view) {
        productDetailsViewModel.onClickDeleteProduct();
    }

    private void navigateToEditProduct(View view) {
        ArrayList<Product> productArrayList = productDetailsViewModel.getProductArrayList();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("productArrayList", productArrayList);
        Navigation.findNavController(view).navigate(R.id.action_nav_product_details_to_nav_edit_product, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}