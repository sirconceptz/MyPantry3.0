package com.hermanowicz.pantry.ui.my_pantry;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MyPantryViewModel extends ViewModel {

    @Inject
    MyPantryUseCaseImpl myPantryUseCase;

    @Inject
    public MyPantryViewModel(MyPantryUseCaseImpl myPantryUseCase) {
        this.myPantryUseCase = myPantryUseCase;
    }
}