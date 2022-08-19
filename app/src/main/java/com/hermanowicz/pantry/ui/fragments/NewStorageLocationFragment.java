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
import com.hermanowicz.pantry.databinding.FragmentNewStorageLocationBinding;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.new_storage_location.NewStorageLocationViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewStorageLocationFragment extends Fragment {

    private FragmentNewStorageLocationBinding binding;
    private NewStorageLocationViewModel newStorageLocationViewModel;
    private DatabaseModeViewModel databaseModeViewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        setObservers();
        setListeners();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        newStorageLocationViewModel = new ViewModelProvider(this).get(NewStorageLocationViewModel.class);
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        binding = FragmentNewStorageLocationBinding.inflate(inflater, container, false);
        binding.setViewModel(newStorageLocationViewModel);
        view = binding.getRoot();
    }


    private void setListeners() {
        binding.buttonAddStorageLocation.setOnClickListener(this::onClickAddNewStorageLocation);
        binding.buttonClearFields.setOnClickListener(this::onClickClearFields);
    }


    private void onClickAddNewStorageLocation(View view) {
        newStorageLocationViewModel.onClickAddStorageLocation();
        navigateToStorageLocations();
    }

    private void onClickClearFields(View view) {
        newStorageLocationViewModel.onClickClearFields();
    }

    private void setObservers() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                newStorageLocationViewModel::setDatabaseMode);
    }

    private void navigateToStorageLocations() {
        Bundle bundle = new Bundle();
        Navigation.findNavController(view)
                .navigate(R.id.nav_storage_locations);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}