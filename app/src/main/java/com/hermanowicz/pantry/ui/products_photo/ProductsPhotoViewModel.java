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
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hermanowicz.pantry.data.db.dao.photo.Photo;
import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.interfaces.PhotoEditViewActions;
import com.hermanowicz.pantry.interfaces.ShowPhotoViewActions;
import com.hermanowicz.pantry.model.DatabaseMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ProductsPhotoViewModel extends ViewModel {

    private final String TAG = "RxJava-Photos";
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final DisposableObserver<List<Photo>> disposableObserver = new DisposableObserver<>() {
        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete()");
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.e(TAG, "onError()", e);
        }

        @Override
        public void onNext(@NonNull List<Photo> productList) {
            Log.i(TAG, "onNext()");
        }
    };
    @Inject
    ProductsPhotoUseCaseImpl useCase;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private LiveData<List<Photo>> photoList;
    private AvailableDataListener availableDataListener;
    private PhotoEditViewActions photoEditViewActions;
    private ShowPhotoViewActions showPhotoViewActions;
    private boolean observed = false;

    @Inject
    public ProductsPhotoViewModel(ProductsPhotoUseCaseImpl productsPhotoUseCase) {
        useCase = productsPhotoUseCase;
        loadOnlineProducts();
    }

    public void deletePhoto() {
        useCase.deletePhoto(productArrayList);
    }

    public void savePhoto(Bitmap bitmap, String photoDescription) {
        useCase.savePhoto(bitmap, photoDescription, productArrayList);
    }

    public void setDatabaseModeAndShowPhoto(DatabaseMode databaseMode) {
        useCase.setDatabaseMode(databaseMode);
        photoList = useCase.getPhotoList(databaseMode);
        showPhoto();
    }

    public void setProductArrayListFromArguments(@Nullable Bundle arguments) {
        if (arguments != null)
            productArrayList = arguments.getParcelableArrayList("productArrayList");
    }

    public void permissionGranted() {
        useCase.createPhotoFile();
        File photoFile = useCase.getPhotoFile();
        photoEditViewActions.takePhotoIntent(photoFile);
    }

    public void setViewActionsListeners(PhotoEditViewActions photoEditViewActions, ShowPhotoViewActions showPhotoViewActions) {
        this.photoEditViewActions = photoEditViewActions;
        this.showPhotoViewActions = showPhotoViewActions;
    }

    public File getPhotoFile() {
        return useCase.getPhotoFile();
    }

    private void showPhoto() {
        int productListSize = productArrayList.size();
        if (productListSize > 0) {
            Product product = productArrayList.get(0);
            String photoDescription = product.getPhotoDescription();
            String photoName = product.getPhotoName();
            if (!photoName.equals("")) {
                useCase.setPhotoFile(photoName);
                Bitmap bitmap = useCase.getPhotoBitmap();
                showPhotoViewActions.showPhoto(product, bitmap);
                photoEditViewActions.showDescription(photoDescription);
            }
        }
    }

    private void loadOnlineProducts() {
        Disposable photoDisposable = photoList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableObserver);
        disposable.add(photoDisposable);
    }

    private Observable<List<Photo>> photoList() {
        return Observable.create(emitter -> {
            String user = FirebaseAuth.getInstance().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            if (user != null && isInternetConnection()) {
                Query query = database.getReference().child("photos").child(user);
                query.addValueEventListener(valueEventListener(emitter));
            } else {
                useCase.setOnlinePhotoList(new MutableLiveData<>());
                availableDataListener.observeAvailableData();
                observed = true;
            }
        });
    }

    private ValueEventListener valueEventListener(Emitter<List<Photo>> emitter) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Photo> list = new ArrayList<>();
                Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();

                for (DataSnapshot dataSnapshot : snapshotIterable) {
                    Photo photo = dataSnapshot.getValue(Photo.class);
                    list.add(photo);
                }
                emitter.onNext(list);

                MutableLiveData<List<Photo>> tempPhotoList = new MutableLiveData<>(list);
                useCase.setOnlinePhotoList(tempPhotoList);
                availableDataListener.observeAvailableData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                emitter.onError(new FirebaseException(error.getMessage()));
            }
        };
    }

    public ArrayList<Product> getProductArrayList() {
        return productArrayList;
    }

    private boolean isInternetConnection() {
        return useCase.checkIsInternetConnection();
    }

    public void setAvailableDataListener(AvailableDataListener availableDataListener) {
        if (availableDataListener == null && !observed)
            loadOnlineProducts();
        this.availableDataListener = availableDataListener;
    }

    public LiveData<List<Photo>> getPhotoList() {
        return photoList;
    }

    public void setCurrentPhotoList(List<Photo> photos) {
        useCase.setCurrentPhotoList(photos);
    }
}