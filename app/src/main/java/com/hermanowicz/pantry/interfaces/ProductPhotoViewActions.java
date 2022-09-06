package com.hermanowicz.pantry.interfaces;

import android.graphics.Bitmap;

import com.hermanowicz.pantry.dao.db.product.Product;

import java.io.File;

public interface ProductPhotoViewActions {
    void takePhotoIntent(File photoFile);

    void showPhoto(Product product, Bitmap bitmap);

    void showDescription(String photoDescription);
}
