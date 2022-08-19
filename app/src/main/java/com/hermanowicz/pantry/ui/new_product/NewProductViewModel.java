package com.hermanowicz.pantry.ui.new_product;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.Database;
import com.hermanowicz.pantry.util.CategorySpinnerListener;
import com.hermanowicz.pantry.util.DatePickerUtil;
import com.hermanowicz.pantry.util.StorageLocationListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NewProductViewModel extends ViewModel {

    @Inject
    NewProductUseCaseImpl useCase;

    //fields
    public ObservableField<String> productName = new ObservableField<>("");
    public MutableLiveData<String> mainCategory = new MutableLiveData<>("");
    public MutableLiveData<String> detailCategory = new MutableLiveData<>("");
    private String expirationDate = "";
    private String productionDate = "";
    private final String storageLocation = "";
    private final LiveData<String[]> storageLocations;
    public ObservableField<String> quantity = new ObservableField<>("1");
    public ObservableField<String> composition = new ObservableField<>("");
    public ObservableField<String> healingProperties = new ObservableField<>("");
    public ObservableField<String> dosage = new ObservableField<>("");
    public ObservableField<String> weight = new ObservableField<>("");
    public ObservableField<String> volume = new ObservableField<>("");
    private String taste = "";
    public ObservableField<Boolean> isVege = new ObservableField<>(false);
    public ObservableField<Boolean> isBio = new ObservableField<>(false);
    public ObservableField<Boolean> hasSugar = new ObservableField<>(false);
    public ObservableField<Boolean> hasSalt = new ObservableField<>(false);

    //detail category visibility, visible if user set main category, gone if user not set category
    public ObservableField<Integer> detailCategoryVisibility = new ObservableField<>(View.GONE);

    //datepickers date int values
    public ObservableField<Integer> expirationDateYear = new ObservableField<>();
    public ObservableField<Integer> expirationDateMonth = new ObservableField<>();
    public ObservableField<Integer> expirationDateDay = new ObservableField<>();
    public ObservableField<Integer> productionDateYear = new ObservableField<>();
    public ObservableField<Integer> productionDateMonth = new ObservableField<>();
    public ObservableField<Integer> productionDateDay = new ObservableField<>();

    public ObservableField<Integer> expirationDatePickerVisibility = new ObservableField<>(View.GONE);
    public ObservableField<Integer> productionDatePickerVisibility = new ObservableField<>(View.GONE);

    private final LiveData<String[]> ownCategoriesNamesLiveData;
    private String[] ownCategoriesNamesArray;

    private Bundle arguments = new Bundle();

    //spinners listeners
    public AdapterView.OnItemSelectedListener categorySelectionListener =
            new CategorySpinnerListener(mainCategory, detailCategory, detailCategoryVisibility);
    public AdapterView.OnItemSelectedListener storageLocationSelectionListener =
            new StorageLocationListener(storageLocation);

    @Inject
    public NewProductViewModel(NewProductUseCaseImpl newProductUseCase) {
        useCase = newProductUseCase;
        storageLocations = useCase.getAllStorageLocations();
        ownCategoriesNamesLiveData = useCase.getAllOwnCategories();
    }

    public void insertProducts() {
        List<Product> productList = getProductList();
        useCase.insert(productList);
    }

    public ArrayList<Product> getProductListToInsert() {
        return useCase.getProductListToInsert();
    }

    private List<Product> getProductList() {
        Product product = getProduct();
        int productQuantity = useCase.getIntValueFromObservableField(quantity);
        List<Product> productList = new ArrayList<>();
        for (int counter = 0; counter <= productQuantity; counter++) {
            productList.add(product);
        }
        return productList;
    }

    public LiveData<String> getMainCategoryValue() {
        return mainCategory;
    }

    public LiveData<String[]> getStorageLocations() {
        return storageLocations;
    }

    public LiveData<String[]> getOwnCategoriesNamesLiveData() {
        return ownCategoriesNamesLiveData;
    }

    public String[] getOwnCategoriesNamesArray() {
        return ownCategoriesNamesArray;
    }

    public void onExpirationDateChanged(int year, int month, int day) {
        month++;
        expirationDate = year + "." + month + "." + day;
    }

    public void onProductionDateChanged(int year, int month, int day) {
        month++;
        productionDate = year + "." + month + "." + day;
    }

    @NonNull
    private Product getProduct() {
        int productWeight = useCase.getIntValueFromObservableField(weight);
        int productVolume = useCase.getIntValueFromObservableField(volume);

        Product product = new Product();
        product.setName(productName.get());
        product.setMainCategory(mainCategory.getValue());
        product.setDetailCategory(detailCategory.getValue());
        product.setStorageLocation(storageLocation);
        product.setExpirationDate(expirationDate);
        product.setProductionDate(productionDate);
        product.setComposition(composition.get());
        product.setHealingProperties(healingProperties.get());
        product.setDosage(dosage.get());
        product.setWeight(productWeight);
        product.setVolume(productVolume);
        product.setTaste(taste);
        product.setIsBio(Boolean.TRUE.equals(isBio.get()));
        product.setIsVege(Boolean.TRUE.equals(isVege.get()));
        product.setHasSugar(Boolean.TRUE.equals(hasSugar.get()));
        product.setHasSalt(Boolean.TRUE.equals(hasSalt.get()));
        return product;
    }

    public void setTaste(@Nullable RadioButton selectedTasteButton) {
        if (selectedTasteButton == null)
            taste = "";
        else
            taste = selectedTasteButton.getText().toString();
    }

    public void onClickClearFields() {
        clearFields();
    }

    private void clearFields() {
        productName.set("");
        mainCategory.setValue("");
        detailCategory.setValue("");
        DatePickerUtil.resetDateInDatePicker(expirationDateYear, expirationDateMonth, expirationDateDay);
        DatePickerUtil.resetDateInDatePicker(productionDateYear, productionDateMonth, productionDateDay);
        expirationDate = "";
        productionDate = "";
        quantity.set("1");
        weight.set("");
        volume.set("");
        isVege.set(false);
        hasSugar.set(false);
        hasSalt.set(false);
    }

    public void setOwnCategoriesNamesArray(String[] ownCategoriesNamesArray) {
        this.ownCategoriesNamesArray = ownCategoriesNamesArray;
    }

    public void setDatePickerEnabled(CompoundButton checkBox, boolean isChecked){
        if(checkBox.getId() == R.id.productExpirationDateCheckbox) {
            if (isChecked) {
                expirationDatePickerVisibility.set(View.GONE);
                expirationDate = "";
            }
            else
                expirationDatePickerVisibility.set(View.VISIBLE);
        }
        if(checkBox.getId() == R.id.productProductionDateCheckbox) {
            if (isChecked) {
                productionDatePickerVisibility.set(View.GONE);
                productionDate = "";
            }
            else
                productionDatePickerVisibility.set(View.VISIBLE);
        }
    }

    public void setDatabaseMode(Database databaseMode) {
        useCase.setDatabaseMode(databaseMode);
    }

    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }

    public void showProductDataIfExists() {
        if(arguments != null){
            ArrayList<Product> productArrayList =
                    arguments.getParcelableArrayList("productArrayList");
            if(productArrayList.size() == 1){
                Product product = productArrayList.get(0);
                showProductData(product);
            }
            else if (productArrayList.size() > 1) {
                //todo: choose product to copy
            }
        }
    }

    private void showProductData(Product product) {
        productName.set(product.getName());
        weight.set(String.valueOf(product.getWeight()));
        volume.set(String.valueOf(product.getVolume()));
        isVege.set(product.getIsVege());
        isBio.set(product.getIsBio());
        hasSugar.set(product.getHasSugar());
        hasSalt.set(product.getHasSalt());
    }
}