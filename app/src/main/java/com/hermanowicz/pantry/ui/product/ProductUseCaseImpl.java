package com.hermanowicz.pantry.ui.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.GroupProduct;
import com.hermanowicz.pantry.repository.ProductRepository;

import java.util.List;

import javax.inject.Inject;

public class ProductUseCaseImpl implements ProductUseCase {

    private final ProductRepository repository;

    @Inject
    public ProductUseCaseImpl(ProductRepository productRepository){
        this.repository = productRepository;
    }

    @Override
    public LiveData<List<Product>> getAllProducts(DatabaseMode databaseMode){
        if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            return repository.getAllLocalProducts();
        if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            return repository.getAllOnlineProducts();
        else
            return new MutableLiveData<>();
    }

    @Override
    public List<GroupProduct> getAllGroupProductList(List<Product> productList){
        return GroupProduct.getGroupProducts(productList);
    }

    @Override
    public void setOnlineProductList(MutableLiveData<List<Product>> listMutableLiveData) {
        repository.setOnlineProductList(listMutableLiveData);
    }
}