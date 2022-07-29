package com.hermanowicz.pantry.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hermanowicz.pantry.databinding.FragmentNewCategoryBinding;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.new_category.NewCategoryViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewCategoryFragment extends Fragment {

    private FragmentNewCategoryBinding binding;

    private NewCategoryViewModel viewModel;
    private DatabaseModeViewModel databaseModeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(NewCategoryViewModel.class);
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);

        binding = FragmentNewCategoryBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        View root = binding.getRoot();

        setObservers();

        return root;
    }

    private void setObservers() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                viewModel::setDatabaseMode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}