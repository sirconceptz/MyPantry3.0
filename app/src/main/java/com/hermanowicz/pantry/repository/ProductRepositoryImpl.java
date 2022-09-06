package com.hermanowicz.pantry.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.dao.db.product.ProductDao;
import com.hermanowicz.pantry.dao.db.product.ProductDb;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.GroupProduct;
import com.hermanowicz.pantry.util.InternetConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductRepositoryImpl implements ProductRepository {

    private final ProductDao productDao;
    private final LiveData<List<Product>> localProductsList;
    private LiveData<List<Product>> onlineProductList;
    private ArrayList<Product> productListToInsert = new ArrayList<>();
    private final InternetConnection internetConnection;
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
        for(Product product : productArrayList) {
            product.setPhotoName(fileName);
            product.setPhotoDescription(photoDescription);
        }
        productDao.update(productArrayList);
    }

    @Override
    public void addOnlinePhoto(Bitmap bitmap, String photoDescription, ArrayList<Product> productArrayList, String fileName) {
        //todo: implement
    }

    @Override
    public void deleteOfflinePhoto(ArrayList<Product> productArrayList) {
        //todo: implement
    }

    @Override
    public void deleteOnlinePhoto(ArrayList<Product> productArrayList) {
        //todo: implement
    }

    @Override
    public LiveData<List<Product>> getAllLocalProducts() {
        return productDao.getAllProducts();
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
        DatabaseReference ref = DatabaseMode.getOnlineDatabaseReference("products");
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
        DatabaseReference ref = DatabaseMode.getOnlineDatabaseReference("products");
        for(Product product : productList) {
            ref.child(String.valueOf(product.getId())).setValue(product);
        }
    }

    private void deleteOnlineProducts(List<Product> productList) {
        DatabaseReference ref = DatabaseMode.getOnlineDatabaseReference("products");
        for(Product product : productList) {
            ref.child(String.valueOf(product.getId())).removeValue();
        }
    }

    private void deleteAllOnlineProducts() {
        DatabaseReference ref = DatabaseMode.getOnlineDatabaseReference("products");
        ref.removeValue();
    }

    private int getLastOnlineProductId(LiveData<List<Product>> onlineProductList) {
        int onlineProductListSize = Objects.requireNonNull(onlineProductList.getValue()).size();
        return onlineProductList.getValue().get(onlineProductListSize - 1).getId();
    }
}