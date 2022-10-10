package com.hermanowicz.pantry.repository;

import android.graphics.Bitmap;

import com.hermanowicz.pantry.dao.db.product.Product;

import java.util.List;

public interface PdfDocumentsRepository {
    String getPdfFileName();

    void setProductList(List<Product> productList);

    void createAndSavePDF(boolean isQrCodeSizePrintBig);
}