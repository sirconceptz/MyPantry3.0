package com.hermanowicz.pantry.ui.product_details;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.interfaces.ShowPhotoViewActions;
import com.hermanowicz.pantry.model.DatabaseMode;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductDetailsViewModel extends ViewModel {

    @Inject
    ProductDetailsUseCaseImpl useCase;

    //fields
    public MutableLiveData<String> productName = new MutableLiveData<>("");
    public MutableLiveData<String> mainCategory = new MutableLiveData<>("");
    public MutableLiveData<String> detailCategory = new MutableLiveData<>("");
    public MutableLiveData<String> expirationDate = new MutableLiveData<>("");
    public MutableLiveData<String> productionDate = new MutableLiveData<>("");
    public MutableLiveData<String> storageLocation = new MutableLiveData<>("");
    public MutableLiveData<String> quantity = new MutableLiveData<>("");
    public MutableLiveData<String> composition = new MutableLiveData<>("");
    public MutableLiveData<String> healingProperties = new MutableLiveData<>("");
    public MutableLiveData<String> dosage = new MutableLiveData<>("");
    public MutableLiveData<String> weight = new MutableLiveData<>("");
    public MutableLiveData<String> volume = new MutableLiveData<>("");
    public MutableLiveData<String> taste = new MutableLiveData<>("");
    public MutableLiveData<String> isVege = new MutableLiveData<>("");
    public MutableLiveData<String> isBio = new MutableLiveData<>("");
    public MutableLiveData<String> hasSugar = new MutableLiveData<>("");
    public MutableLiveData<String> hasSalt = new MutableLiveData<>("");

    //fields visibility, visible if not empty
    public ObservableField<Integer> photoVisibility = new ObservableField<>(View.GONE);
    public ObservableField<Integer> detailCategoryVisibility = new ObservableField<>(View.GONE);
    public ObservableField<Integer> expirationDateVisibility = new ObservableField<>(View.GONE);
    public ObservableField<Integer> productionDateVisibility = new ObservableField<>(View.GONE);
    public ObservableField<Integer> storageLocationVisibility = new ObservableField<>(View.GONE);
    public ObservableField<Integer> compositionVisibility = new ObservableField<>(View.GONE);
    public ObservableField<Integer> healingPropertiesVisibility = new ObservableField<>(View.GONE);
    public ObservableField<Integer> dosageVisibility = new ObservableField<>(View.GONE);
    public ObservableField<Integer> weightVisibility = new ObservableField<>(View.GONE);
    public ObservableField<Integer> volumeVisibility = new ObservableField<>(View.GONE);
    public ObservableField<Integer> tasteVisibility = new ObservableField<>(View.GONE);

    private ArrayList<Product> productArrayList;
    private Bundle arguments;
    private ShowPhotoViewActions showPhotoViewActions;

    @Inject
    public ProductDetailsViewModel(ProductDetailsUseCaseImpl productDetailsUseCase) {
        useCase = productDetailsUseCase;
    }

    public void setShowPhotoViewActions(ShowPhotoViewActions showPhotoViewActions) {
        this.showPhotoViewActions = showPhotoViewActions;
    }

    public void showProductData(Product product, int productListSize) {
        Resources resources = useCase.getResources();
        String yes = resources.getString(R.string.general_yes);
        String no = resources.getString(R.string.general_no);
        String expirationDateInShowFormat = "", productionDateInShowFormat = "";

        if(!Objects.equals(product.getExpirationDate(), "-"))
            expirationDateInShowFormat = useCase.getDateInFormatToShow(product.getExpirationDate());
        if(!Objects.equals(product.getProductionDate(), "-"))
            productionDateInShowFormat = useCase.getDateInFormatToShow(product.getProductionDate());

        showPhoto(product);

        productName.setValue(product.getName());
        mainCategory.setValue(product.getMainCategory());
        detailCategory.setValue(product.getDetailCategory());
        expirationDate.setValue(expirationDateInShowFormat);
        productionDate.setValue(productionDateInShowFormat);
        storageLocation.setValue(product.getStorageLocation());
        quantity.setValue(String.valueOf(productListSize));
        composition.setValue(product.getComposition());
        healingProperties.setValue(product.getHealingProperties());
        dosage.setValue(product.getDosage());
        weight.setValue(String.valueOf(product.getWeight()));
        volume.setValue(String.valueOf(product.getVolume()));
        taste.setValue(product.getTaste());

        if (product.getIsVege())
            isVege.setValue(yes);
        else
            isVege.setValue(no);

        if (product.getIsBio())
            isBio.setValue(yes);
        else
            isBio.setValue(no);

        if (product.getHasSugar())
            hasSugar.setValue(yes);
        else
            hasSugar.setValue(no);

        if (product.getHasSalt())
            hasSalt.setValue(yes);
        else
            hasSalt.setValue(no);

        setVisibleFieldsIfNotEmpty(product);
    }

    private void setVisibleFieldsIfNotEmpty(Product product) {
        if (!product.getPhotoName().equals(""))
            photoVisibility.set(View.VISIBLE);
        if (!product.getDetailCategory().equals(""))
            detailCategoryVisibility.set(View.VISIBLE);
        if (!product.getExpirationDate().equals(""))
            expirationDateVisibility.set(View.VISIBLE);
        if (!product.getProductionDate().equals(""))
            productionDateVisibility.set(View.VISIBLE);
        if (!product.getStorageLocation().equals(""))
            storageLocationVisibility.set(View.VISIBLE);
        if (!product.getComposition().equals(""))
            compositionVisibility.set(View.VISIBLE);
        if (!product.getHealingProperties().equals(""))
            healingPropertiesVisibility.set(View.VISIBLE);
        if (!product.getDosage().equals(""))
            dosageVisibility.set(View.VISIBLE);
        if (product.getWeight() > 0)
            weightVisibility.set(View.VISIBLE);
        if (product.getVolume() > 0)
            volumeVisibility.set(View.VISIBLE);
        if (!product.getTaste().equals(""))
            tasteVisibility.set(View.VISIBLE);
    }

    public ArrayList<Product> getProductArrayList() {
        return productArrayList;
    }

    public void onClickDeleteProduct() {
        useCase.deleteProducts(productArrayList);
    }

    public void setDatabaseMode(DatabaseMode databaseMode) {
        useCase.setDatabaseMode(databaseMode);
    }

    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }

    public void showProductDataIfExists(){
        if(arguments != null){
            int productId = arguments.getInt("productId");
            String productHashCode = arguments.getString("productHashCode");
            productArrayList = arguments.getParcelableArrayList("productArrayList");
            for(Product product : productArrayList){
                if((product.getId() == productId) && productHashCode.equals(product.getHashCode()))
                    showProductData(product, productArrayList.size());
            }
        }
    }

    private void showPhoto(Product product) {
        String photoName = product.getPhotoName();
        if(!photoName.equals("")) {
            useCase.setPhotoFile(photoName);
            Bitmap bitmap = useCase.getPhotoBitmap();
            showPhotoViewActions.showPhoto(product, bitmap);
        }
    }
}