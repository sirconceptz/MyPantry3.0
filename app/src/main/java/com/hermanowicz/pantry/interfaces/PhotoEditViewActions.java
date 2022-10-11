package com.hermanowicz.pantry.interfaces;

import java.io.File;

public interface PhotoEditViewActions {
    void takePhotoIntent(File photoFile);

    void showDescription(String photoDescription);
}
