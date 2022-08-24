package com.hermanowicz.pantry.repository;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.dao.db.product.ProductDao;
import com.hermanowicz.pantry.dao.db.product.ProductDb;
import com.hermanowicz.pantry.model.Database;
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

    public ProductRepositoryImpl(Context context) {
        ProductDb productDb = ProductDb.getInstance(context);
        productDao = productDb.productsDao();
        localProductsList = productDao.getAllProducts();
        internetConnection = new InternetConnection(context);
    }

    @Override
    public LiveData<List<Product>> getAllProducts(Database databaseMode) {
        if (databaseMode.getDatabaseMode() == Database.DatabaseMode.ONLINE)
            return onlineProductList;
        else if (databaseMode.getDatabaseMode() == Database.DatabaseMode.LOCAL)
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
    public void insert(List<Product> productList, Database databaseMode) {
        productListToInsert = new ArrayList<>(productList);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> insertToSelectedDatabase(productList, databaseMode));
    }

    @Override
    public void update(List<Product> productList, Database databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> updateToSelectedDatabase(productList, databaseMode));
    }

    @Override
    public void delete(List<Product> productList, Database databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteToSelectedDatabase(productList, databaseMode));
    }

    @Override
    public void deleteAll(Database databaseMode) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteAllToSelectedDatabase(databaseMode));
    }

    private void deleteAllToSelectedDatabase(Database databaseMode) {
        if (databaseMode.getDatabaseMode() == Database.DatabaseMode.LOCAL)
            productDao.deleteAll();
        else if (databaseMode.getDatabaseMode() == Database.DatabaseMode.ONLINE)
            deleteAllOnlineProducts();
    }

    private void insertToSelectedDatabase(List<Product> productList, Database databaseMode) {
        if (databaseMode.getDatabaseMode() == Database.DatabaseMode.LOCAL)
            insertLocalProducts(productList);
        else if (databaseMode.getDatabaseMode() == Database.DatabaseMode.ONLINE)
            insertOnlineProductList(productList);
    }

    private void updateToSelectedDatabase(List<Product> productList, Database databaseMode) {
        if (databaseMode.getDatabaseMode() == Database.DatabaseMode.LOCAL)
            updateLocalProducts(productList);
        else if (databaseMode.getDatabaseMode() == Database.DatabaseMode.ONLINE)
            updateOnlineProducts(productList);
    }

    private void updateLocalProducts(List<Product> productList) {
        productDao.update(productList);
    }

    private void deleteToSelectedDatabase(List<Product> productList, Database databaseMode) {
        if (databaseMode.getDatabaseMode() == Database.DatabaseMode.LOCAL)
            deleteLocalProducts(productList);
        else if (databaseMode.getDatabaseMode() == Database.DatabaseMode.ONLINE)
            deleteOnlineProducts(productList);
    }

    private void deleteLocalProducts(List<Product> productList) {
        productDao.delete(productList);
    }

    private void insertLocalProducts(List<Product> productList) {
        productDao.insert(productList);
    }

    private void insertOnlineProductList(List<Product> productList) {
        DatabaseReference ref = Database.getOnlineDatabaseReference("products");
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
        DatabaseReference ref = Database.getOnlineDatabaseReference("products");
        for(Product product : productList) {
            ref.child(String.valueOf(product.getId())).setValue(product);
        }
    }

    private void deleteOnlineProducts(List<Product> productList) {
        DatabaseReference ref = Database.getOnlineDatabaseReference("products");
        for(Product product : productList) {
            ref.child(String.valueOf(product.getId())).removeValue();
        }
    }

    private void deleteAllOnlineProducts() {
        DatabaseReference ref = Database.getOnlineDatabaseReference("products");
        ref.removeValue();
    }

    private int getLastOnlineProductId(LiveData<List<Product>> onlineProductList) {
        int onlineProductListSize = Objects.requireNonNull(onlineProductList.getValue()).size();
        return onlineProductList.getValue().get(onlineProductListSize - 1).getId();
    }
}