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

package com.hermanowicz.pantry.ui.print_qr_codes;

import static androidx.core.content.FileProvider.getUriForFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.FragmentPrintQrCodesBinding;
import com.hermanowicz.pantry.interfaces.PrintQRCodesViewActionsListener;
import com.hermanowicz.pantry.util.PdfFile;
import com.hermanowicz.pantry.util.SettingsIntent;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PrintQRCodesFragment extends Fragment implements PrintQRCodesViewActionsListener {

    private final int PERMISSION_REQUEST_CODE = 42;

    private FragmentPrintQrCodesBinding binding;
    private PrintQRCodesViewModel viewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        setListeners();
        getArgumentsAndSetData();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, ViewGroup container) {
        viewModel = new ViewModelProvider(this).get(PrintQRCodesViewModel.class);
        binding = FragmentPrintQrCodesBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        view = binding.getRoot();
        viewModel.setViewActionsListener(this);
    }

    private void setListeners() {
        binding.buttonPrintQRCodes.setOnClickListener(this::onClickPrintQRCodes);
        binding.buttonSendEmailQRCodes.setOnClickListener(this::onClickSendEmailQRCodes);
        binding.buttonSkip.setOnClickListener(this::onClickSkipButton);
    }

    private void getArgumentsAndSetData() {
        viewModel.setArguments(getArguments());
    }

    private void onClickPrintQRCodes(View view) {
        viewModel.setRequestedPrintQRCodesAction();
        requestPermission();
    }

    private void onClickSendEmailQRCodes(View view) {
        viewModel.setRequestedSendEmailQRCodesAction();
        requestPermission();
    }

    private void onClickSkipButton(View view) {
        navigateToMyPantry();
    }

    private void navigateToMyPantry() {
        Navigation.findNavController(view).navigate(R.id.action_nav_print_qr_codes_to_nav_my_pantry);
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

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            requestPermissionAboveApi28();
        else
            requestPermissionEqualAndBelowApi28();
    }

    private void requestPermissionEqualAndBelowApi28() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(requireActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED)
            viewModel.permissionGranted();
        else if (shouldShowRequestPermissionRationale(permissions[0]))
            showDialogInfoPermissionIsNeeded();
        else
            ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQUEST_CODE);
    }

    private void requestPermissionAboveApi28() {
        @SuppressLint("InlinedApi")
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION};
        if (ContextCompat.checkSelfPermission(requireActivity(), permissions[1]) == PackageManager.PERMISSION_GRANTED)
            viewModel.permissionGranted();
        else if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissions[0]) || ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissions[1]))
            showDialogInfoPermissionIsNeeded();
        else
            ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                viewModel.permissionGranted();
            return;
        }
        throw new IllegalStateException("Unexpected value: " + requestCode);
    }

    @Override
    public void openPdfFile(String pdfFileName) {
        Uri pdfUri = getUriForFile(requireActivity(), "com.hermanowicz.pantry.provider", PdfFile.getPdfFile(pdfFileName));
        Intent pdfDocumentIntent = new Intent(Intent.ACTION_VIEW);
        pdfDocumentIntent.setDataAndType(pdfUri, "application/pdf");
        pdfDocumentIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        pdfDocumentIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(pdfDocumentIntent);
    }

    @Override
    public void sendPdfWithQRCodesByEmail(String pdfFileName) {
        Uri pdfUri = getUriForFile(requireActivity(), "com.hermanowicz.pantry.provider", PdfFile.getPdfFile(pdfFileName));
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        if (pdfUri != null) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        }
        startActivity(Intent.createChooser(emailIntent, ""));
    }

    private void openAppSettings() {
        Intent settingsIntent = SettingsIntent.getSettingsIntent(requireActivity());
        startActivity(settingsIntent);
    }
}