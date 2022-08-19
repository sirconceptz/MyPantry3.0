package com.hermanowicz.pantry.repository;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.util.PdfData;
import com.hermanowicz.pantry.util.PdfFile;
import com.hermanowicz.pantry.util.PrintQRData;

import java.util.ArrayList;
import java.util.List;

public class PdfDocumentsRepositoryImpl implements PdfDocumentsRepository {

    private final Context context;
    private String pdfFileName;
    private ArrayList<String> textToQRCodeArray = new ArrayList<>();
    private ArrayList<String> namesOfProductsArray = new ArrayList<>();
    private ArrayList<String> expirationDatesArray = new ArrayList<>();
    private ArrayList<String> productionDatesArray = new ArrayList<>();

    public PdfDocumentsRepositoryImpl(Context context){
        this.context = context;
    }

    @Override
    public String getPdfFileName(){
        return pdfFileName;
    }

    @Override
    public Bitmap getThumb() {
        return PrintQRData.getBitmapQRCode(textToQRCodeArray.get(0), false);
    }

    @Override
    public void setProductList(List<Product> productList) {
        textToQRCodeArray = PrintQRData.getTextToQRCodeList(productList);
        namesOfProductsArray = PrintQRData.getNamesOfProductsList(productList);
        expirationDatesArray = PrintQRData.getExpirationDatesList(productList);
        productionDatesArray = PrintQRData.getProductionDatesList(productList);
    }

    @Override
    public void createAndSavePDF(boolean isQrCodeSizePrintBig) {
        pdfFileName = PdfFile.generatePdfFileName();

        ArrayList<Bitmap> qrCodeBitmapArray = PrintQRData.getQrCodeBitmapArray(textToQRCodeArray, isQrCodeSizePrintBig);
        android.graphics.pdf.PdfDocument pdfDocument;
        PdfData pdfData = new PdfData();
        if (isQrCodeSizePrintBig)
            pdfDocument = pdfData.createPdfDocumentBigQrCodes(qrCodeBitmapArray, namesOfProductsArray, expirationDatesArray, productionDatesArray);
        else
            pdfDocument = pdfData.createPdfDocumentSmallQrCodes(qrCodeBitmapArray, namesOfProductsArray, expirationDatesArray);
        PdfFile.savePdf(context, pdfDocument, pdfFileName);
        pdfDocument.close();
    }
}