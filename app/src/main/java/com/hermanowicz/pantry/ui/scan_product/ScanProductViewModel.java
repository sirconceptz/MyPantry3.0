package com.hermanowicz.pantry.ui.scan_product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ScanProductViewModel extends ViewModel {

    @Inject
    ScanProductUseCaseImpl useCase;
    private final MutableLiveData<String> text;

    @Inject
    public ScanProductViewModel(ScanProductUseCaseImpl scanProductUseCase) {
        useCase = scanProductUseCase;
        text = new MutableLiveData<>();
        text.setValue("SCAN PRODUCT");
    }

    public LiveData<String> getText() {
        return text;
    }
}