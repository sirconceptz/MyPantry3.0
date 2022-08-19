package com.hermanowicz.pantry.ui.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.databinding.FragmentMyPantryBinding;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.model.GroupProduct;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.my_pantry.MyPantryViewModel;
import com.hermanowicz.pantry.ui.product.ProductViewModel;
import com.hermanowicz.pantry.util.ProductsAdapter;
import com.hermanowicz.pantry.util.RecyclerClickListener;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyPantryFragment extends Fragment implements AvailableDataListener {

    private FragmentMyPantryBinding binding;
    private MyPantryViewModel myPantryViewModel;
    private ProductViewModel productViewModel;
    private DatabaseModeViewModel databaseModeViewModel;
    private View view;
    private ProductsAdapter productsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        setupFloatingActionButtons();
        setupRecyclerView();
        setListeners();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        myPantryViewModel = new ViewModelProvider(this).get(MyPantryViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        productViewModel.setAvailableDataListener(this);

        binding = FragmentMyPantryBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        binding.setViewModel(myPantryViewModel);
    }

    private void setupFloatingActionButtons() {
        binding.newProductFab.setColorFilter(Color.WHITE);
        binding.newProductFab.setOnClickListener(v -> onClickNewProduct());

        binding.filterProductFab.setColorFilter(Color.WHITE);
        binding.filterProductFab.setOnClickListener(v -> onClickFilterProduct());
    }

    private void setupRecyclerView() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(requireActivity());
        productsAdapter = new ProductsAdapter(sharedPreferences);
        binding.recyclerviewProducts.setAdapter(productsAdapter);
        binding.recyclerviewProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewProducts.setHasFixedSize(true);
        binding.recyclerviewProducts.setItemAnimator(new DefaultItemAnimator());
    }

    private void setListeners() {
        binding.recyclerviewProducts.addOnItemTouchListener(new RecyclerClickListener(getContext(),
                binding.recyclerviewProducts, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ArrayList<Product> productArrayList = productViewModel.getProductSimilarProductsList(position);
                navigateToProductDetails(view, productArrayList);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void setDataRecyclerViewAdapter(List<GroupProduct> groupProducts) {
        productsAdapter.setData(groupProducts);
        binding.recyclerviewProducts.setAdapter(productsAdapter);
    }

    private void navigateToProductDetails(View view, ArrayList<Product> productArrayList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("productArrayList", productArrayList);
        Navigation.findNavController(view).navigate(R.id.action_nav_my_pantry_to_nav_product_details, bundle);
    }

    public void onClickNewProduct() {
        Navigation.findNavController(view).navigate(R.id.action_nav_my_pantry_to_nav_new_product);
    }

    public void onClickFilterProduct() {
        Navigation.findNavController(view).navigate(R.id.action_nav_my_pantry_to_nav_filter_product);
    }

    @Override
    public void observeAvailableData() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                productViewModel::updateDataForSelectedDatabase);
        productViewModel.getAllProductList().observe(getViewLifecycleOwner(),
                productViewModel::convertProductListToGroupProductList);
        productViewModel.getAllGroupProductList().observe(getViewLifecycleOwner(),
                this::setDataRecyclerViewAdapter);
    }

    @Override
    public void onStart(){
        if(productViewModel.isAvailableData())
            observeAvailableData();
        super.onStart();
    }

    @Override
    public void onPause() {
        productViewModel.clearDisposable();
        super.onPause();
    }
}