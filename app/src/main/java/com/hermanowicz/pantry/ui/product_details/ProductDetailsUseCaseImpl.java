package com.hermanowicz.pantry.ui.product_details;

import android.content.res.Resources;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.repository.ProductRepository;
import com.hermanowicz.pantry.util.Notifications;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

public class ProductDetailsUseCaseImpl implements ProductDetailsUseCase {

    private final ProductRepository repository;
    private final Resources resources;
    private final Notifications notifications;
    private DatabaseMode databaseMode;

    @Inject
    public ProductDetailsUseCaseImpl(ProductRepository productRepository, Resources resources, Notifications notifications) {
        this.repository = productRepository;
        this.resources = resources;
        this.notifications = notifications;
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
        repository.delete(productArrayList, databaseMode);
        notifications.cancelNotifications(productArrayList);
    }

    @Override
    public String getDateInFormatToShow(String dateString) {
        String[] dateArrayString = dateString.split("-");
        int year = Integer.parseInt(dateArrayString[0]);
        int month = Integer.parseInt(dateArrayString[1]);
        int day = Integer.parseInt(dateArrayString[2]);

        DateFormat dateFormat = DateFormat.getDateInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }
}