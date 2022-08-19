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
import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.databinding.FragmentStorageLocationDetailBinding;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.storage_location_detail.StorageLocationDetailViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StorageLocationDetailFragment extends Fragment {

    private FragmentStorageLocationDetailBinding binding;
    private DatabaseModeViewModel databaseModeViewModel;
    private StorageLocationDetailViewModel storageLocationDetailViewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);

        setObservers();
        setListeners();
        getStorageLocationAndShow();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        storageLocationDetailViewModel = new ViewModelProvider(this).get(StorageLocationDetailViewModel.class);

        binding = FragmentStorageLocationDetailBinding.inflate(inflater, container, false);
        binding.setViewModel(storageLocationDetailViewModel);
        view = binding.getRoot();
    }

    private void setObservers() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                storageLocationDetailViewModel::setDatabaseMode);
    }

    private void setListeners() {
        binding.buttonUpdateStorageLocation.setOnClickListener(this::navigateToStorageLocations);
        binding.buttonDeleteStorageLocation.setOnClickListener(this::onClickDeleteStorageLocation);
    }

    private void getStorageLocationAndShow() {
        StorageLocation storageLocation = getStorageLocationData();
        storageLocationDetailViewModel.showStorageLocationData(storageLocation);
    }

    private StorageLocation getStorageLocationData() {
        return StorageLocationDetailFragmentArgs.fromBundle(getArguments()).getStorageLocation();
    }

    private void onClickDeleteStorageLocation(View view) {
        storageLocationDetailViewModel.onClickDeleteStorageLocation();
    }

    private void navigateToStorageLocations(View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_storage_location_detail_to_nav_storage_locations);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}