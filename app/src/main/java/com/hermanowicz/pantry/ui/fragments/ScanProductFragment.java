package com.hermanowicz.pantry.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hermanowicz.pantry.databinding.FragmentScanProductBinding;
import com.hermanowicz.pantry.ui.scan_product.ScanProductViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanProductFragment extends Fragment {

    private FragmentScanProductBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScanProductViewModel viewModel =
                new ViewModelProvider(this).get(ScanProductViewModel.class);

        binding = FragmentScanProductBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        View root = binding.getRoot();

        final TextView textView = binding.textView;
        viewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}