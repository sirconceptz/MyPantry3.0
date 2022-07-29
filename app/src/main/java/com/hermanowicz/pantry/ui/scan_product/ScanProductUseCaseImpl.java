package com.hermanowicz.pantry.ui.scan_product;

import com.hermanowicz.pantry.repository.ProductRepository;

import javax.inject.Inject;

public class ScanProductUseCaseImpl implements ScanProductUseCase {

    private final ProductRepository repository;

    @Inject
    public ScanProductUseCaseImpl(ProductRepository productRepository){
        repository = productRepository;
    }
}