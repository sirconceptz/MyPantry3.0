package com.hermanowicz.pantry.ui.category;

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
import com.hermanowicz.pantry.dao.db.category.Category;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.model.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class CategoryViewModel extends ViewModel {

    @Inject
    CategoryUseCaseImpl useCase;
    private final String TAG = "RxJava-Categories";
    private LiveData<List<Category>> categoryAllListLiveData;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private AvailableDataListener availableDataListener;

    @Inject
    public CategoryViewModel(CategoryUseCaseImpl categoryUseCase) {
        useCase = categoryUseCase;
        loadOnlineCategories();
    }

    public LiveData<List<Category>> getCategoryList() {
        return categoryAllListLiveData;
    }

    private void loadOnlineCategories() {
        CompositeDisposable disposables = new CompositeDisposable();
        disposables.add(categoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Category>>() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError()", e);
                    }

                    @Override
                    public void onNext(@NonNull List<Category> categoryList) {
                        Log.i(TAG, "onNext()");
                    }
                }));
    }

    private Observable<List<Category>> categoryList() {
        return Observable.create(emitter -> {
            String user = FirebaseAuth.getInstance().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            if(user != null && isInternetConnection()) {
                Query query = database.getReference().child("categories").child(user);
                query.addValueEventListener(valueEventListener(emitter));}
            else{
                useCase.setOnlineCategoryList(new MutableLiveData<>());
                availableDataListener.observeAvailableData();
            }
        });
    }

    private ValueEventListener valueEventListener(Emitter<List<Category>> emitter) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> list = new ArrayList<>();
                Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();

                for (DataSnapshot dataSnapshot : snapshotIterable) {
                    Category category = dataSnapshot.getValue(Category.class);
                    list.add(category);
                }
                emitter.onNext(list);

                MutableLiveData<List<Category>> tempCategoryList = new MutableLiveData<>(list);
                useCase.setOnlineCategoryList(tempCategoryList);
                availableDataListener.observeAvailableData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                emitter.onError(new FirebaseException(error.getMessage()));
            }
        };
    }

    public void clearDisposable() {
        disposable.clear();
    }

    public void setAvailableDataListener(AvailableDataListener listener) {
        availableDataListener = listener;
    }

    public void updateDataForSelectedDatabase(Database databaseMode) {
        categoryAllListLiveData = useCase.getAllCategories(databaseMode);
    }

    public Category getCategory(int position) {
        return Objects.requireNonNull(categoryAllListLiveData.getValue()).get(position);
    }

    public boolean isAvailableData(){
        return categoryAllListLiveData != null;
    }

    private boolean isInternetConnection(){
        return useCase.checkIsInternetConnection();
    }

    public void setDefaultDatabaseMode(Database databaseModeFromSettings) {
        if(databaseModeFromSettings.getDatabaseMode() == Database.DatabaseMode.LOCAL){
            updateDataForSelectedDatabase(databaseModeFromSettings);
            availableDataListener.observeAvailableData();
        }
    }
}