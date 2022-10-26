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

import com.hermanowicz.pantry.data.db.dao.product.Product;
import com.hermanowicz.pantry.interfaces.ScanDecodedResult;
import com.hermanowicz.pantry.util.ScanIntentResult;
import com.hermanowicz.pantry.util.ScanOptions;

import java.util.List;

public interface ScanProductUseCase {
    void setProductList(List<Product> productList);

    void setBarcodeToProductList(String barcode);

    void setScanType(String scanType);

    void setScanResult(ScanIntentResult result);

    void setScanDecodedResultListener(ScanDecodedResult scanDecodedResult);

    void setBarcode(String barcode);

    ScanOptions getScanOptions();
}
