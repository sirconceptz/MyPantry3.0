package com.hermanowicz.pantry.ui.filter_product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.data.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.data.repository.StorageLocationRepository;
import com.hermanowicz.pantry.domain.usecase.FilterProductUseCase;
import com.hermanowicz.pantry.model.Filter;
import com.hermanowicz.pantry.model.FilterModel;

import javax.inject.Inject;

public class FilterProductUseCaseImpl implements FilterProductUseCase {

    private final StorageLocationRepository storageLocationRepository;
    private final OwnCategoryRepository ownCategoryRepository;
    private final MutableLiveData<FilterModel> filterModelMutableLiveData = new MutableLiveData<>();
    private Filter filter;
    private FilterModel filterModel = new FilterModel();
    private String expirationDateSince = "";
    private String expirationDateFor = "";
    private String productionDateSince = "";
    private String productionDateFor = "";

    @Inject
    public FilterProductUseCaseImpl(StorageLocationRepository storageLocationRepository,
                                    OwnCategoryRepository ownCategoryRepository) {
        this.storageLocationRepository = storageLocationRepository;
        this.ownCategoryRepository = ownCategoryRepository;
    }

    @Override
    public LiveData<String[]> getAllStorageLocationsNames() {
        return storageLocationRepository.getAllStorageLocationsNames();
    }

    @Override
    public LiveData<String[]> getAllOwnCategoriesNames() {
        return ownCategoryRepository.getOwnCategoriesNames();
    }

    @Override
    public void setExpirationDateSince(int year, int month, int day) {
        expirationDateSince = year + "." + month + "." + day;
    }

    @Override
    public void setExpirationDateFor(int year, int month, int day) {
        expirationDateFor = year + "." + month + "." + day;
    }

    @Override
    public void setProductionDateSince(int year, int month, int day) {
        productionDateSince = year + "." + month + "." + day;
    }

    @Override
    public void setProductionDateFor(int year, int month, int day) {
        productionDateFor = year + "." + month + "." + day;
    }

    @Override
    public void clearExpirationDateSince() {
        expirationDateSince = "";
    }

    @Override
    public void clearExpirationDateFor() {
        expirationDateFor = "";
    }

    @Override
    public void clearProductionDateSince() {
        productionDateSince = "";
    }

    @Override
    public void clearProductionDateFor() {
        productionDateFor = "";
    }

    @Override
    public String getExpirationDateSince() {
        return expirationDateSince;
    }

    @Override
    public String getExpirationDateFor() {
        return expirationDateFor;
    }

    @Override
    public String getProductionDateSince() {
        return productionDateSince;
    }

    @Override
    public String getProductionDateFor() {
        return productionDateFor;
    }
}