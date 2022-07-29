package com.hermanowicz.pantry.ui.storage_locations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class StorageLocationsViewModel extends ViewModel {

    @Inject
    StorageLocationsUseCaseImpl useCase;
    private LiveData<List<StorageLocation>> storageLocationList;

    @Inject
    public StorageLocationsViewModel(StorageLocationsUseCaseImpl storageLocationUseCase) {
        useCase = storageLocationUseCase;
        storageLocationList = useCase.getAllStorageLocations();
    }

    public LiveData<List<StorageLocation>> getAllStorageLocationList(){
        return storageLocationList;
    }

    public StorageLocation getStorageLocation(int id) {
        List<StorageLocation> storageLocations = storageLocationList.getValue();
        assert storageLocations != null;
        return storageLocations.get(id);
    }
}