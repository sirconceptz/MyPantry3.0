package com.hermanowicz.pantry.ui.product_details;

import android.content.res.Resources;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.Database;
import com.hermanowicz.pantry.repository.ProductRepository;

import java.util.List;

import javax.inject.Inject;

public class ProductDetailsUseCaseImpl implements ProductDetailsUseCase {

    private final ProductRepository repository;
    private final Resources resources;
    private Database databaseMode;

    @Inject
    public ProductDetailsUseCaseImpl(ProductRepository productRepository, Resources resources) {
        this.repository = productRepository;
        this.resources = resources;
    }

    @Override
    public Resources getResources() {
        return resources;
    }

    @Override
    public void deleteSimilarProducts(Product product) {
        List<Product> allProductList = repository.getAllProducts(databaseMode).getValue();
        assert allProductList != null;
        List<Product> productListToDelete = Product.getSimilarProductsList(product, allProductList);
        repository.delete(productListToDelete, databaseMode);
    }

    @Override
    public void setDatabaseMode(Database databaseMode) {
        this.databaseMode = databaseMode;
    }
}