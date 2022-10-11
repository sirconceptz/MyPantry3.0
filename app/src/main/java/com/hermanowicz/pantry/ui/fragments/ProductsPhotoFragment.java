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

package com.hermanowicz.pantry.ui.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.databinding.FragmentProductsPhotoBinding;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.interfaces.PhotoEditViewActions;
import com.hermanowicz.pantry.interfaces.ShowPhotoViewActions;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeViewModel;
import com.hermanowicz.pantry.ui.products_photo.ProductsPhotoViewModel;
import com.hermanowicz.pantry.util.ImageRotation;
import com.hermanowicz.pantry.util.SettingsIntent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductsPhotoFragment extends Fragment implements PhotoEditViewActions, ShowPhotoViewActions, AvailableDataListener {

    private final int PERMISSION_REQUEST_CODE = 42;
    private final int REQUEST_IMAGE_CAPTURE_CODE = 15;

    private FragmentProductsPhotoBinding binding;
    private ProductsPhotoViewModel productsPhotoViewModel;
    private DatabaseModeViewModel databaseModeViewModel;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initView(inflater, container);
        getArgumentsAndSetProductArrayList();
        setObservers();
        setListeners();

        return view;
    }

    private void initView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        productsPhotoViewModel = new ViewModelProvider(this).get(ProductsPhotoViewModel.class);
        databaseModeViewModel = new ViewModelProvider(this).get(DatabaseModeViewModel.class);

        binding = FragmentProductsPhotoBinding.inflate(inflater, container, false);
        binding.setViewModel(productsPhotoViewModel);

        productsPhotoViewModel.setViewActionsListeners(this, this);
        productsPhotoViewModel.setProductArrayListFromArguments(getArguments());

        view = binding.getRoot();
    }

    private void setObservers() {
        databaseModeViewModel.getDatabaseMode().observe(getViewLifecycleOwner(),
                productsPhotoViewModel::setDatabaseModeAndShowPhoto);
    }

    private void setListeners(){
        binding.buttonTakePhoto.setOnClickListener(this::onClickTakePhoto);
        binding.buttonSavePhoto.setOnClickListener(this::onClickSavePhoto);
        binding.buttonDeletePhoto.setOnClickListener(this::onClickDeletePhoto);
    }

    private void getArgumentsAndSetProductArrayList() {
        productsPhotoViewModel.setProductArrayListFromArguments(getArguments());
    }

    private void onClickTakePhoto(View view){
        requestPermissionsToTakePhoto();
    }

    private void requestPermissionsToTakePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            requestPermissionAboveApi28();
        else
            requestPermissionEqualAndBelowApi28();
    }

    private void requestPermissionEqualAndBelowApi28() {
        String[] permissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(requireActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireActivity(), permissions[1]) == PackageManager.PERMISSION_GRANTED)
            productsPhotoViewModel.permissionGranted();
        else if(shouldShowRequestPermissionRationale(permissions[0]))
            showDialogInfoPermissionIsNeeded();
        else
            ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQUEST_CODE);
    }

    private void requestPermissionAboveApi28() {
        @SuppressLint("InlinedApi")
        String[] permissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(requireActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireActivity(), permissions[1]) == PackageManager.PERMISSION_GRANTED)
            productsPhotoViewModel.permissionGranted();
        else if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissions[0]) || ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissions[1]))
            showDialogInfoPermissionIsNeeded();
        else
            ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQUEST_CODE);
    }

    public void onClickSavePhoto(View view){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.imageviewPhoto.getDrawable();
        Bitmap bitmap = null;
        if(bitmapDrawable != null) {
            bitmap = bitmapDrawable.getBitmap();
        }
        String photoDescription = Objects.requireNonNull(binding.edittextPhotoDescription.getText()).toString();
        productsPhotoViewModel.savePhoto(bitmap, photoDescription);
        Toast.makeText(requireActivity(), getText(R.string.products_photo_add_photo_successful), Toast.LENGTH_LONG).show();
    }

    public void onClickDeletePhoto(View view){
        productsPhotoViewModel.deletePhoto();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                productsPhotoViewModel.permissionGranted();
            return;
        }
        throw new IllegalStateException("Unexpected value: " + requestCode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK){
            File photoFile = productsPhotoViewModel.getPhotoFile();
            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            Matrix m = new Matrix();
            m.postRotate(ImageRotation.isNeededRotation(photoFile));

            takenImage = Bitmap.createBitmap(takenImage,
                    0, 0, takenImage.getWidth(), takenImage.getHeight(),
                    m, true);

            binding.imageviewPhoto.setImageBitmap(takenImage);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            takenImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openAppSettings() {
        Intent settingsIntent = SettingsIntent.getSettingsIntent(requireActivity());
        startActivity(settingsIntent);
    }

    @Override
    public void takePhotoIntent(File photoFile) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileProvider = FileProvider.getUriForFile(requireContext(), "com.hermanowicz.pantry.provider", photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_CODE);
        } else
            Toast.makeText(requireActivity(), getText(R.string.error_no_camera_access), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPhoto(Product product, Bitmap bitmap) {
        binding.imageviewPhoto.setImageBitmap(bitmap);
    }

    @Override
    public void showDescription(String photoDescription) {
        binding.edittextPhotoDescription.setText(photoDescription);
    }

    @Override
    public void observeAvailableData() {
        productsPhotoViewModel.getPhotoList().observe(getViewLifecycleOwner(),
                productsPhotoViewModel::setCurrentPhotoList);
    }
}