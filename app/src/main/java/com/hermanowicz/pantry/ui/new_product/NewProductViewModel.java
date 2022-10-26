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
import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.interfaces.NewProductViewActions;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.GroupProduct;
import com.hermanowicz.pantry.util.CategorySpinnerListener;
import com.hermanowicz.pantry.util.DateHelper;
import com.hermanowicz.pantry.util.StorageLocationListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NewProductViewModel extends ViewModel {

    private final String storageLocation = "";
    private final LiveData<String[]> storageLocations;
    private final LiveData<String[]> ownCategoriesNamesLiveData;
    //fields
    public ObservableField<String> productName = new ObservableField<>("");
    public MutableLiveData<String> mainCategory = new MutableLiveData<>("");
    public MutableLiveData<String> detailCategory = new MutableLiveData<>("");
    public ObservableField<String> quantity = new ObservableField<>("1");
    public ObservableField<String> composition = new ObservableField<>("");
    public ObservableField<String> healingProperties = new ObservableField<>("");
    public ObservableField<String> dosage = new ObservableField<>("");
    public ObservableField<String> weight = new ObservableField<>("");
    public ObservableField<String> volume = new ObservableField<>("");
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
    //spinners listeners
    public AdapterView.OnItemSelectedListener categorySelectionListener =
            new CategorySpinnerListener(mainCategory, detailCategory, detailCategoryVisibility);
    public AdapterView.OnItemSelectedListener storageLocationSelectionListener =
            new StorageLocationListener(storageLocation);
    @Inject
    NewProductUseCaseImpl useCase;
    private String taste = "";
    private String[] ownCategoriesNamesArray;
    private NewProductViewActions newProductViewActions;

    @Inject
    public NewProductViewModel(NewProductUseCaseImpl newProductUseCase) {
        useCase = newProductUseCase;
        storageLocations = useCase.getAllStorageLocations();
        ownCategoriesNamesLiveData = useCase.getAllOwnCategories();

        resetDateData();
    }

    private void resetDateData() {
        DateHelper.resetDateInDatePicker(expirationDateYear, expirationDateMonth, expirationDateDay);
        DateHelper.resetDateInDatePicker(productionDateYear, productionDateMonth, productionDateDay);
        useCase.clearExpirationDate();
        useCase.clearProductionDate();
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
        for (int counter = 1; counter <= productQuantity; counter++) {
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

    public void setOwnCategoriesNamesArray(String[] ownCategoriesNamesArray) {
        this.ownCategoriesNamesArray = ownCategoriesNamesArray;
    }

    public void onExpirationDateChanged(int year, int month, int day) {
        month++;
        useCase.setExpirationDate(year, month, day);
    }

    public void onProductionDateChanged(int year, int month, int day) {
        month++;
        useCase.setProductionDate(year, month, day);
    }

    @NonNull
    private Product getProduct() {
        int productWeight = useCase.getIntValueFromObservableField(weight);
        int productVolume = useCase.getIntValueFromObservableField(volume);
        String expirationDate = useCase.getExpirationDate();
        String productionDate = useCase.getProductionDate();

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
        product.setHashCode(String.valueOf(product.hashCode()));
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
        quantity.set("1");
        weight.set("");
        volume.set("");
        isVege.set(false);
        hasSugar.set(false);
        hasSalt.set(false);
        resetDateData();
    }

    public void setDatePickerEnabled(CompoundButton checkBox, boolean isChecked) {
        if (checkBox.getId() == R.id.productExpirationDateCheckbox) {
            if (isChecked) {
                expirationDatePickerVisibility.set(View.GONE);
                useCase.clearExpirationDate();
            } else {
                expirationDatePickerVisibility.set(View.VISIBLE);
                int year = expirationDateYear.get();
                int month = expirationDateMonth.get();
                int day = expirationDateDay.get();
                useCase.setExpirationDate(year, month, day);
            }
        }
        if (checkBox.getId() == R.id.productProductionDateCheckbox) {
            if (isChecked) {
                productionDatePickerVisibility.set(View.GONE);
                useCase.clearProductionDate();
            } else
                productionDatePickerVisibility.set(View.VISIBLE);
            int year = productionDateYear.get();
            int month = productionDateMonth.get();
            int day = productionDateDay.get();
            useCase.setProductionDate(year, month, day);
        }
    }

    public void setDatabaseMode(DatabaseMode databaseMode) {
        useCase.setDatabaseMode(databaseMode);
    }

    public void showProductDataIfExists(@Nullable Bundle arguments) {
        if (arguments != null) {
            ArrayList<Product> productArrayList =
                    arguments.getParcelableArrayList("productArrayList");
            List<GroupProduct> groupProductArrayList = useCase.setAndGetGroupProductListFromProductList(productArrayList);
            if (groupProductArrayList.size() == 1) {
                Product product = groupProductArrayList.get(0).getProduct();
                int productQuantity = groupProductArrayList.get(0).getQuantity();
                showProductData(product, productQuantity);
            } else if (groupProductArrayList.size() > 1) {
                String[] groupProductNames = useCase.getGroupProductNames(productArrayList);
                newProductViewActions.showDialogChooseProductToCopy(groupProductNames);
            }
        }
    }

    private void showProductData(Product product, int productQuantity) {
        productName.set(product.getName());
        quantity.set(String.valueOf(productQuantity));
        weight.set(String.valueOf(product.getWeight()));
        volume.set(String.valueOf(product.getVolume()));
        isVege.set(product.getIsVege());
        isBio.set(product.getIsBio());
        hasSugar.set(product.getHasSugar());
        hasSalt.set(product.getHasSalt());
    }

    public void setNewProductDialogListener(NewProductViewActions newProductViewActions) {
        this.newProductViewActions = newProductViewActions;
    }

    public void showSelectedProductData(int position) {
        List<GroupProduct> groupProductList = useCase.getGroupProductList();
        Product product = groupProductList.get(position).getProduct();
        int productQuantity = groupProductList.get(position).getQuantity();
        showProductData(product, productQuantity);
    }

    public boolean isFormValid() {
        boolean isValidForm = false;
        if (Objects.requireNonNull(productName.get()).length() < 3)
            newProductViewActions.showErrorNameIsTooShort();
        else
            isValidForm = true;
        return isValidForm;
    }
}