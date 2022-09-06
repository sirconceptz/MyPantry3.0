package com.hermanowicz.pantry.ui.print_qr_codes;

import com.hermanowicz.pantry.dao.db.product.Product;

import java.util.ArrayList;

public interface PrintQRCodesUseCase {
    void createPdfWithQRCodes();

    String getPdfFileName();

    void setProductArrayList(ArrayList<Product> productArrayList);

    void setRequestedActionType(String requestedActionType);

    String getRequestedActionType();
}