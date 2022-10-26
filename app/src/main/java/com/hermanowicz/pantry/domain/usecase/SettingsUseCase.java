/*
 * Copyright (c) 2019-2022
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.domain.usecase;

import android.app.Activity;
import android.content.Context;

import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.interfaces.PricingListener;
import com.hermanowicz.pantry.util.PremiumAccess;

import java.util.List;

public interface SettingsUseCase {
    void setPremiumActivationListenerAndSetupBillingClient(Context context, PricingListener pricingListener);

    PremiumAccess getPremiumAccess();

    void initPremiumPurchase(Activity activity);

    void activatePremiumAccess();

    String getAppVersion();

    String getActiveUserEmail();

    void importLocalDataToCloud();

    void backupProductDbToFile();

    void restoreProductDbFromFile();

    void backupOwnCategoriesDbToFile();

    void restoreOwnCategoriesDbFromFile();

    void backupStorageLocationsDbToFile();

    void restoreStorageLocationDbFromFile();

    void clearProductDb();

    void clearOwnCategoriesDb();

    void clearStorageLocationsDb();

    void restoreNotificationsIfNeeded();

    void setNotificationsToRestoreFlag();

    void setProductList(List<Product> productList);
}