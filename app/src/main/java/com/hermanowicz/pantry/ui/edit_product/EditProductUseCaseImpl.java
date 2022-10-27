package com.hermanowicz.pantry.ui.edit_product;

import android.content.res.Resources;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.data.db.dao.storagelocation.StorageLocation;
import com.hermanowicz.pantry.domain.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.domain.repository.ProductRepository;
import com.hermanowicz.pantry.domain.repository.StorageLocationRepository;
import com.hermanowicz.pantry.domain.usecase.EditProductUseCase;
import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class EditProductUseCaseImpl implements EditProductUseCase {

    private final ProductRepository productRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final OwnCategoryRepository ownCategoryRepository;
    private final Resources resources;
    private DatabaseMode databaseMode;
    private String expirationDate;
    private String productionDate;
    private List<Product> productList;

    @Inject
    public EditProductUseCaseImpl(ProductRepository productRepository,
                                  StorageLocationRepository storageLocationRepository,
                                  OwnCategoryRepository ownCategoryRepository,
                                  Resources resources) {
        this.productRepository = productRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.ownCategoryRepository = ownCategoryRepository;
        this.resources = resources;
    }

    @Override
    public void updateProductList(List<Product> productList) {
        productRepository.update(productList, databaseMode);
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
    public void setDatabaseMode(DatabaseMode databaseMode) {
        this.databaseMode = databaseMode;
    }

    @Override
    public int getIntValueFromObservableField(ObservableField<String> observableField) {
        return productRepository.getIntValueFromObservableField(observableField);
    }

    @Override
    public void setExpirationDate(int year, int month, int day) {
        expirationDate = year + "." + month + "." + day;
    }

    @Override
    public void setProductionDate(int year, int month, int day) {
        productionDate = year + "." + month + "." + day;
    }

    @Override
    public void clearExpirationDate() {
        expirationDate = "";
    }

    @Override
    public void clearProductionDate() {
        productionDate = "";
    }

    @Override
    public String getExpirationDate() {
        return expirationDate;
    }

    @Override
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String getProductionDate() {
        return productionDate;
    }

    @Override
    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    @Override
    public int[] getExpirationDateArray() {
        String[] dateArrayString = expirationDate.split("\\.");
        return Arrays.stream(dateArrayString).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public int[] getProductionDateArray() {
        String[] dateArrayString = productionDate.split("\\.");
        return Arrays.stream(dateArrayString).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public void setProductArrayList(ArrayList<Product> productArrayList) {
        productList = productArrayList;
    }

    @Override
    public List<Product> getAllProductList() {
        return productList;
    }

    @Override
    public int getDetailCategorySpinnerPosition(int productMainCategorySpinnerPosition) {
        String[] detailCategoryArray;
        int selection = 0;
        if (productMainCategorySpinnerPosition == 1 && databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            detailCategoryArray = ownCategoryRepository.getAllCategoriesNameAsList(databaseMode);
        else if (productMainCategorySpinnerPosition == 2)
            detailCategoryArray = resources.getStringArray(R.array.product_store_products_array);
        else if (productMainCategorySpinnerPosition == 3)
            detailCategoryArray = resources.getStringArray(R.array.product_ready_meals_array);
        else if (productMainCategorySpinnerPosition == 4)
            detailCategoryArray = resources.getStringArray(R.array.product_vegetables_array);
        else if (productMainCategorySpinnerPosition == 5)
            detailCategoryArray = resources.getStringArray(R.array.product_fruits_array);
        else if (productMainCategorySpinnerPosition == 6)
            detailCategoryArray = resources.getStringArray(R.array.product_herbs_array);
        else if (productMainCategorySpinnerPosition == 7)
            detailCategoryArray = resources.getStringArray(R.array.product_liqueurs_array);
        else if (productMainCategorySpinnerPosition == 8)
            detailCategoryArray = resources.getStringArray(R.array.product_wines_type_array);
        else if (productMainCategorySpinnerPosition == 9)
            detailCategoryArray = resources.getStringArray(R.array.product_mushrooms_array);
        else if (productMainCategorySpinnerPosition == 10)
            detailCategoryArray = resources.getStringArray(R.array.product_vinegars_array);
        else if (productMainCategorySpinnerPosition == 11)
            detailCategoryArray = resources.getStringArray(R.array.product_chemical_products_array);
        else
            detailCategoryArray = resources.getStringArray(R.array.product_other_products_array);

        for (int counter = 0; detailCategoryArray.length > counter; counter++) {
            if (productList.get(0).getDetailCategory().equals(detailCategoryArray[counter]))
                selection = counter;
        }
        return selection;
    }

    @Override
    public int getProductMainCategorySpinnerPosition() {
        String[] productTypesArray = resources.getStringArray(R.array.product_type_of_product_array);
        int selection = 0;
        for (int counter = 0; productTypesArray.length > counter; counter++) {
            if (productList.get(0).getMainCategory().equals(productTypesArray[counter]))
                selection = counter;
        }
        return selection;
    }

    @Override
    public int getProductStorageLocationPosition() {
        List<StorageLocation> storageLocationList = storageLocationRepository.getAllStorageLocations(databaseMode).getValue();
        int selection = 0;
        for (int counter = 0; (storageLocationList != null ? storageLocationList.size() : 0) > counter; counter++) {
            if (productList.get(0).getStorageLocation().equals(storageLocationList.get(counter).getName()))
                selection = counter;
        }
        return selection;
    }

    public String[] getDetailCategoryArray(String typeOfProductSpinnerValue) {
        String[] productMainCategoryArray = resources.getStringArray(R.array.product_type_of_product_array);
        if (typeOfProductSpinnerValue.equals(productMainCategoryArray[1]))
            return getAllOwnCategoriesNames().getValue();
        else if (typeOfProductSpinnerValue.equals(productMainCategoryArray[2]))
            return resources.getStringArray(R.array.product_store_products_array);
        else if (typeOfProductSpinnerValue.equals(productMainCategoryArray[3]))
            return resources.getStringArray(R.array.product_ready_meals_array);
        else if (typeOfProductSpinnerValue.equals(productMainCategoryArray[4]))
            return resources.getStringArray(R.array.product_vegetables_array);
        else if (typeOfProductSpinnerValue.equals(productMainCategoryArray[5]))
            return resources.getStringArray(R.array.product_fruits_array);
        else if (typeOfProductSpinnerValue.equals(productMainCategoryArray[6]))
            return resources.getStringArray(R.array.product_herbs_array);
        else if (typeOfProductSpinnerValue.equals(productMainCategoryArray[7]))
            return resources.getStringArray(R.array.product_liqueurs_array);
        else if (typeOfProductSpinnerValue.equals(productMainCategoryArray[8]))
            return resources.getStringArray(R.array.product_wines_type_array);
        else if (typeOfProductSpinnerValue.equals(productMainCategoryArray[9]))
            return resources.getStringArray(R.array.product_mushrooms_array);
        else if (typeOfProductSpinnerValue.equals(productMainCategoryArray[10]))
            return resources.getStringArray(R.array.product_vinegars_array);
        else if (typeOfProductSpinnerValue.equals(productMainCategoryArray[11]))
            return resources.getStringArray(R.array.product_chemical_products_array);
        else
            return resources.getStringArray(R.array.product_other_products_array);
    }
}