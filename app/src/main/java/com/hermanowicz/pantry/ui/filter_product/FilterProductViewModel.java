package com.hermanowicz.pantry.ui.filter_product;

import android.view.View;
import android.widget.AdapterView;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.util.CategorySpinnerListener;
import com.hermanowicz.pantry.util.DatePickerUtil;
import com.hermanowicz.pantry.util.StorageLocationListener;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FilterProductViewModel extends ViewModel {

    @Inject
    FilterProductUseCaseImpl useCase;

    //fields
    public ObservableField<String> productName = new ObservableField<>();
    public MutableLiveData<String> mainCategory = new MutableLiveData<>("");
    public MutableLiveData<String> detailCategory = new MutableLiveData<>("");
    private final LiveData<String[]> storageLocations;
    public String storageLocation = "";
    public ObservableField<String> composition = new ObservableField<>("");
    public ObservableField<String> healingProperties = new ObservableField<>("");
    public ObservableField<String> dosage = new ObservableField<>("");
    public ObservableField<String> weightSince = new ObservableField<>();
    public ObservableField<String> weightFor = new ObservableField<>();
    public ObservableField<String> volumeSince = new ObservableField<>();
    public ObservableField<String> volumeFor = new ObservableField<>();
    public ObservableField<Boolean> isSweet = new ObservableField<>();
    public ObservableField<Boolean> isSour = new ObservableField<>();
    public ObservableField<Boolean> isSweetAndSour = new ObservableField<>();
    public ObservableField<Boolean> isBitter = new ObservableField<>();
    public ObservableField<Boolean> isSalty = new ObservableField<>();
    public ObservableField<Boolean> isVege = new ObservableField<>();
    public ObservableField<Boolean> isBio = new ObservableField<>();
    public ObservableField<Boolean> hasSugar = new ObservableField<>();
    public ObservableField<Boolean> hasSalt = new ObservableField<>();

    //detail category visibility, visible if user set main category, gone if user not set category
    public ObservableField<Integer> detailCategoryVisibility = new ObservableField<>(View.GONE);

    //datepickers date int values
    public ObservableField<Integer> expirationDateSinceYear = new ObservableField<>();
    public ObservableField<Integer> expirationDateSinceMonth = new ObservableField<>();
    public ObservableField<Integer> expirationDateSinceDay = new ObservableField<>();
    public ObservableField<Integer> expirationDateForYear = new ObservableField<>();
    public ObservableField<Integer> expirationDateForMonth = new ObservableField<>();
    public ObservableField<Integer> expirationDateForDay = new ObservableField<>();
    public ObservableField<Integer> productionDateSinceYear = new ObservableField<>();
    public ObservableField<Integer> productionDateSinceMonth = new ObservableField<>();
    public ObservableField<Integer> productionDateSinceDay = new ObservableField<>();
    public ObservableField<Integer> productionDateForYear = new ObservableField<>();
    public ObservableField<Integer> productionDateForMonth = new ObservableField<>();
    public ObservableField<Integer> productionDateForDay = new ObservableField<>();

    private String expirationDateSince = "";
    private String expirationDateFor = "";
    private String productionDateSince = "";
    private String productionDateFor = "";

    private final LiveData<String[]> ownCategoriesNamesLiveData;
    private String[] ownCategoriesNamesArray;

    public AdapterView.OnItemSelectedListener mainCategorySelectedListener =
            new CategorySpinnerListener(mainCategory, detailCategory, detailCategoryVisibility);
    public AdapterView.OnItemSelectedListener storageLocationSelectionListener =
            new StorageLocationListener(storageLocation);

    @Inject
    public FilterProductViewModel(FilterProductUseCaseImpl filterProductUseCase){
        useCase = filterProductUseCase;
        storageLocations = useCase.getAllStorageLocationsNames();
        ownCategoriesNamesLiveData = useCase.getAllOwnCategoriesNames();
    }

    public void onClickFilterProducts(){

    }

    public void onClickClearFilter(){
        clearFields();
    }

    public LiveData<String> getMainCategoryValue() {
        return mainCategory;
    }

    public LiveData<String[]> getOwnCategoriesNamesLiveData() {
        return ownCategoriesNamesLiveData;
    }

    public String[] getOwnCategoriesNamesArray() {
        return ownCategoriesNamesArray;
    }

    public void setOwnCategoriesNamesArray(String[] ownCategoriesNamesArray) {
        this.ownCategoriesNamesArray = ownCategoriesNamesArray;
    }

    public LiveData<String[]> getStorageLocations() {
        return storageLocations;
    }

    public void onExpirationDateSinceChanged(int year, int month, int day){
        month++;
        expirationDateSince = year + "." + month + "." + day;
    }

    public void onExpirationDateForChanged(int year, int month, int day){
        month++;
        expirationDateFor = year + "." + month + "." + day;
    }

    public void onProductionDateSinceChanged(int year, int month, int day){
        month++;
        productionDateSince = year + "." + month + "." + day;
    }

    public void onProductionDateForChanged(int year, int month, int day){
        month++;
        productionDateFor = year + "." + month + "." + day;
    }

    private void clearFields(){
        productName.set("");
        mainCategory.setValue("");
        detailCategory.setValue("");
        DatePickerUtil.resetDateInDatePicker(expirationDateSinceYear, expirationDateSinceMonth, expirationDateSinceDay);
        DatePickerUtil.resetDateInDatePicker(expirationDateForYear, expirationDateForMonth, expirationDateForDay);
        DatePickerUtil.resetDateInDatePicker(productionDateSinceYear, productionDateSinceMonth, productionDateSinceDay);
        DatePickerUtil.resetDateInDatePicker(productionDateForYear, productionDateForMonth, productionDateForDay);
        expirationDateSince = "";
        expirationDateFor = "";
        productionDateSince = "";
        productionDateFor = "";
        weightSince.set("");
        weightFor.set("");
        volumeSince.set("");
        volumeFor.set("");
        isSweet.set(false);
        isSour.set(false);
        isSweetAndSour.set(false);
        isBitter.set(false);
        isSalty.set(false);
        isVege.set(false);
        isBitter.set(false);
        hasSugar.set(false);
        hasSalt.set(false);
    }
}