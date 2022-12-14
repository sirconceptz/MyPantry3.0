package com.hermanowicz.pantry.ui.new_storage_location;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.data.db.dao.storagelocation.StorageLocation;
import com.hermanowicz.pantry.model.DatabaseMode;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NewStorageLocationViewModel extends ViewModel {

    private final NewStorageLocationUseCaseImpl useCase;

    public ObservableField<String> storageLocationName = new ObservableField<>();
    public ObservableField<String> storageLocationDescription = new ObservableField<>();

    @Inject
    public NewStorageLocationViewModel(NewStorageLocationUseCaseImpl newStorageLocationUseCase) {
        useCase = newStorageLocationUseCase;
    }

    public void onClickAddStorageLocation() {
        StorageLocation storageLocation = getStorageLocation();
        useCase.insert(storageLocation);
        clearFields();
    }

    @NonNull
    private StorageLocation getStorageLocation() {
        StorageLocation storageLocation = new StorageLocation();
        storageLocation.setName(storageLocationName.get());
        storageLocation.setDescription(storageLocationDescription.get());
        return storageLocation;
    }

    public void onClickClearFields() {
        clearFields();
    }

    private void clearFields() {
        storageLocationName.set("");
        storageLocationDescription.set("");
    }

    public void setDatabaseMode(DatabaseMode databaseMode) {
        useCase.setDatabase(databaseMode);
    }
}