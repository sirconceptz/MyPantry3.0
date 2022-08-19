package com.hermanowicz.pantry.ui.product_details;

import android.content.res.Resources;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.Database;

public interface ProductDetailsUseCase {
    Resources getResources();

    void deleteSimilarProducts(Product product);

    void setDatabaseMode(Database databaseMode);
}
