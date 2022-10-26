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

package com.hermanowicz.pantry.model;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.data.db.dao.product.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>GroupProducts</h1>
 * Model class for group product
 *
 * @author Mateusz Hermanowicz
 */

public final class GroupProduct {

    private Product secondProductToCompare;
    private int quantity;

    public GroupProduct(@NonNull Product product, int quantity) {
        this.secondProductToCompare = product;
        this.quantity = quantity;
    }

    public static List<GroupProduct> getGroupProducts(List<Product> productList) {
        List<GroupProduct> groupProductList = new ArrayList<>();
        if (productList != null) {
            List<GroupProduct> toAddGroupProductList = new ArrayList<>();
            List<GroupProduct> toRemoveGroupProductList = new ArrayList<>();
            for (Product product : productList) {
                if (product.getPhotoName() == null)
                    product.setPhotoName("");
                if (product.getPhotoDescription() == null)
                    product.setPhotoDescription("");
                GroupProduct testedGroupProduct = getGroupIfOnList(product, groupProductList);

                if (testedGroupProduct != null) {
                    toRemoveGroupProductList.add(testedGroupProduct);
                    testedGroupProduct.setQuantity(testedGroupProduct.getQuantity() + 1);
                    toAddGroupProductList.add(testedGroupProduct);
                } else {
                    GroupProduct newGroupProduct = new GroupProduct(product, 1);
                    toAddGroupProductList.add(newGroupProduct);
                }
                groupProductList.removeAll(toRemoveGroupProductList);
                groupProductList.addAll(toAddGroupProductList);
                toAddGroupProductList.clear();
                toRemoveGroupProductList.clear();
            }
        }
        return groupProductList;
    }

    public static String[] getGroupProductNamesArray(List<Product> productList) {
        List<GroupProduct> groupProductList = getGroupProducts(productList);
        String[] groupProductNamesArray = new String[groupProductList.size()];
        for (int counter = 0; counter <= groupProductList.size(); counter++) {
            groupProductNamesArray[counter] = groupProductList.get(counter).getProduct().getName();
        }
        return groupProductNamesArray;
    }

    private static GroupProduct getGroupIfOnList(Product product, List<GroupProduct> groupProductList) {
        GroupProduct groupProductReturned = null;
        for (GroupProduct groupProduct : groupProductList) {
            Product productToCompare = groupProduct.getProduct();
            if (isProductsSimilar(product, productToCompare))
                groupProductReturned = groupProduct;
        }
        return groupProductReturned;
    }

    private static boolean isProductsSimilar(Product firstProductToCompare, Product secondProductToCompare) {
        return firstProductToCompare.getName().toLowerCase().contains(secondProductToCompare.getName().toLowerCase())
                && firstProductToCompare.getMainCategory().equals(secondProductToCompare.getMainCategory())
                && firstProductToCompare.getDetailCategory().equals(secondProductToCompare.getDetailCategory())
                && firstProductToCompare.getExpirationDate().equals(secondProductToCompare.getExpirationDate())
                && firstProductToCompare.getProductionDate().equals(secondProductToCompare.getProductionDate())
                && firstProductToCompare.getStorageLocation().equals(secondProductToCompare.getStorageLocation())
                && firstProductToCompare.getComposition().equals(secondProductToCompare.getComposition())
                && firstProductToCompare.getHealingProperties().equals(secondProductToCompare.getHealingProperties())
                && firstProductToCompare.getDosage().equals(secondProductToCompare.getDosage())
                && firstProductToCompare.getVolume() == secondProductToCompare.getVolume()
                && firstProductToCompare.getWeight() == secondProductToCompare.getWeight()
                && firstProductToCompare.getIsVege() == secondProductToCompare.getIsVege()
                && firstProductToCompare.getIsBio() == secondProductToCompare.getIsBio()
                && firstProductToCompare.getHasSugar() == secondProductToCompare.getHasSugar()
                && firstProductToCompare.getHasSalt() == secondProductToCompare.getHasSalt()
                && firstProductToCompare.getBarcode().equals(secondProductToCompare.getBarcode())
                && firstProductToCompare.getPhotoName().equals(secondProductToCompare.getPhotoName())
                && firstProductToCompare.getPhotoDescription().equals(secondProductToCompare.getPhotoDescription())
                && firstProductToCompare.getTaste().equals(secondProductToCompare.getTaste());
    }

    public Product getProduct() {
        return secondProductToCompare;
    }

    public void setProduct(Product product) {
        this.secondProductToCompare = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}