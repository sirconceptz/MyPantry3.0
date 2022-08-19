package com.hermanowicz.pantry.ui.scan_product;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.zxing.client.android.Intents;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.interfaces.ScanDecodedResult;
import com.hermanowicz.pantry.repository.SharedPreferencesRepository;
import com.hermanowicz.pantry.util.ScanIntentResult;
import com.hermanowicz.pantry.util.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ScanProductUseCaseImpl implements ScanProductUseCase {

    private String scanType;
    private final SharedPreferencesRepository sharedPreferencesRepository;
    private List<Product> productList;
    private List<Product> productListToAddBarcode;
    private ScanDecodedResult scanDecodedResultListener;
    private Resources resources;

    @Inject
    public ScanProductUseCaseImpl(SharedPreferencesRepository sharedPreferencesRepository, Resources resources) {
        this.sharedPreferencesRepository = sharedPreferencesRepository;
        this.resources = resources;
    }

    @Override
    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public void setBarcodeToProductList(String barcode) {
        for (Product product : productListToAddBarcode) {
            product.setBarcode(barcode);
        }
    }

    public int getSelectedCameraType() {
        return sharedPreferencesRepository.getSelectedCameraType();
    }

    public boolean getSelectedSoundMode() {
        return sharedPreferencesRepository.getSelectedSoundMode();
    }

    @Override
    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    @Override
    public void setScanResult(ScanIntentResult result) {
        if(result.getContents() == null) {
            Intent originalIntent = result.getOriginalIntent();
            if (originalIntent == null) {
                Log.d("Scanner", "Scan canceled");
                scanDecodedResultListener.onScanCanceled();
            } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Log.d("Scanner", "No camera permission");
                scanDecodedResultListener.onNoCameraPermission();
            }
        } else {
            Log.d("Scanner - Scan Result", result.getContents());
            processCorrectResultData(result);
        }
    }

    @Override
    public void setScanDecodedResultListener(ScanDecodedResult scanDecodedResultListener) {
        this.scanDecodedResultListener = scanDecodedResultListener;
    }

    @Override
    public void setBarcode(String barcode) {
        onBarcodeScan(barcode);
    }

    @Override
    public ScanOptions getScanOptions() {
        int selectedCameraId = getSelectedCameraType(); // 0 - Rear camera, 1 - Front camera
        boolean soundMode = getSelectedSoundMode(); // true - sound enabled, false - sound disabled
        String prompt = "";

        if (scanType.equals("QRCODE"))
            prompt = resources.getString(R.string.scan_qr_code);
        else if(scanType.equals("BARCODE"))
            prompt = resources.getString(R.string.scan_barcode);

        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
        options.setPrompt(prompt);
        options.setCameraId(selectedCameraId);
        options.setBeepEnabled(soundMode);
        options.setOrientationLocked(true);
        options.setBarcodeImageEnabled(true);
        return options;
    }

    private void processCorrectResultData(ScanIntentResult result) {
        if(scanType.equals("QRCODE"))
            onQrCodeScan(result.getContents());
        else if(scanType.equals("BARCODE"))
            onBarcodeScan(result.getContents());
    }

    private void onQrCodeScan(String qrcode) {
        List<Integer> decodedQRCodeAsList = decodeScanQRCodeResult(qrcode);
        int productId = decodedQRCodeAsList.get(0);
        int productHashCode = decodedQRCodeAsList.get(1);
        ArrayList<Product> productArrayList = new ArrayList<>(productList);
        scanDecodedResultListener.onScanQrCodeSuccess(productId, productHashCode, productArrayList);
    }

    private void onBarcodeScan(String barcode) {
        ArrayList<Product> productsWithBarcode = getProductListWithBarcode(barcode);
        scanDecodedResultListener.onScanBarCodeSuccess(productsWithBarcode);
    }

    private List<Integer> decodeScanQRCodeResult(@NonNull String scanResult) {
        List<Integer> decodedQRCodeAsList = null;
        try {
            decodedQRCodeAsList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(scanResult);
            decodedQRCodeAsList.add(jsonObject.getInt("product_id"));
            decodedQRCodeAsList.add(jsonObject.getInt("hash_code"));
        } catch (JSONException e) {
            Log.e("Decode scan result error", e.toString());
        }
        return decodedQRCodeAsList;
    }

    private ArrayList<Product> getProductListWithBarcode(String barcode) {
        ArrayList<Product> productListWithBarcode = new ArrayList<>();
        for(Product product : productList){
            if(product.getBarcode().equals(barcode))
                productListWithBarcode.add(product);
        }
        return productListWithBarcode;
    }
}