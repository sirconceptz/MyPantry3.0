/*
 * Copyright (c) 2019-2021
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.receiver.NotificationBroadcastReceiver;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * <h1>Notification</h1>
 * Class to support the notification in the application.
 *
 * @author Mateusz Hermanowicz
 */

public class Notifications {

    public static final int NOTIFICATION_DEFAULT_HOUR = 12;
    public static final int NOTIFICATION_DEFAULT_DAYS = 3;
    private static final String PREFERENCES_DAYS_TO_NOTIFICATIONS = "HOW_MANY_DAYS_BEFORE_EXPIRATION_DATE_SEND_A_NOTIFICATION?";

    private final Context context;
    private List<Product> productList;

    public Notifications(@NonNull Context context) {
        this.context = context;
    }

    private Calendar createCalendar(@NonNull String expirationDate) {
        String[] dateArray = expirationDate.split("\\.");
        Calendar calendar = null;
        if(dateArray.length < 3) {
            calendar = Calendar.getInstance();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
            calendar.set(Calendar.MONTH, (Integer.parseInt(dateArray[1])) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]));
            calendar.set(Calendar.HOUR_OF_DAY, Notifications.NOTIFICATION_DEFAULT_HOUR);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.DAY_OF_MONTH, -preferences.getInt(
                    PREFERENCES_DAYS_TO_NOTIFICATIONS, Notifications.NOTIFICATION_DEFAULT_DAYS));
        }

        return calendar;
    }

    private void createNotification(Product product) {
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        assert product != null;
        intent.putExtra("product_name", product.getName());
        intent.putExtra("product_id", product.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, product.getId(), intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (!product.getExpirationDate().equals("-")) {
            Calendar calendar = createCalendar(product.getExpirationDate());
            if(calendar != null && alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    public void createNotification(@NonNull List<Product> productList) {
        for(Product product : productList) {
            createNotification(product);
        }
    }

    public void createNotificationsForAllProducts() {
        if(productList.size() > 0) {
            for (int counter = 0; counter < productList.size(); counter++) {
                Product selectedProduct = productList.get(counter);
                String productExpiration = selectedProduct.getExpirationDate();
                productExpiration = productExpiration.replace(".", "-");
                if (!productExpiration.equals("-")) {
                    Date expirationDate = Date.valueOf(productExpiration);
                    Date currentTime = new Date(System.currentTimeMillis());
                    if (expirationDate.after(currentTime))
                        createNotification(selectedProduct);
                }
            }
        }
    }

    public void cancelNotifications(@NonNull List<Product> productList) {
        if(productList.size() == 0)
            return;
        if (!productList.get(0).getExpirationDate().equals("-")) {
            for(Product product : productList) {
                AlarmManager alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
                Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, product.getId(), intent, PendingIntent.FLAG_IMMUTABLE);
                pendingIntent.cancel();
                assert alarmManager != null;
                alarmManager.cancel(pendingIntent);
            }
        }
    }

    public void setProductList(@NonNull List<Product> productList) {
        this.productList = productList;
    }

    public void cancelNotificationsForAllProducts() {
        cancelNotifications(productList);
    }
}