package com.hermanowicz.pantry.ui.product_details;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.ArrayList;

public interface ProductDetailsUseCase {
    void setPhotoFile(String photoName);

    Bitmap getPhotoBitmap();

    Resources getResources();

    void setDatabaseMode(DatabaseMode databaseMode);

    void deleteProducts(ArrayList<Product> productArrayList);

    String getDateInFormatToShow(String expirationDate);
}
