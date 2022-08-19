package com.hermanowicz.pantry.ui.my_pantry;

import com.hermanowicz.pantry.repository.ProductRepository;

import javax.inject.Inject;

public class MyPantryUseCaseImpl implements MyPantryUseCase {

    private final ProductRepository repository;

    @Inject
    public MyPantryUseCaseImpl(ProductRepository productRepository) {
        repository = productRepository;
    }
}