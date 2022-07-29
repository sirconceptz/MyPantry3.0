package com.hermanowicz.pantry.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.databinding.FragmentProductDetailsBinding;
import com.hermanowicz.pantry.model.GroupProduct;
import com.hermanowicz.pantry.ui.product_details.ProductDetailsViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailsFragment extends Fragment {

    private ProductDetailsViewModel viewModel;
    private FragmentProductDetailsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ProductDetailsViewModel.class);

        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

        assert getArguments() != null;
        Product product = getArguments().getParcelable("product");
        int quantity = getArguments().getInt("productQuantity");
        GroupProduct groupProduct = new GroupProduct(product, quantity);
        viewModel.setGroupProduct(groupProduct);

        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}