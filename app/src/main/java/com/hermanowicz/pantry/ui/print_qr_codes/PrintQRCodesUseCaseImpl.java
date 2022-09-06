package com.hermanowicz.pantry.ui.print_qr_codes;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.repository.PdfDocumentsRepository;
import com.hermanowicz.pantry.repository.SharedPreferencesRepository;

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
    public void createPdfWithQRCodes() {
        boolean isBigPrintQRCode = sharedPreferencesRepository.getIsBigPrintQRCode();
        pdfDocumentsRepository.createAndSavePDF(isBigPrintQRCode);
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
    public void setRequestedActionType(String requestedActionType) {
        this.requestedActionType = requestedActionType;
    }

    @Override
    public String getRequestedActionType() {
        return requestedActionType;
    }
}