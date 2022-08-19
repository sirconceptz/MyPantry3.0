package com.hermanowicz.pantry.ui.print_qr_codes;

import android.Manifest;
import android.os.Build;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.repository.PdfDocumentsRepository;
import com.hermanowicz.pantry.repository.SharedPreferencesRepository;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

public class PrintQRCodesUseCaseImpl implements PrintQRCodesUseCase {

    private final PdfDocumentsRepository pdfDocumentsRepository;
    private final SharedPreferencesRepository sharedPreferencesRepository;
    private String requestedActionType;

    @Inject
    public PrintQRCodesUseCaseImpl(PdfDocumentsRepository pdfDocumentsRepository, SharedPreferencesRepository sharedPreferencesRepository) {
        this.pdfDocumentsRepository = pdfDocumentsRepository;
        this.sharedPreferencesRepository = sharedPreferencesRepository;
    }

    @Override
    public void printQRCodes() {
        boolean isBigPrintQRCode = sharedPreferencesRepository.getIsBigPrintQRCode();
        pdfDocumentsRepository.createAndSavePDF(isBigPrintQRCode);
    }

    @Override
    public void sendPdfWithQRCodesByEmail() {
        printQRCodes();
    }

    @Override
    public String getPdfFileName() {
        return pdfDocumentsRepository.getPdfFileName();
    }

    @Override
    public void setProductArrayList(ArrayList<Product> productArrayList) {
        pdfDocumentsRepository.setProductList(productArrayList);
    }

    @Override
    public String getPermissionType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            return Manifest.permission.WRITE_EXTERNAL_STORAGE;
        else
            return Manifest.permission.WRITE_EXTERNAL_STORAGE;
    }

    @Override
    public void setRequestedActionType(String requestedActionType) {
        this.requestedActionType = requestedActionType;
    }

    @Override
    public String getRequestedActionType() {
        return requestedActionType;
    }
}