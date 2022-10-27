/*
 * Copyright (c) 2019-2022
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.data.repository;

import android.content.Context;
import android.graphics.Bitmap;

import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.domain.repository.PdfDocumentsRepository;
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

    public PdfDocumentsRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getPdfFileName() {
        return pdfFileName;
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