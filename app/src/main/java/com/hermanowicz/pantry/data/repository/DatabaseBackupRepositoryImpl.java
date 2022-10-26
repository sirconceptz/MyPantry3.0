/*
 * Copyright (c) 2019-2022
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.data.db.dao.category.CategoryDb;
import com.hermanowicz.pantry.data.db.dao.product.ProductDb;
import com.hermanowicz.pantry.data.db.dao.storagelocation.StorageLocationDb;
import com.hermanowicz.pantry.util.DateHelper;

import de.raphaelebner.roomdatabasebackup.core.RoomBackup;

public class DatabaseBackupRepositoryImpl implements DatabaseBackupRepository {

    private final Context context;

    public DatabaseBackupRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void backupProductDb() {
        final RoomBackup roomBackup = new RoomBackup(context);
        String currentTime = DateHelper.getTimeStamp();
        roomBackup.database(ProductDb.getInstance(context));
        roomBackup.customBackupFileName("ProductDbBackup:" + currentTime);
        roomBackup.customEncryptPassword(context.getString(R.string.database_encrypt_code));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.backupLocation(RoomBackup.BACKUP_FILE_LOCATION_EXTERNAL);
        roomBackup.maxFileCount(15);
        roomBackup.backup();
    }

    @Override
    public void backupCategoryDb() {
        final RoomBackup roomBackup = new RoomBackup(context);
        String currentTime = DateHelper.getTimeStamp();
        roomBackup.database(CategoryDb.getInstance(context));
        roomBackup.customBackupFileName("CategoryDbBackup:" + currentTime);
        roomBackup.customEncryptPassword(context.getString(R.string.database_encrypt_code));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.backupLocation(RoomBackup.BACKUP_FILE_LOCATION_EXTERNAL);
        roomBackup.maxFileCount(15);
        roomBackup.backup();
    }

    @Override
    public void backupStorageLocationDb() {
        final RoomBackup roomBackup = new RoomBackup(context);
        String currentTime = DateHelper.getTimeStamp();
        roomBackup.database(StorageLocationDb.getInstance(context));
        roomBackup.customBackupFileName("StorageLocationDbBackup:" + currentTime);
        roomBackup.customEncryptPassword(context.getString(R.string.database_encrypt_code));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.backupLocation(RoomBackup.BACKUP_FILE_LOCATION_EXTERNAL);
        roomBackup.maxFileCount(15);
        roomBackup.backup();
    }

    @Override
    public void restoreProductDb() {
        final RoomBackup roomBackup = new RoomBackup(context);
        roomBackup.database(ProductDb.getInstance(context));
        roomBackup.customEncryptPassword(context.getString(R.string.database_encrypt_code));
        roomBackup.customRestoreDialogTitle(context.getString(R.string.settings_choose_file_to_restore));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.backupLocation(RoomBackup.BACKUP_FILE_LOCATION_EXTERNAL);
        roomBackup.restore();
    }

    @Override
    public void restoreCategoryDb() {
        final RoomBackup roomBackup = new RoomBackup(context);
        roomBackup.database(CategoryDb.getInstance(context));
        roomBackup.customEncryptPassword(context.getString(R.string.database_encrypt_code));
        roomBackup.customRestoreDialogTitle(context.getString(R.string.settings_choose_file_to_restore));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.backupLocation(RoomBackup.BACKUP_FILE_LOCATION_EXTERNAL);
        roomBackup.restore();
    }

    @Override
    public void restoreStorageLocationDb() {
        final RoomBackup roomBackup = new RoomBackup(context);
        roomBackup.database(StorageLocationDb.getInstance(context));
        roomBackup.customEncryptPassword(context.getString(R.string.database_encrypt_code));
        roomBackup.customRestoreDialogTitle(context.getString(R.string.settings_choose_file_to_restore));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.backupLocation(RoomBackup.BACKUP_FILE_LOCATION_EXTERNAL);
        roomBackup.restore();
    }
}