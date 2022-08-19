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

package com.hermanowicz.pantry.dao.db.storagelocation;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * <h1>StorageLocation/h1>
 * Storage location model.
 *
 * @author Mateusz Hermanowicz
 */

@Keep
@Entity(tableName = "storage_locations")
public class StorageLocation implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;

    public StorageLocation(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
    }

    public StorageLocation(){
    }

    public static final Creator<StorageLocation> CREATOR = new Creator<StorageLocation>() {
        @Override
        public StorageLocation createFromParcel(Parcel in) {
            return new StorageLocation(in);
        }

        @Override
        public StorageLocation[] newArray(int size) {
            return new StorageLocation[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
    }
}