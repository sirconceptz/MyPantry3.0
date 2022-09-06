package com.hermanowicz.pantry.ui.new_storage_location;

import com.hermanowicz.pantry.dao.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.model.DatabaseMode;

public interface NewStorageLocationUseCase {
    void insert(StorageLocation storageLocation);

    void setDatabase(DatabaseMode databaseMode);
}