package com.hermanowicz.pantry.ui.product;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
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
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.interfaces.FilteredDataListener;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.FilterModel;
import com.hermanowicz.pantry.model.GroupProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class ProductViewModel extends ViewModel {

    @Inject
    ProductUseCaseImpl useCase;
    private final String TAG = "RxJava-Products";
    private LiveData<List<Product>> productListLiveData;
    private final MutableLiveData<List<GroupProduct>> groupProductList = new MutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private AvailableDataListener availableDataListener;
    private FilteredDataListener filteredDataListener;
    private LiveData<List<Product>> filteredProductsLiveData;
    private boolean observed = false;

    @Inject
    public ProductViewModel(ProductUseCaseImpl productUseCase) {
        this.useCase = productUseCase;
    }

    public GroupProduct getGroupProduct(int position) {
        return Objects.requireNonNull(groupProductList.getValue()).get(position);
    }

    public void filterProductList(List<Product> productList) {
        filteredProductsLiveData = useCase.getFilteredProductList(productList);
        filteredDataListener.observeFilteredData();
    }

    public void convertProductListToGroupProductList(List<Product> productList) {
        List<GroupProduct> groupProducts = useCase.getAllGroupProductList(productList);
        groupProductList.setValue(groupProducts);
    }

    public LiveData<List<GroupProduct>> getAllGroupProductList() {
        return groupProductList;
    }

    private void loadOnlineProducts() {
        Disposable productDisposable = productList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableObserver);
        disposable.add(productDisposable);
    }

    private final DisposableObserver<List<Product>> disposableObserver = new DisposableObserver<>() {
        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete()");
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.e(TAG, "onError()", e);
        }

        @Override
        public void onNext(@NonNull List<Product> productList) {
            Log.i(TAG, "onNext()");
        }
    };

    private Observable<List<Product>> productList() {
        return Observable.create(emitter -> {
            String user = FirebaseAuth.getInstance().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            if(user != null && isInternetConnection()) {
                Query query = database.getReference().child("products").child(user);
                query.addValueEventListener(valueEventListener(emitter));
            } else {
                useCase.setOnlineProductList(new MutableLiveData<>());
                availableDataListener.observeAvailableData();
                observed = true;
            }
        });
    }

    private ValueEventListener valueEventListener(Emitter<List<Product>> emitter) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> list = new ArrayList<>();
                Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();

                for (DataSnapshot dataSnapshot : snapshotIterable) {
                    Product product = dataSnapshot.getValue(Product.class);
                    list.add(product);
                }
                emitter.onNext(list);

                MutableLiveData<List<Product>> tempProductList = new MutableLiveData<>(list);
                useCase.setOnlineProductList(tempProductList);
                availableDataListener.observeAvailableData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                emitter.onError(new FirebaseException(error.getMessage()));
            }
        };
    }

    public void updateDataForSelectedDatabase(@NonNull DatabaseMode databaseMode) {
        useCase.setDatabaseMode(databaseMode);
        productListLiveData = useCase.getAllProducts();
    }

    public void clearDisposable() {
        disposable.clear();
    }

    public void setAvailableDataListener(AvailableDataListener listener) {
        if(availableDataListener == null && !observed)
            loadOnlineProducts();
        availableDataListener = listener;
    }

    public LiveData<List<Product>> getAllProductList() {
        return productListLiveData;
    }

    public ArrayList<Product> getProductSimilarProductsList(int position) {
        Product product = getGroupProduct(position).getProduct();
        List<Product> productList = getAllProductList().getValue();
        assert productList != null;
        List<Product> similarProducts = Product.getSimilarProductsList(product, productList);
        return new ArrayList<>(similarProducts);
    }

    public boolean isAvailableData(){
        return productListLiveData != null;
    }

    private boolean isInternetConnection() {
        return useCase.checkIsInternetConnection();
    }

    public void setDefaultDatabaseMode(DatabaseMode databaseModeFromSettings) {
        if(databaseModeFromSettings.getDatabaseMode() == DatabaseMode.Mode.LOCAL){
            updateDataForSelectedDatabase(databaseModeFromSettings);
            availableDataListener.observeAvailableData();
            observed = true;
        }
    }

    public void setFilterModel(FilterModel filterModel) {
        useCase.setFilterModel(filterModel);
    }

    public void setFilterModelFromArguments(Bundle arguments) {
        if(arguments != null) {
            FilterModel filterModel = arguments.getParcelable("filterModel");
            if(filterModel != null)
                setFilterModel(filterModel);
        }
    }

    public LiveData<List<Product>> getFilteredProductsLiveData() {
        return filteredProductsLiveData;
    }

    public void setFilteredDataListener(FilteredDataListener filteredDataListener) {
        this.filteredDataListener = filteredDataListener;
    }
}