package com.hermanowicz.pantry.ui.storage_location_detail;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;

interface StorageLocationDetailUseCase {
    void deleteStorageLocation(StorageLocation storageLocation);
    void updateStorageLocation(StorageLocation storageLocation);
}