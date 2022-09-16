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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface ProductsPhotoUseCase {
    void savePhoto(Bitmap bitmap, String photoDescription, ArrayList<Product> productArrayList);

    void deletePhoto(ArrayList<Product> productArrayList);

    void setDatabaseMode(DatabaseMode databaseMode);

    File getPhotoFile();

    void createPhotoFile();

    Bitmap getPhotoBitmap();

    void setPhotoFile(String photoName);

    boolean checkIsInternetConnection();

    void setOnlinePhotoList(MutableLiveData<List<Photo>> photoList);

    LiveData<List<Photo>> getPhotoList(DatabaseMode databaseMode);

    void setCurrentPhotoList(List<Photo> photos);
}