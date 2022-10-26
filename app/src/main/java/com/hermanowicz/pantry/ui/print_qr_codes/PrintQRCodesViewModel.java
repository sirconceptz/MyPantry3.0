package com.hermanowicz.pantry.ui.print_qr_codes;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.data.db.dao.product.Product;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PrintQRCodesViewModel extends ViewModel {

    @Inject
    PrintQRCodesUseCaseImpl useCase;

    private Bundle arguments;
    private PrintQRCodesFragment viewActionsListener;

    @Inject
    public PrintQRCodesViewModel(PrintQRCodesUseCaseImpl printQRCodesUseCase) {
        useCase = printQRCodesUseCase;
    }

    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }

    public void getProductsArrayListFromArguments() {
        if (arguments != null) {
            ArrayList<Product> productArrayList =
                    arguments.getParcelableArrayList("productArrayList");
            useCase.setProductArrayList(productArrayList);
        }
    }

    public void permissionGranted() {
        getProductsArrayListFromArguments();
        useCase.createPdfWithQRCodes();
        String pdfFileName = useCase.getPdfFileName();
        if (useCase.getRequestedActionType().equals("PRINT_QR_CODES"))
            viewActionsListener.openPdfFile(pdfFileName);
        else if (useCase.getRequestedActionType().equals("SEND_EMAIL_QR_CODES"))
            viewActionsListener.sendPdfWithQRCodesByEmail(pdfFileName);
    }

    public void setRequestedPrintQRCodesAction() {
        useCase.setRequestedActionType("PRINT_QR_CODES");
    }

    public void setRequestedSendEmailQRCodesAction() {
        useCase.setRequestedActionType("SEND_EMAIL_QR_CODES");
    }

    public void setViewActionsListener(PrintQRCodesFragment printQRCodesFragmentListener) {
        viewActionsListener = printQRCodesFragmentListener;
    }
}