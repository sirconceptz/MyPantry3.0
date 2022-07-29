package com.hermanowicz.pantry.ui.product_details;

import android.content.res.Resources;

import com.hermanowicz.pantry.dao.db.product.Product;

public interface ProductDetailsUseCase {
    Resources getResources();
    void deleteSimilarProducts(Product product);
}
