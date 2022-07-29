package com.hermanowicz.pantry.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.databinding.FragmentStorageLocationDetailBinding;
import com.hermanowicz.pantry.ui.storage_location_detail.StorageLocationDetailViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StorageLocationDetailFragment extends Fragment {

    private StorageLocationDetailViewModel viewModel;
    private FragmentStorageLocationDetailBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(StorageLocationDetailViewModel.class);

        binding = FragmentStorageLocationDetailBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        View root = binding.getRoot();

        getStorageLocationAndShowData();

        return root;
    }

    private void getStorageLocationAndShowData() {
        StorageLocation storageLocation = getStorageLocationData();
        viewModel.showStorageLocationData(storageLocation);
    }

    private StorageLocation getStorageLocationData() {
        return StorageLocationDetailFragmentArgs.fromBundle(getArguments()).getStorageLocation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}