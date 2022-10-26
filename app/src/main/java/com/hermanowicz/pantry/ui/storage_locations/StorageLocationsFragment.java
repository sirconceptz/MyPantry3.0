/*
 * Copyright (c) 2019-2022
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.ui.storage_locations;

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
import com.hermanowicz.pantry.data.db.dao.storagelocation.StorageLocation;
import com.hermanowicz.pantry.databinding.FragmentStorageLocationsBinding;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.util.RecyclerClickListener;
import com.hermanowicz.pantry.util.StorageLocationsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StorageLocationsFragment extends Fragment implements AvailableDataListener {

    private final StorageLocationsAdapter storageLocationsAdapter = new StorageLocationsAdapter();
    private FragmentStorageLocationsBinding binding;
    private DatabaseModeViewModel databaseModeViewModel;
    private StorageLocationsViewModel storageLocationsViewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        setupFloatingActionButton();
        setupRecyclerView();
        setListeners();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        storageLocationsViewModel = new ViewModelProvider(this).get(StorageLocationsViewModel.class);
        storageLocationsViewModel.setAvailableDataListener(this);
        storageLocationsViewModel.setDefaultDatabaseMode(databaseModeViewModel.getDatabaseModeFromSettings());

        binding = FragmentStorageLocationsBinding.inflate(inflater, container, false);
        binding.setViewModel(storageLocationsViewModel);
        view = binding.getRoot();
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

    private void setListeners() {
        binding.recyclerviewStorageLocations.addOnItemTouchListener(new RecyclerClickListener(getContext(),
                binding.recyclerviewStorageLocations, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StorageLocation storageLocation = storageLocationsViewModel.getStorageLocation(position);
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
        NavDirections action = StorageLocationsFragmentDirections
                .actionNavStorageLocationsToNavStorageLocationDetail(storageLocation);
        Navigation.findNavController(view).navigate(action);
    }

    private void onClickNewStorageLocation() {
        ArrayList<StorageLocation> storageLocationArrayList = new ArrayList<>();
        if (storageLocationsViewModel.getAllStorageLocationList() != null)
            storageLocationArrayList = new ArrayList<>(Objects.requireNonNull(storageLocationsViewModel.getAllStorageLocationList().getValue()));
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("storageLocationArrayList", storageLocationArrayList);
        Navigation.findNavController(view)
                .navigate(R.id.action_nav_storage_locations_to_nav_new_storage_location, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void observeAvailableData() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                storageLocationsViewModel::updateDataForSelectedDatabase);
        storageLocationsViewModel.getAllStorageLocationList().observe(getViewLifecycleOwner(),
                this::setDataRecyclerViewAdapter);
    }

    @Override
    public void onStart() {
        if (storageLocationsViewModel.isAvailableData())
            observeAvailableData();
        super.onStart();
    }

    @Override
    public void onPause() {
        storageLocationsViewModel.clearDisposable();
        super.onPause();
    }
}