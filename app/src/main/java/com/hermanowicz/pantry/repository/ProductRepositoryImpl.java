package com.hermanowicz.pantry.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.dao.db.product.ProductDao;
import com.hermanowicz.pantry.dao.db.product.ProductDb;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.GroupProduct;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductRepositoryImpl implements ProductRepository{

    private final ProductDao productDao;
    private final LiveData<List<Product>> localProductsList;

    private LiveData<List<Product>> onlineProductList = new MutableLiveData<>();

    public ProductRepositoryImpl(Context context){
        ProductDb productDb = ProductDb.getInstance(context);
        productDao = productDb.productsDao();
        localProductsList = productDao.getAllProducts();
    }

    @Override
    public LiveData<List<Product>> getAllLocalProducts(){
        return localProductsList;
    }

    @Override
    public LiveData<List<Product>> getAllOnlineProducts(){
        return onlineProductList;
    }

    @Override
    public void setOnlineProductList(MutableLiveData<List<Product>> listMutableLiveData) {
        List<Product> productList = listMutableLiveData.getValue();
        onlineProductList = new MutableLiveData<>(productList);
    }

    @Override
    public void insert(GroupProduct groupProduct, DatabaseMode databaseMode){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> insertToSelectedDatabase(groupProduct, databaseMode));
    }

    @Override
    public void update(Product[] products, DatabaseMode databaseMode){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> updateToSelectedDatabase(products, databaseMode));
    }

    @Override
    public void delete(Product[] products, DatabaseMode databaseMode){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteToSelectedDatabase(products, databaseMode));
    }

    @Override
    public void deleteAll(DatabaseMode databaseMode){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> deleteAllToSelectedDatabase(databaseMode));
    }

    private void deleteAllToSelectedDatabase(DatabaseMode databaseMode) {
        if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            productDao.deleteAll();
        else if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            deleteAllOnlineProducts();
    }

    private void insertToSelectedDatabase(GroupProduct groupProduct, DatabaseMode databaseMode) {
        if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            insertOfflineGroupProduct(groupProduct);
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            insertOnlineGroupProduct(groupProduct);
    }

    private void updateToSelectedDatabase(Product[] products, DatabaseMode databaseMode) {
        if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            productDao.update(products);
        else if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            updateOnlineProducts(products);
    }

    private void deleteToSelectedDatabase(Product[] products, DatabaseMode databaseMode) {
        if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            productDao.delete(products);
        else if(databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            deleteOnlineProducts(products);
    }

    //todo: koniecznie do refaktoryzacji
    private void insertOnlineGroupProduct(GroupProduct groupProduct) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child("products/" + FirebaseAuth.getInstance().getUid());
        getProductListToInsert();
    }

    private List<Product> getProductListToInsert() {
        int counter = 0;
        int onlineProductListSize = 0;
        if (onlineProductList.getValue() != null)
            onlineProductListSize = onlineProductList.getValue().size();
        for (Product product : onlineProductList.getValue()) {
            counter++;
            int nextId = counter;
            if (onlineProductListSize > 0)
                nextId = nextId + onlineProductList.getValue().get(onlineProductListSize - 1).getId();
            product.setId(nextId);
            ref.child(String.valueOf(nextId)).setValue(product);
        }
    }

    private void updateOnlineProducts(Product[] products) {

    }

    private void deleteOnlineProducts(Product[] products) {

    }

    private void deleteAllOnlineProducts() {

    }

    private void insertOfflineGroupProduct(GroupProduct... groupProduct){
        Product product = groupProduct[0].getProduct();
        for(int i = 0; i < groupProduct[0].getQuantity(); i++){
            productDao.insert(product);
        }
    }
}