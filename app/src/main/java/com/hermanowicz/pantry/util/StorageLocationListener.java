package com.hermanowicz.pantry.util;

import android.view.View;
import android.widget.AdapterView;

public class StorageLocationListener implements AdapterView.OnItemSelectedListener {

    public String storageLocation;

    public StorageLocationListener(String storageLocation){
        this.storageLocation = storageLocation;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setStorageLocationIfChanged(parent, position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void setStorageLocationIfChanged(AdapterView<?> parent, int position) {
        if (position == 0) { // no storage location
            storageLocation = "";
        } else {
            storageLocation = (String) parent.getItemAtPosition(position);
        }
    }
}