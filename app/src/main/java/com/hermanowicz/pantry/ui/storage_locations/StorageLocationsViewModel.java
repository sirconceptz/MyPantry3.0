package com.hermanowicz.pantry.ui.storage_locations;

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
import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.interfaces.AvailableDataListener;
import com.hermanowicz.pantry.model.DatabaseMode;

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
public class StorageLocationsViewModel extends ViewModel {

    @Inject
    StorageLocationsUseCaseImpl useCase;
    private final String TAG = "RxJava-Categories";
    private LiveData<List<StorageLocation>> storageLocationListLiveData;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private AvailableDataListener availableDataListener;

    @Inject
    public StorageLocationsViewModel(StorageLocationsUseCaseImpl storageLocationUseCase) {
        useCase = storageLocationUseCase;
        loadOnlineStorageLocations();
    }

    public LiveData<List<StorageLocation>> getAllStorageLocationList() {
        return storageLocationListLiveData;
    }

    private void loadOnlineStorageLocations() {
        CompositeDisposable disposables = new CompositeDisposable();
        disposables.add(storageLocationList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<StorageLocation>>() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError()", e);
                    }

                    @Override
                    public void onNext(@NonNull List<StorageLocation> storageLocationList) {
                        Log.i(TAG, "onNext()");
                    }
                }));
    }

    private Observable<List<StorageLocation>> storageLocationList() {
        return Observable.create(emitter -> {
            String user = FirebaseAuth.getInstance().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            if(user != null && isInternetConnection()) {
                Query query = database.getReference().child("storage_locations").child(user);
                query.addValueEventListener(valueEventListener(emitter));
            } else {
                useCase.setOnlineStorageLocationList(new MutableLiveData<>());
                availableDataListener.observeAvailableData();
            }
        });
    }

    private ValueEventListener valueEventListener (Emitter<List<StorageLocation>> emitter){
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<StorageLocation> list = new ArrayList<>();
                Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();

                for (DataSnapshot dataSnapshot : snapshotIterable) {
                    StorageLocation storageLocation = dataSnapshot.getValue(StorageLocation.class);
                    list.add(storageLocation);
                }
                emitter.onNext(list);
                MutableLiveData<List<StorageLocation>> tempStorageLocationList = new MutableLiveData<>(list);
                useCase.setOnlineStorageLocationList(tempStorageLocationList);
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

    public void updateDataForSelectedDatabase(DatabaseMode databaseMode) {
        storageLocationListLiveData = useCase.getAllStorageLocations(databaseMode);
    }

    public StorageLocation getStorageLocation(int id) {
        return Objects.requireNonNull(storageLocationListLiveData.getValue()).get(id);
    }

    public boolean isAvailableData(){
        return storageLocationListLiveData != null;
    }

    private boolean isInternetConnection(){
        return useCase.checkIsInternetConnection();
    }

    public void setDefaultDatabaseMode(DatabaseMode databaseModeFromSettings) {
        if(databaseModeFromSettings.getDatabaseMode() == DatabaseMode.Mode.LOCAL){
            updateDataForSelectedDatabase(databaseModeFromSettings);
            availableDataListener.observeAvailableData();
        }
    }
}