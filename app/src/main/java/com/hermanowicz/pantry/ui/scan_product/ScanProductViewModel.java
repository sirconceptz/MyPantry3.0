package com.hermanowicz.pantry.ui.scan_product;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.interfaces.ScanDecodedResult;
import com.hermanowicz.pantry.util.ScanIntentResult;
import com.hermanowicz.pantry.util.ScanOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ScanProductViewModel extends ViewModel {

    @Inject
    ScanProductUseCaseImpl useCase;

    @Inject
    public ScanProductViewModel(ScanProductUseCaseImpl scanProductUseCase) {
        useCase = scanProductUseCase;
    }

    public void setProductList(List<Product> productList) {
        useCase.setProductList(productList);
    }

    public void setResult(ScanIntentResult result) {
        useCase.setScanResult(result);
    }

    public void setViewListener(ScanDecodedResult scanDecodedResult){
        useCase.setScanDecodedResultListener(scanDecodedResult);
    }

    public void setScanType(String scanType){
        useCase.setScanType(scanType);
    }

    @NonNull
    public ScanOptions getScanOptions() {
        return useCase.getScanOptions();
    }

    public void setBarcode(String barcode) {
        useCase.setBarcode(barcode);
    }

    public void setProductListToAddBarcode(ArrayList<Product> productListToAddBarcode) {
        useCase.setProductList(productListToAddBarcode);
        useCase.setProductListToAddBarcode(productListToAddBarcode);
    }
}