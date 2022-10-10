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

package com.hermanowicz.pantry.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.hermanowicz.pantry.dao.db.photo.Photo;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.util.DateHelper;
import com.hermanowicz.pantry.util.InternetConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhotoRepositoryImpl implements PhotoRepository {

    private File photoFile;
    private final String filePath;
    private String fileName;
    private Bitmap photoBitmap;
    private final ContentResolver contentResolver;
    private List<Photo> currentPhotoList;
    private final InternetConnection internetConnection;
    private MutableLiveData<List<Photo>> onlinePhotoListLiveData;

    public PhotoRepositoryImpl(Context context){
        filePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES
                + File.separator + "MyPantry").getPath();
        contentResolver = context.getContentResolver();
        internetConnection = new InternetConnection(context);
    }

    private String getExternalAbsolutePath() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            return Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + "MyPantry";
        else
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }

    @Override
    public File getPhotoFile() {
        return photoFile;
    }

    @Override
    public void createPhotoFile() {
        fileName = "JPEG_" + DateHelper.getTimeStamp();
        try {
            photoFile = File.createTempFile(fileName, ".jpg", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPhotoFileName() {
        return fileName;
    }

    @Override
    public void savePhotoInGallery(Bitmap bitmap, String fileName) {
        try {
            savePhotoIfEqualOrAboveAPI28(bitmap, fileName);
            savePhotoIfBelowAPI28(bitmap, fileName);
        } catch (Exception e) {
            Log.e("Error on insert photo", e.getMessage());
        }
    }

    @Override
    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    @Override
    public void setPhotoFileFromOfflineDb(String photoName) {
        String externalPath = getExternalAbsolutePath();
        photoFile = new File(externalPath + File.separator + photoName + ".jpg");
        photoBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
    }

    @Override
    public void setPhotoFileFromOnlineDb(String photoName) {
        if(currentPhotoList != null) {
            for (Photo photo : currentPhotoList) {
                if (photo.getName().equals(photoName)) {
                    byte[] decodedString = Base64.decode(photo.getContent(), Base64.DEFAULT);
                    photoBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }
            }
        }
    }

    private void savePhotoIfBelowAPI28(Bitmap bitmap, String fileName) throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    + File.separator + fileName + ".jpg", false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        }
    }

    private void savePhotoIfEqualOrAboveAPI28(Bitmap bitmap, String fileName) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            OutputStream fos;
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES
                    + File.separator + "MyPantry");
            Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = contentResolver.openOutputStream(Objects.requireNonNull(imageUri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Objects.requireNonNull(fos);
            fos.close();
        }
    }

    @Override
    public boolean checkIsInternetConnection() {
        return internetConnection.isNetworkConnected();
    }

    @Override
    public MutableLiveData<List<Photo>> getOnlinePhotoListLiveData() {
        return onlinePhotoListLiveData;
    }

    @Override
    public void setOnlinePhotoListLiveData(MutableLiveData<List<Photo>> onlinePhotoListLiveData) {
        this.onlinePhotoListLiveData = onlinePhotoListLiveData;
    }

    @Override
    public void deleteLocalPhotoFile() {
        photoFile.delete();
    }

    @Override
    public void deleteOnlinePhoto(ArrayList<Product> productArrayList) {
        DatabaseReference ref = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTablePhotos);
        for(Photo photo : currentPhotoList) {
            if(photo.getName().equals(productArrayList.get(0).getPhotoName()))
                ref.child(String.valueOf(photo.getId())).removeValue();
        }
    }

    @Override
    public void setCurrentPhotoList(List<Photo> photos) {
        currentPhotoList = photos;
    }

    @Override
    public List<Photo> getCurrentPhotoList() {
        return currentPhotoList;
    }
}