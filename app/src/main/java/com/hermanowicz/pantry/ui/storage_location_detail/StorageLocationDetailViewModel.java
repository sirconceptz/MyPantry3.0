package com.hermanowicz.pantry.ui.storage_location_detail;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.model.DatabaseMode;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class StorageLocationDetailViewModel extends ViewModel {

    private final StorageLocationDetailUseCaseImpl useCase;

    private StorageLocation storageLocation = new StorageLocation();
    public ObservableField<String> storageLocationName = new ObservableField<>();
    public ObservableField<String> storageLocationDescription = new ObservableField<>();

    @Inject
    public StorageLocationDetailViewModel(StorageLocationDetailUseCaseImpl storageLocationDetailUseCase) {
        useCase = storageLocationDetailUseCase;
    }

    public void showStorageLocationData(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
        storageLocationName.set(storageLocation.getName());
        storageLocationDescription.set(storageLocation.getDescription());
    }

    private StorageLocation getUpdatedStorageLocation() {
        storageLocation.setName(storageLocationName.get());
        storageLocation.setDescription(storageLocationDescription.get());
        return storageLocation;
    }

    public void onClickUpdateStorageLocation() {
        StorageLocation storageLocation = getUpdatedStorageLocation();
        useCase.updateStorageLocation(storageLocation);
    }

    public void onClickDeleteStorageLocation() {
        useCase.deleteStorageLocation(storageLocation);
    }

    public void setDatabaseMode(DatabaseMode databaseMode) {
        useCase.setDatabaseMode(databaseMode);
    }
}