package com.hermanowicz.pantry.ui.product_details;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.domain.repository.PhotoRepository;
import com.hermanowicz.pantry.domain.repository.ProductRepository;
import com.hermanowicz.pantry.domain.usecase.ProductDetailsUseCase;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.util.Notifications;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

public class ProductDetailsUseCaseImpl implements ProductDetailsUseCase {

    private final ProductRepository productRepository;
    private final PhotoRepository photoRepository;
    private final Resources resources;
    private final Notifications notifications;
    private DatabaseMode databaseMode;

    @Inject
    public ProductDetailsUseCaseImpl(ProductRepository productRepository, PhotoRepository photoRepository, Resources resources, Notifications notifications) {
        this.productRepository = productRepository;
        this.photoRepository = photoRepository;
        this.resources = resources;
        this.notifications = notifications;
    }

    @Override
    public void setPhotoFile(String photoName) {
        if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL)
            photoRepository.setPhotoFileFromOfflineDb(photoName);
        else if (databaseMode.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            photoRepository.setPhotoFileFromOnlineDb(photoName);
    }

    @Override
    public Bitmap getPhotoBitmap() {
        return photoRepository.getPhotoBitmap();
    }

    @Override
    public Resources getResources() {
        return resources;
    }

    @Override
    public void setDatabaseMode(DatabaseMode databaseMode) {
        this.databaseMode = databaseMode;
    }

    @Override
    public void deleteProducts(ArrayList<Product> productArrayList) {
        productRepository.delete(productArrayList, databaseMode);
        notifications.cancelNotifications(productArrayList);
    }

    @Override
    public String getDateInFormatToShow(String dateString) {
        String[] dateArrayString = dateString.split("-");
        if (dateArrayString.length < 2)
            dateArrayString = dateString.split("\\.");
        if (dateArrayString.length > 2) {
            int year = Integer.parseInt(dateArrayString[0]);
            int month = Integer.parseInt(dateArrayString[1]);
            int day = Integer.parseInt(dateArrayString[2]);

            DateFormat dateFormat = DateFormat.getDateInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            return dateFormat.format(date);
        } else
            return "";
    }
}