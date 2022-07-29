package com.hermanowicz.pantry.ui.edit_product;

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
public class EditProductViewModel extends ViewModel {

    @Inject
    EditProductUseCaseImpl useCase;
    //fields
    public ObservableField<String> productName = new ObservableField<>();
    public MutableLiveData<String> mainCategory = new MutableLiveData<>("");
    public MutableLiveData<String> detailCategory = new MutableLiveData<>("");
    private final LiveData<String[]> storageLocations;
    public String storageLocation = "";
    public ObservableField<String> quantity = new ObservableField<>("1");
    public ObservableField<String> composition = new ObservableField<>("");
    public ObservableField<String> healingProperties = new ObservableField<>("");
    public ObservableField<String> dosage = new ObservableField<>("");
    public ObservableField<String> weight = new ObservableField<>();
    public ObservableField<String> volume = new ObservableField<>();
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
    public ObservableField<Integer> expirationDateYear = new ObservableField<>();
    public ObservableField<Integer> expirationDateMonth = new ObservableField<>();
    public ObservableField<Integer> expirationDateDay = new ObservableField<>();
    public ObservableField<Integer> productionDateYear = new ObservableField<>();
    public ObservableField<Integer> productionDateMonth = new ObservableField<>();
    public ObservableField<Integer> productionDateDay = new ObservableField<>();

    private String expirationDate = "";
    private String productionDate = "";

    private final LiveData<String[]> ownCategoriesNamesLiveData;
    private String[] ownCategoriesNamesArray;

    public AdapterView.OnItemSelectedListener categorySelectionListener =
            new CategorySpinnerListener(mainCategory, detailCategory, detailCategoryVisibility);
    public AdapterView.OnItemSelectedListener storageLocationSelectionListener =
            new StorageLocationListener(storageLocation);

    @Inject
    public EditProductViewModel(EditProductUseCaseImpl editProductUseCase) {
        useCase = editProductUseCase;
        storageLocations = useCase.getAllStorageLocationsNames();
        ownCategoriesNamesLiveData = useCase.getAllOwnCategoriesNames();
    }


    public void onClickEditProduct(){

    }

    public void onClickClearFields(){
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


    public void onExpirationDateChanged(
            int year,
            int month,
            int day
    ){
        month++;
        expirationDate = year + "." + month + "." + day;
    }

    public void onProductionDateChanged(
            int year,
            int month,
            int day
    ){
        month++;
        productionDate = year + "." + month + "." + day;
    }

    private void clearFields() {
        productName.set("");
        mainCategory.setValue("");
        detailCategory.setValue("");
        DatePickerUtil.resetDateInDatePicker(expirationDateYear, expirationDateMonth, expirationDateDay);
        DatePickerUtil.resetDateInDatePicker(productionDateYear, productionDateMonth, productionDateDay);
        storageLocation = "";
        expirationDate = "";
        productionDate = "";
        quantity.set("1");
        weight.set("");
        volume.set("");
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