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

package com.hermanowicz.pantry.ui.products_photo;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.photo.Photo;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.repository.PhotoRepository;
import com.hermanowicz.pantry.repository.ProductRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ProductsPhotoUseCaseImpl implements ProductsPhotoUseCase {

    private final ProductRepository productRepository;
    private final PhotoRepository photoRepository;
    private DatabaseMode databaseMode;

    @Inject
    public ProductsPhotoUseCaseImpl(ProductRepository productRepository, PhotoRepository photoRepository){
        this.productRepository = productRepository;
        this.photoRepository = photoRepository;
    }

    @Override
    public void savePhoto(Bitmap bitmap, String photoDescription, ArrayList<Product> productArrayList) {
        String fileName = photoRepository.getPhotoFileName();
        if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL) {
            photoRepository.savePhotoInGallery(bitmap, fileName);
            productRepository.addOfflinePhoto(photoDescription, productArrayList, fileName);
        }
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE) {
            List<Photo> currentPhotoList = photoRepository.getCurrentPhotoList();
            productRepository.addOnlinePhoto(bitmap, photoDescription, productArrayList, currentPhotoList);
        }
    }

    @Override
    public void deletePhoto(ArrayList<Product> productArrayList) {
        if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL) {
            productRepository.deleteOfflinePhoto(productArrayList);
            photoRepository.deleteLocalPhotoFile();
        }
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE) {
            productRepository.deleteOnlinePhoto(productArrayList);
            photoRepository.deleteOnlinePhoto(productArrayList);
        }
    }

    @Override
    public void setDatabaseMode(DatabaseMode databaseMode) {
        this.databaseMode = databaseMode;
    }

    @Override
    public File getPhotoFile() {
        return photoRepository.getPhotoFile();
    }

    @Override
    public void createPhotoFile() {
        photoRepository.createPhotoFile();
    }

    @Override
    public Bitmap getPhotoBitmap() {
        return photoRepository.getPhotoBitmap();
    }

    @Override
    public void setPhotoFile(String photoName) {
        if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            photoRepository.setPhotoFileFromOfflineDb(photoName);
        else if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            photoRepository.setPhotoFileFromOnlineDb(photoName);
    }

    @Override
    public boolean checkIsInternetConnection() {
        return photoRepository.checkIsInternetConnection();
    }

    @Override
    public void setOnlinePhotoList(MutableLiveData<List<Photo>> photoList) {
        photoRepository.setOnlinePhotoListLiveData(photoList);
    }

    @Override
    public LiveData<List<Photo>> getPhotoList(DatabaseMode databaseMode) {
        return photoRepository.getOnlinePhotoListLiveData();
    }

    @Override
    public void setCurrentPhotoList(List<Photo> photos) {
        photoRepository.setCurrentPhotoList(photos);
    }
}