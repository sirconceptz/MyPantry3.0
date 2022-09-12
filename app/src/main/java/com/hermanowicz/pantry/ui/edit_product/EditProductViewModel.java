package com.hermanowicz.pantry.ui.edit_product;

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
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.util.CategorySpinnerListener;
import com.hermanowicz.pantry.util.DateHelper;
import com.hermanowicz.pantry.util.StorageLocationListener;

import java.util.ArrayList;
import java.util.List;

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
    public ObservableField<String> weight = new ObservableField<>("");
    public ObservableField<String> volume = new ObservableField<>("");
    private String taste = "";
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

    public ObservableField<Boolean> expirationCheckBoxChecked = new ObservableField<>(false);
    public ObservableField<Boolean> productionCheckBoxChecked = new ObservableField<>(false);
    public ObservableField<Integer> expirationDatePickerVisibility = new ObservableField<>(View.VISIBLE);
    public ObservableField<Integer> productionDatePickerVisibility = new ObservableField<>(View.VISIBLE);

    private final LiveData<String[]> ownCategoriesNamesLiveData;
    private String[] ownCategoriesNamesArray;

    public AdapterView.OnItemSelectedListener categorySelectionListener =
            new CategorySpinnerListener(mainCategory, detailCategory, detailCategoryVisibility);
    public AdapterView.OnItemSelectedListener storageLocationSelectionListener =
            new StorageLocationListener(storageLocation);
    private ArrayList<Product> productArrayList;

    @Inject
    public EditProductViewModel(EditProductUseCaseImpl editProductUseCase) {
        useCase = editProductUseCase;
        storageLocations = useCase.getAllStorageLocationsNames();
        ownCategoriesNamesLiveData = useCase.getAllOwnCategoriesNames();
    }

    public ArrayList<Product> getProductArrayList() {
        return productArrayList;
    }

    public void onClickUpdateProduct() {
        updateProducts();
    }

    private void updateProducts() {
        List<Product> productListToUpdate = new ArrayList<>();
        for (Product product : productArrayList) {
            productListToUpdate.add(getUpdatedProduct(product));
        }
        useCase.updateProductList(productListToUpdate);
    }

    public void onClickClearFields() {
        clearFields();
    }

    @NonNull
    private Product getUpdatedProduct(Product product) {
        int productWeight = useCase.getIntValueFromObservableField(weight);
        int productVolume = useCase.getIntValueFromObservableField(volume);
        String expirationDate = useCase.getExpirationDate();
        String productionDate = useCase.getProductionDate();

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
    ) {
        month++;
        useCase.setExpirationDate(year, month, day);
    }

    public void onProductionDateChanged(
            int year,
            int month,
            int day
    ) {
        month++;
        useCase.setProductionDate(year, month, day);
    }

    private void clearFields() {
        productName.set("");
        mainCategory.setValue("");
        detailCategory.setValue("");
        DateHelper.resetDateInDatePicker(expirationDateYear, expirationDateMonth, expirationDateDay);
        DateHelper.resetDateInDatePicker(productionDateYear, productionDateMonth, productionDateDay);
        storageLocation = "";
        useCase.clearExpirationDate();
        useCase.clearProductionDate();
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

    public void showDataForSelectedDatabase() {
        Product product = getProductArrayList().get(0);
        int productListSize = getProductArrayList().size();
        setupDatepickersAndSetDates(product);

        productName.set(product.getName());
        storageLocation = product.getStorageLocation();
        quantity.set(String.valueOf(productListSize));
        composition.set(product.getComposition());
        healingProperties.set(product.getHealingProperties());
        dosage.set(product.getDosage());
        weight.set(String.valueOf(product.getWeight()));
        volume.set(String.valueOf(product.getVolume()));
    }

    private void setupDatepickersAndSetDates(Product product) {
        useCase.setExpirationDate(product.getExpirationDate());
        useCase.setProductionDate(product.getProductionDate());

        int[] expirationDate = useCase.getExpirationDateArray();
        expirationDateYear.set(expirationDate[0]);
        expirationDateMonth.set(expirationDate[1]);
        expirationDateDay.set(expirationDate[2]);

        int[] productionDate = useCase.getProductionDateArray();
        productionDateYear.set(productionDate[0]);
        productionDateMonth.set(productionDate[1]);
        productionDateDay.set(productionDate[2]);

        if(product.getExpirationDate().equals("")) {
            expirationCheckBoxChecked.set(true);
            expirationDatePickerVisibility.set(View.GONE);
        }
        if(product.getProductionDate().equals("")) {
            productionCheckBoxChecked.set(true);
            productionDatePickerVisibility.set(View.GONE);
        }
    }

    public void setDatePickerEnabled(CompoundButton checkBox, boolean isChecked){
        if(checkBox.getId() == R.id.productExpirationDateCheckbox) {
            if (isChecked) {
                expirationDatePickerVisibility.set(View.GONE);
                useCase.clearExpirationDate();
            }
            else
                expirationDatePickerVisibility.set(View.VISIBLE);
        }
        if(checkBox.getId() == R.id.productProductionDateCheckbox) {
            if (isChecked) {
                productionDatePickerVisibility.set(View.GONE);
                useCase.clearProductionDate();
            }
            else
                productionDatePickerVisibility.set(View.VISIBLE);
        }
    }

    public void setArrayProductList(ArrayList<Product> productArrayList) {
        this.productArrayList = productArrayList;
    }

    public void setDatabaseMode(DatabaseMode databaseMode) {
        useCase.setDatabaseMode(databaseMode);
    }
}