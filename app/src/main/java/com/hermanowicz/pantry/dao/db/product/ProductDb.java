/*
 * Copyright (c) 2019-2021
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.dao.db.product;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * <h1>ProductDb/h1>
 * Product database class
 *
 * @author Mateusz Hermanowicz
 */

@Database(entities = {Product.class}, version = 6)
public abstract class ProductDb extends RoomDatabase {

    public abstract ProductDao productsDao();

    private static ProductDb INSTANCE;
    private static final Object sLock = new Object();

    public static ProductDb getInstance(@NonNull Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                ProductDb.class, "ProductDb.db")
                        .allowMainThreadQueries()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                            }
                        })
                        .addMigrations(ALL_MIGRATIONS)
                        .build();
            }
            return INSTANCE;
        }
    }

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE products RENAME COLUMN typeOfProduct TO mainCategory");
            database.execSQL(
                    "ALTER TABLE products RENAME COLUMN productFeatures TO detailCategory");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE products ADD COLUMN barcode TEXT DEFAULT ''");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE products ADD COLUMN photoName TEXT DEFAULT ''");
            database.execSQL(
                    "ALTER TABLE products ADD COLUMN photoDescription TEXT DEFAULT ''");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE products ADD COLUMN storageLocation TEXT DEFAULT ''");
        }
    };

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE products ADD COLUMN isVege INTEGER NOT NULL DEFAULT '0'");
            database.execSQL(
                    "ALTER TABLE products ADD COLUMN isBio INTEGER NOT NULL DEFAULT '0'");
        }
    };

    private static final Migration[] ALL_MIGRATIONS = new Migration[]{
            MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6};
}
