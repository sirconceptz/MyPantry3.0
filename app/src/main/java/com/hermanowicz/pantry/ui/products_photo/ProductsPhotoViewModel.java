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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hermanowicz.pantry.dao.db.photo.Photo;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.interfaces.ProductPhotoViewActions;
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

    @Inject
    ProductsPhotoUseCaseImpl useCase;
    private final String TAG = "RxJava-Photos";
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private AvailableDataListener availableDataListener;
    private ProductPhotoViewActions viewActionsListener;

    @Inject
    public ProductsPhotoViewModel(ProductsPhotoUseCaseImpl productsPhotoUseCase){
        useCase = productsPhotoUseCase;
        loadOnlineProducts();
    }

    public void deletePhoto(){
        useCase.deletePhoto(productArrayList);
    }

    public void savePhoto(Bitmap bitmap, String photoDescription) {
        useCase.savePhoto(bitmap, photoDescription, productArrayList);
    }

    public void setDatabaseModeAndShowPhoto(DatabaseMode databaseMode){
        useCase.setDatabaseMode(databaseMode);
        showPhoto();
    }

    public void setProductArrayListFromArguments(@Nullable Bundle arguments) {
        if(arguments != null)
            productArrayList = arguments.getParcelableArrayList("productArrayList");
    }

    public void permissionGranted() {
        useCase.createPhotoFile();
        File photoFile = useCase.getPhotoFile();
        viewActionsListener.takePhotoIntent(photoFile);
    }

    public void setViewActionsListener(ProductPhotoViewActions viewActionsListener) {
        this.viewActionsListener = viewActionsListener;
    }

    public File getPhotoFile() {
        return useCase.getPhotoFile();
    }

    private void showPhoto() {
        Product product = productArrayList.get(0);
        String photoDescription = product.getPhotoDescription();
        String photoName = product.getPhotoName();
        if(!photoName.equals("")) {
            useCase.setPhotoFile(photoName);
            Bitmap bitmap = useCase.getPhotoBitmap();
            viewActionsListener.showPhoto(product, bitmap);
            viewActionsListener.showDescription(photoDescription);
        }
    }

    private void loadOnlineProducts() {
        Disposable photoDisposable =photoList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableObserver);
        disposable.add(photoDisposable);
    }

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

    private Observable<List<Photo>> photoList() {
        return Observable.create(emitter -> {
            String user = FirebaseAuth.getInstance().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            if(user != null && isInternetConnection()) {
                Query query = database.getReference().child("photos").child(user);
                query.addValueEventListener(valueEventListener(emitter));
            } else{
                useCase.setOnlinePhotoList(new MutableLiveData<>());
                availableDataListener.observeAvailableData();
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
        this.availableDataListener = availableDataListener;
    }
}