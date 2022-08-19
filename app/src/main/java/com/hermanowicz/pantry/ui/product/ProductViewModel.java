package com.hermanowicz.pantry.ui.product;

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
import com.hermanowicz.pantry.model.Database;
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
    ProductUseCaseImpl productUseCase;
    private final String TAG = "RxJava-Products";
    private LiveData<List<Product>> productListLiveData;
    private final MutableLiveData<List<GroupProduct>> groupProductList = new MutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private AvailableDataListener availableDataListener;

    @Inject
    public ProductViewModel(ProductUseCaseImpl productUseCase) {
        this.productUseCase = productUseCase;
        loadOnlineProducts();
    }

    public GroupProduct getGroupProduct(int position) {
        return Objects.requireNonNull(groupProductList.getValue()).get(position);
    }

    public void convertProductListToGroupProductList(List<Product> productList) {
        List<GroupProduct> groupProducts = productUseCase.getAllGroupProductList(productList);
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
            if(user != null && productUseCase.checkIsInternetConnection()) {
                Query query = database.getReference().child("products").child(user);
                query.addValueEventListener(valueEventListener(emitter));
            }
            else{
                productUseCase.setOnlineProductList(new MutableLiveData<>());
                availableDataListener.observeAvailableData();
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
                productUseCase.setOnlineProductList(tempProductList);
                availableDataListener.observeAvailableData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                emitter.onError(new FirebaseException(error.getMessage()));
            }
        };
    }

    public void updateDataForSelectedDatabase(@NonNull Database databaseMode) {
        productListLiveData = productUseCase.getAllProducts(databaseMode);
    }

    public void clearDisposable() {
        disposable.clear();
    }

    public void setAvailableDataListener(AvailableDataListener listener) {
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

    public boolean isInternetConnection() {
        return productUseCase.checkIsInternetConnection();
    }
}