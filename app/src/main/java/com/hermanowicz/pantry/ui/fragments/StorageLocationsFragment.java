package com.hermanowicz.pantry.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.databinding.FragmentStorageLocationsBinding;
import com.hermanowicz.pantry.ui.storage_locations.StorageLocationsViewModel;
import com.hermanowicz.pantry.util.RecyclerClickListener;
import com.hermanowicz.pantry.util.StorageLocationsAdapter;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StorageLocationsFragment extends Fragment {

    private StorageLocationsViewModel viewModel;
    private FragmentStorageLocationsBinding binding;
    private View view;
    private final StorageLocationsAdapter storageLocationsAdapter = new StorageLocationsAdapter();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(StorageLocationsViewModel.class);

        binding = FragmentStorageLocationsBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        view = binding.getRoot();

        setupFloatingActionButton();
        setupRecyclerView();
        setObservers(viewModel);
        setListeners();

        return view;
    }

    private void setupFloatingActionButton() {
        binding.newStorageLocationFab.setColorFilter(Color.WHITE);
        binding.newStorageLocationFab.setOnClickListener(v -> onClickNewStorageLocation());
    }

    private void setupRecyclerView() {
        binding.recyclerviewStorageLocations.setAdapter(storageLocationsAdapter);
        binding.recyclerviewStorageLocations.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewStorageLocations.setHasFixedSize(true);
        binding.recyclerviewStorageLocations.setItemAnimator(new DefaultItemAnimator());
    }

    private void setObservers(StorageLocationsViewModel viewModel) {
        viewModel.getAllStorageLocationList().observe(getViewLifecycleOwner(),
                this::setDataRecyclerViewAdapter);
    }

    private void setListeners() {
        binding.recyclerviewStorageLocations.addOnItemTouchListener(new RecyclerClickListener(getContext(),
                binding.recyclerviewStorageLocations, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StorageLocation storageLocation = viewModel.getStorageLocation(position);
                navigateToStorageLocationDetails(view, storageLocation);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void setDataRecyclerViewAdapter(List<StorageLocation> storageLocations) {
        storageLocationsAdapter.setData(storageLocations);
    }

    private void navigateToStorageLocationDetails(View view, StorageLocation storageLocation) {
        NavDirections action =
                StorageLocationsFragmentDirections
                        .actionNavStorageLocationsToNavStorageLocationDetail(storageLocation);
        Navigation.findNavController(view).navigate(action);
    }

    private void onClickNewStorageLocation() {
        Navigation.findNavController(view)
                .navigate(R.id.action_nav_storage_locations_to_nav_new_storage_location);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}