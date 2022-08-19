package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.dao.db.product.Product;

import java.util.ArrayList;

public interface ScanDecodedResult {
    void onScanBarCodeSuccess(ArrayList<Product> productsWithBarcode);

    void onScanCanceled();

    void onNoCameraPermission();

    void onScanQrCodeSuccess(int productId, int productHashCode, ArrayList<Product> productArrayList);
}
