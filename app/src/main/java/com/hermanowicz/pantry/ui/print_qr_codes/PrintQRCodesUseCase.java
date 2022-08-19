package com.hermanowicz.pantry.ui.print_qr_codes;

import com.hermanowicz.pantry.dao.db.product.Product;

import java.util.ArrayList;

public interface PrintQRCodesUseCase {
    void printQRCodes();

    void sendPdfWithQRCodesByEmail();

    String getPdfFileName();

    void setProductArrayList(ArrayList<Product> productArrayList);

    String getPermissionType();

    void setRequestedActionType(String requestedActionType);

    String getRequestedActionType();
}