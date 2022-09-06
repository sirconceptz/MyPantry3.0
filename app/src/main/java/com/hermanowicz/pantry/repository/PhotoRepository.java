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

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.dao.db.photo.Photo;

import java.io.File;
import java.util.List;

public interface PhotoRepository {
    File getPhotoFile();

    void createPhotoFile();

    String getPhotoFileName();

    void savePhotoInGallery(Bitmap bitmap, String fileName);

    Bitmap getPhotoBitmap();

    void setPhotoFileFromOfflineDb(String photoName);

    void setPhotoFileFromOnlineDb(String photoName);

    boolean checkIsInternetConnection();

    MutableLiveData<List<Photo>> getOfflinePhotoListLiveData();

    void setOfflinePhotoListLiveData(MutableLiveData<List<Photo>> offlinePhotoListLiveData);

    MutableLiveData<List<Photo>> getOnlinePhotoListLiveData();

    void setOnlinePhotoListLiveData(MutableLiveData<List<Photo>> onlinePhotoListLiveData);
}