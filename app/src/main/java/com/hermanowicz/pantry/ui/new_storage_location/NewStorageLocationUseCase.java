package com.hermanowicz.pantry.ui.new_storage_location;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;

public interface NewStorageLocationUseCase {
    void insert(StorageLocation storageLocation);
}