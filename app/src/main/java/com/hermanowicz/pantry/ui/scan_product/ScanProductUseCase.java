package com.hermanowicz.pantry.ui.scan_product;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.interfaces.ScanDecodedResult;
import com.hermanowicz.pantry.util.ScanIntentResult;
import com.hermanowicz.pantry.util.ScanOptions;

import java.util.List;

public interface ScanProductUseCase {
    void setProductList(List<Product> productList);

    void setBarcodeToProductList(String barcode);

    void setScanType(String scanType);

    void setScanResult(ScanIntentResult result);

    void setScanDecodedResultListener(ScanDecodedResult scanDecodedResult);

    void setBarcode(String barcode);

    ScanOptions getScanOptions();
}
