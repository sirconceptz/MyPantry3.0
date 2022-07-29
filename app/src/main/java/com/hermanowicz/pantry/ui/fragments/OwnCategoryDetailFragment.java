package com.hermanowicz.pantry.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.databinding.FragmentOwnCategoryDetailBinding;
import com.hermanowicz.pantry.ui.own_category_detail.OwnCategoryDetailViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OwnCategoryDetailFragment extends Fragment {

    private OwnCategoryDetailViewModel viewModel;
    private FragmentOwnCategoryDetailBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(OwnCategoryDetailViewModel.class);

        binding = FragmentOwnCategoryDetailBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        View root = binding.getRoot();

        getCategoryAndShowData();

        return root;
    }

    private void getCategoryAndShowData() {
        Category category = getCategoryData();
        viewModel.showCategoryData(category);
    }

    private Category getCategoryData() {
        return OwnCategoryDetailFragmentArgs.fromBundle(getArguments()).getCategory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}