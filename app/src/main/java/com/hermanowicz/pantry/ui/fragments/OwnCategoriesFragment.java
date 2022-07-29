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
import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.databinding.FragmentOwnCategoriesBinding;
import com.hermanowicz.pantry.ui.own_categories.OwnCategoriesViewModel;
import com.hermanowicz.pantry.util.OwnCategoriesAdapter;
import com.hermanowicz.pantry.util.RecyclerClickListener;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OwnCategoriesFragment extends Fragment {

    private OwnCategoriesViewModel viewModel;
    private FragmentOwnCategoriesBinding binding;
    private View view;
    private final OwnCategoriesAdapter ownCategoriesAdapter = new OwnCategoriesAdapter();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(OwnCategoriesViewModel.class);

        binding = FragmentOwnCategoriesBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        view = binding.getRoot();

        setupFloatingActionButton();
        setupRecyclerView();
        setObservers(viewModel);
        setListeners();

        return view;
    }

    private void setupFloatingActionButton() {
        binding.newCategoryFab.setColorFilter(Color.WHITE);
        binding.newCategoryFab.setOnClickListener(v -> onClickNewCategory());
    }

    private void setupRecyclerView() {
        binding.recyclerviewCategories.setAdapter(ownCategoriesAdapter);
        binding.recyclerviewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewCategories.setHasFixedSize(true);
        binding.recyclerviewCategories.setItemAnimator(new DefaultItemAnimator());
    }

    private void setObservers(OwnCategoriesViewModel viewModel) {
        viewModel.getAllCategories().observe(getViewLifecycleOwner(),
                this::setDataRecyclerViewAdapter);
    }

    private void setListeners() {
        binding.recyclerviewCategories.addOnItemTouchListener(new RecyclerClickListener(getContext(),
                binding.recyclerviewCategories, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Category category = viewModel.getCategory(position);
                navigateToCategoryDetails(view, category);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void setDataRecyclerViewAdapter(List<Category> categories) {
        ownCategoriesAdapter.setData(categories);
    }

    private void navigateToCategoryDetails(View view, Category category) {
        NavDirections action =
                OwnCategoriesFragmentDirections.actionNavOwnCategoriesToNavOwnCategoryDetail(category);
        Navigation.findNavController(view).navigate(action);
    }

    private void onClickNewCategory() {
        Navigation.findNavController(view)
                .navigate(R.id.action_nav_own_categories_to_nav_new_category);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}