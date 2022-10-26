package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.data.db.dao.product.Product;

import java.util.ArrayList;

public interface ScanDecodedResult {
    void onScanAddBarCodeSuccess();

    void onScanBarCodeSuccess(ArrayList<Product> productsWithBarcode);

    void onScanCanceled();

    void onNoCameraPermission();

    void onScanQrCodeSuccess(int productId, int productHashCode, ArrayList<Product> productArrayList);
}
