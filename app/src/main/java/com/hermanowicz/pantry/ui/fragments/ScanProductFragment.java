package com.hermanowicz.pantry.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.databinding.FragmentScanProductBinding;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.interfaces.ScanDecodedResult;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.product.ProductViewModel;
import com.hermanowicz.pantry.ui.scan_product.ScanProductViewModel;
import com.hermanowicz.pantry.util.ScanContract;
import com.hermanowicz.pantry.util.ScanOptions;
import com.hermanowicz.pantry.util.SettingsIntent;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanProductFragment extends Fragment implements AvailableDataListener, ScanDecodedResult {

    private final int PERMISSION_REQUEST_CODE = 42;

    private FragmentScanProductBinding binding;
    private ScanProductViewModel scanProductViewModel;
    private ProductViewModel productViewModel;
    private DatabaseModeViewModel databaseModeViewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        setListeners();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);
        scanProductViewModel = new ViewModelProvider(this).get(ScanProductViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        binding = FragmentScanProductBinding.inflate(inflater, container, false);
        binding.setViewModel(scanProductViewModel);

        view = binding.getRoot();
    }

    private void setListeners() {
        productViewModel.setAvailableDataListener(this);
        scanProductViewModel.setViewListener(this);

        binding.buttonScanProductQRCode.setOnClickListener(this::onClickScanProductQRCode);
        binding.buttonScanProductBarcode.setOnClickListener(this::onClickScanProductBarcode);
        binding.buttonEnterBarcodeManually.setOnClickListener(this::onClickEnterBarcodeManually);
    }

    private void onClickScanProductQRCode(View view) {
        scanProductViewModel.setScanType("QRCODE");
        requestPermission();
    }

    private void onClickScanProductBarcode(View view) {
        scanProductViewModel.setScanType("BARCODE");
        requestPermission();
    }

    private void onClickEnterBarcodeManually(View view) {
        scanProductViewModel.setScanType("BARCODE");
        showDialogBarcodeManually();
    }

    private final ActivityResultLauncher<ScanOptions> activityResultLauncher = registerForActivityResult(new ScanContract(),
            result -> scanProductViewModel.setResult(result));

    private void initScanner() {
        ScanOptions options = scanProductViewModel.getScanOptions();
        activityResultLauncher.launch(options);
    }

    private void showDialogBarcodeManually() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(getString(R.string.scan_barcode_manually));
        final EditText input = new EditText(requireActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input)
                .setPositiveButton(getString(R.string.general_save), (dialog, click) -> {
                    String barcode = input.getText().toString();
                    scanProductViewModel.setScanType("BARCODE");
                    scanProductViewModel.setBarcode(barcode);
                })
                .setNegativeButton("Anuluj", (dialog, click) -> dialog.cancel())
                .show();
    }

    private void showDialogInfoPermissionIsNeeded() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(getString(R.string.general_permission_required_title))
                .setMessage(getString(R.string.general_permission_required_message))
                .setPositiveButton(getString(android.R.string.ok), (dialog, click) ->
                       openAppSettings())
                .setNegativeButton(getString(R.string.general_no_thanks), (dialog, click) -> dialog.cancel())
                .show();
    }

    private void openAppSettings() {
        Intent settingsIntent = SettingsIntent.getSettingsIntent(requireActivity());
        startActivity(settingsIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void navigateToNewProduct(String barcode, ArrayList<Product> productArrayList) {
        Bundle bundle = new Bundle();
        bundle.putString("barcode", barcode);
        bundle.putParcelableArrayList("productArrayList", productArrayList);
        Navigation.findNavController(view).navigate(R.id.action_nav_scan_product_to_nav_new_product, bundle);
    }

    private void navigateToProductDetails(String barcode, ArrayList<Product> productArrayList) {
        Bundle bundle = new Bundle();
        bundle.putString("barcode", barcode);
        bundle.putParcelableArrayList("productArrayList", productArrayList);
        Navigation.findNavController(view).navigate(R.id.action_nav_scan_product_to_nav_product_details, bundle);
    }

    @Override
    public void observeAvailableData() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                productViewModel::updateDataForSelectedDatabase);
        productViewModel.getAllProductList().observe(getViewLifecycleOwner(),
                scanProductViewModel::setProductList);
    }

    private void requestPermission() {
        String[] permissions = new String[] {Manifest.permission.CAMERA};
        int checkPermission = ContextCompat.checkSelfPermission(requireActivity(), permissions[0]);
        if (checkPermission == PackageManager.PERMISSION_GRANTED)
            initScanner();
        else if(shouldShowRequestPermissionRationale(permissions[0]))
            showDialogInfoPermissionIsNeeded();
        else
            ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQUEST_CODE);
    }

    private void navigateToProductDetails(int productId, int productHashCode, ArrayList<Product> productArrayList) {
        Bundle bundle = new Bundle();
        bundle.putInt("productId", productId);
        bundle.putInt("productHashCode", productHashCode);
        bundle.putParcelableArrayList("productArrayList", productArrayList);
        Navigation.findNavController(view)
                .navigate(R.id.action_nav_scan_product_to_nav_product_details, bundle);
    }

    private void navigateToNewProduct(ArrayList<Product> productsWithBarcode) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("productArrayList", productsWithBarcode);
        Navigation.findNavController(view)
                .navigate(R.id.action_nav_scan_product_to_nav_new_product, bundle);
    }

    @Override
    public void onScanBarCodeSuccess(ArrayList<Product> productsWithBarcode) {
        navigateToNewProduct(productsWithBarcode);
    }

    @Override
    public void onScanCanceled() {
        Toast.makeText(requireActivity(), "Scan canceled!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNoCameraPermission() {
        Toast.makeText(requireActivity(), "No camera permission!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onScanQrCodeSuccess(int productId, int productHashCode, ArrayList<Product> productArrayList) {
        navigateToProductDetails(productId, productHashCode, productArrayList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                initScanner();
            return;
        }
        throw new IllegalStateException("Unexpected value: " + requestCode);
    }
}