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

package com.hermanowicz.pantry.data.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.hermanowicz.pantry.data.db.dao.photo.Photo;
import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.data.db.dao.product.ProductDao;
import com.hermanowicz.pantry.data.db.dao.product.ProductDb;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.GroupProduct;
import com.hermanowicz.pantry.util.InternetConnection;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductRepositoryImpl implements ProductRepository {

    private final ProductDao productDao;
    private final LiveData<List<Product>> localProductsList;
    private final InternetConnection internetConnection;
    private LiveData<List<Product>> onlineProductList;
    private ArrayList<Product> productListToInsert = new ArrayList<>();
    private List<GroupProduct> groupProductList = new ArrayList<>();

    public ProductRepositoryImpl(Context context) {
        ProductDb productDb = ProductDb.getInstance(context);
        productDao = productDb.productsDao();
        localProductsList = productDao.getAllProducts();
        internetConnection = new InternetConnection(context);
    }

    @Override
    public LiveData<List<Product>> getAllProducts(DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            return onlineProductList;
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            return localProductsList;
        else
            return new MutableLiveData<>();
    }

    @Override
    public ArrayList<Product> getProductListToInsert() {
        return productListToInsert;
    }

    @Override
    public void setOnlineProductList(MutableLiveData<List<Product>> listMutableLiveData) {
        List<Product> productList = listMutableLiveData.getValue();
        onlineProductList = new MutableLiveData<>(productList);
    }

    @Override
    public int getIntValueFromObservableField(ObservableField<String> observableField) {
        int value = 0;
        try {
            value = Integer.parseInt(Objects.requireNonNull(observableField.get()));
        } catch (NullPointerException | NumberFormatException e) {
            Log.e("Parse error:", e.toString());
        }
        return value;
    }

    @Override
    public boolean checkIsInternetConnection() {
        return internetConnection.isNetworkConnected();
    }

    @Override
    public List<Product> getAllLocalProductsAsList() {
        return productDao.getAllProductsAsList();
    }

    @Override
    public void addOfflinePhoto(String photoDescription, ArrayList<Product> productArrayList, String fileName) {
        for (Product product : productArrayList) {
            product.setPhotoName(fileName);
            product.setPhotoDescription(photoDescription);
        }
        productDao.update(productArrayList);
    }

    @Override
    public void addOnlinePhoto(Bitmap bitmap, String photoDescription, List<Product> productList, List<Photo> currentPhotoList) {
        String encodedImage = "";
        String photoName = "";
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] byteArray = baos.toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            photoName = String.valueOf(encodedImage.hashCode());
        }
        DatabaseReference ref = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTablePhotos);
        int photoId = productList.get(0).getId();
        for (Photo photo : currentPhotoList) {
            if (photo.getName().equals(productList.get(0).getPhotoName()))
                ref.child(String.valueOf(photo.getId())).removeValue();
        }
        Photo newPhoto = new Photo();
        newPhoto.setId(photoId);
        newPhoto.setName(String.valueOf(encodedImage.hashCode()));
        newPhoto.setContent(encodedImage);
        ref.child(String.valueOf(newPhoto.getId())).setValue(newPhoto);

        ref = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTableProducts);
        for (Product product : productList) {
            if (encodedImage.length() > 0)
                product.setPhotoName(photoName);
            product.setPhotoDescription(photoDescription);
            ref.child(String.valueOf(product.getId())).setValue(product);
        }
    }

    @Override
    public void deleteOfflinePhoto(ArrayList<Product> productArrayList) {
        deletePhotoData(productArrayList);
    }

    @Override
    public void deleteOnlinePhoto(ArrayList<Product> productArrayList) {
        deletePhotoData(productArrayList);
    }

    private void deletePhotoData(List<Product> productList) {
        for (Product product : productList) {
            product.setPhotoName("");
            product.setPhotoDescription("");
        }
    }

    @Override
    public String[] getGroupProductNames(ArrayList<Product> productArrayList) {
        return GroupProduct.getGroupProductNamesArray(productArrayList);
    }

    @Override
    public List<GroupProduct> getGroupProductListFromProductList(ArrayList<Product> productArrayList) {
        groupProductList = GroupProduct.getGroupProducts(productArrayList);
        return groupProductList;
    }

    @Override
    public List<GroupProduct> getGroupProductList() {
        return groupProductList;
    }

    @Override
    public void insert(List<Product> productList, DatabaseMode databaseMode) {
        productListToInsert = new ArrayList<>(productList);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> insertToSelectedDatabase(productList, databaseMode));
    }

    @Override
    public void update(List<Product> productList, DatabaseMode databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> updateToSelectedDatabase(productList, databaseMode));
    }

    @Override
    public void delete(List<Product> productList, DatabaseMode databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteToSelectedDatabase(productList, databaseMode));
    }

    @Override
    public void deleteAll(DatabaseMode databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteAllToSelectedDatabase(databaseMode));
    }

    private void deleteAllToSelectedDatabase(DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            productDao.deleteAll();
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            deleteAllOnlineProducts();
    }

    private void insertToSelectedDatabase(List<Product> productList, DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            insertLocalProducts(productList);
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            insertOnlineProductList(productList);
    }

    private void updateToSelectedDatabase(List<Product> productList, DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            updateLocalProducts(productList);
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            updateOnlineProducts(productList);
    }

    private void updateLocalProducts(List<Product> productList) {
        productDao.update(productList);
    }

    private void deleteToSelectedDatabase(List<Product> productList, DatabaseMode databaseMode) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            deleteLocalProducts(productList);
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            deleteOnlineProducts(productList);
    }

    private void deleteLocalProducts(List<Product> productList) {
        productDao.delete(productList);
    }

    private void insertLocalProducts(List<Product> productList) {
        productDao.insert(productList);
    }

    private void insertOnlineProductList(List<Product> productList) {
        DatabaseReference ref = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTableProducts);
        insertOnlineProductList(productList, ref);
    }

    private void insertOnlineProductList(List<Product> productList, DatabaseReference ref) {
        int counter = 0;
        if (productList.size() > 0) {
            for (Product product : productList) {
                counter++;
                int nextId = counter + getLastOnlineProductId(onlineProductList);
                product.setId(nextId);
                ref.child(String.valueOf(nextId)).setValue(product);
            }
        }
    }

    private void updateOnlineProducts(List<Product> productList) {
        DatabaseReference ref = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTableProducts);
        for (Product product : productList) {
            ref.child(String.valueOf(product.getId())).setValue(product);
        }
    }

    private void deleteOnlineProducts(List<Product> productList) {
        DatabaseReference ref = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTableProducts);
        for (Product product : productList) {
            ref.child(String.valueOf(product.getId())).removeValue();
        }
    }

    private void deleteAllOnlineProducts() {
        DatabaseReference ref = DatabaseMode.getOnlineDatabaseReference(DatabaseMode.dbTableProducts);
        ref.removeValue();
    }

    private int getLastOnlineProductId(LiveData<List<Product>> onlineProductList) {
        int onlineProductListSize = Objects.requireNonNull(onlineProductList.getValue()).size();
        return onlineProductList.getValue().get(onlineProductListSize - 1).getId();
    }
}