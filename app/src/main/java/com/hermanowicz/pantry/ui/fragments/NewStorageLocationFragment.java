package com.hermanowicz.pantry.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hermanowicz.pantry.databinding.FragmentNewStorageLocationBinding;
import com.hermanowicz.pantry.ui.new_storage_location.NewStorageLocationViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewStorageLocationFragment extends Fragment {

    private FragmentNewStorageLocationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewStorageLocationViewModel viewModel =
                new ViewModelProvider(this).get(NewStorageLocationViewModel.class);

        binding = FragmentNewStorageLocationBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        View root = binding.getRoot();

        setListeners();

        return root;
    }

    private void setListeners() {
        binding.addStorageLocation.setOnClickListener(view -> onClickAddNewStorageLocation());
    }

    private void onClickAddNewStorageLocation() {
        Toast.makeText(requireContext(), "Miejsce dodane Ziomeczku!", Toast.LENGTH_LONG).show();
        //TODO: nie dziala
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}