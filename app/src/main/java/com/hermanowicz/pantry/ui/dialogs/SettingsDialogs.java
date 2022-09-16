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

package com.hermanowicz.pantry.ui.dialogs;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.hermanowicz.pantry.R;

public class SettingsDialogs {

    public static AlertDialog.Builder getBackupProductDbDialog(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.settings_backup_product_database))
                .setMessage(activity.getString(R.string.statement_backup_database))
                .setNegativeButton(activity.getString(R.string.general_no_thanks), (dialog, click) -> dialog.cancel());
        return builder;
    }

    public static AlertDialog.Builder getRestoreProductDbDialog(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.settings_restore_product_database))
                .setMessage(activity.getString(R.string.statement_restore_database))
                .setNegativeButton(activity.getString(R.string.general_no_thanks), (dialog, click) -> dialog.cancel());
        return builder;
    }

    public static AlertDialog.Builder getBackupOwnCategoriesDbDialog(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.settings_backup_category_database))
                .setMessage(activity.getString(R.string.statement_backup_database))
                .setNegativeButton(activity.getString(R.string.general_no_thanks), (dialog, click) -> dialog.cancel());
        return builder;
    }

    public static AlertDialog.Builder getRestoreOwnCategoriesDbDialog(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.settings_restore_category_database))
                .setMessage(activity.getString(R.string.statement_restore_database))
                .setNegativeButton(activity.getString(R.string.general_no_thanks), (dialog, click) -> dialog.cancel());
        return builder;
    }

    public static AlertDialog.Builder getBackupStorageLocationsDbDialog(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.settings_backup_storage_location_database))
                .setMessage(activity.getString(R.string.statement_backup_database))
                .setNegativeButton(activity.getString(R.string.general_no_thanks), (dialog, click) -> dialog.cancel());
        return builder;
    }

    public static AlertDialog.Builder getRestoreStorageLocationsDbDialog(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.settings_restore_storage_location_database))
                .setMessage(activity.getString(R.string.statement_restore_database))
                .setNegativeButton(activity.getString(R.string.general_no_thanks), (dialog, click) -> dialog.cancel());
        return builder;
    }

    public static AlertDialog.Builder getImportProductLocalDbToCloud(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.settings_import_local_db_to_cloud))
                .setMessage(activity.getString(R.string.statement_import_local_db_to_cloud_warning))
                .setNegativeButton(activity.getString(R.string.general_no_thanks), (dialog, click) -> dialog.cancel());
        return builder;
    }

    public static AlertDialog.Builder getClearProductDbDialog(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.settings_clear_product_database))
                .setMessage(activity.getString(R.string.statement_clear_database_warning))
                .setNegativeButton(activity.getString(R.string.general_no_thanks), (dialog, click) -> dialog.cancel());
        return builder;
    }

    public static AlertDialog.Builder getClearCategoryDbDialog(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.settings_clear_category_database))
                .setMessage(activity.getString(R.string.statement_clear_database_warning))
                .setNegativeButton(activity.getString(R.string.general_no_thanks), (dialog, click) -> dialog.cancel());
        return builder;
    }

    public static AlertDialog.Builder getClearStorageLocationsDbDialog(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.settings_clear_storage_location_database))
                .setMessage(activity.getString(R.string.statement_clear_database_warning))
                .setNegativeButton(activity.getString(R.string.general_no_thanks), (dialog, click) -> dialog.cancel());
        return builder;
    }
}