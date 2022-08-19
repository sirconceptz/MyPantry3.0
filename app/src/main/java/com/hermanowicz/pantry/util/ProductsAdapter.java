/*
 * Copyright (c) 2019-2022
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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dao.db.product.Product;
import com.hermanowicz.pantry.databinding.RvSingleGroupProductBinding;
import com.hermanowicz.pantry.model.GroupProduct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>ProductsAdapter</h1>
 * View's adapter for products view
 *
 * @author Mateusz Hermanowicz
 */

public class ProductsAdapter extends
        RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private RvSingleGroupProductBinding binding;

    private static final String PREFERENCES_DAYS_TO_NOTIFICATIONS = "HOW_MANY_DAYS_BEFORE_EXPIRATION_DATE_SEND_A_NOTIFICATION?";

    private List<GroupProduct> productList = new ArrayList<>();
    private List<Product> multiSelectList = new ArrayList<>();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SharedPreferences preferences;
    int itemAnimPosition = -1;

    public ProductsAdapter(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void setData(@NonNull List<GroupProduct> newData) {
        this.productList = newData;
        notifyDataSetChanged();
    }

    public void setMultiSelectList(@NonNull List<Product> multiSelectList) {
        this.multiSelectList = multiSelectList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TextView nameTv = binding.textProductName;
        TextView quantity = binding.textProductQuantity;
        TextView weightTv = binding.textProductWeight;
        TextView volumeTv = binding.textProductVolume;
        TextView expirationDateTv = binding.textExpirationDateValue;
        TextView hasSugar = binding.textHasSugar;
        TextView hasSalt = binding.textHasSalt;
        TextView isVege = binding.textIsVege;
        TextView isBio = binding.textIsBio;

        Context context = nameTv.getContext();
        Resources resources = context.getResources();
        final Product product = productList.get(position).getProduct();
        String quantityString = String.format("%s: %s", resources.getString(R.string.product_quantity), productList.get(position).getQuantity());
        String weightString = String.format("%s: %s%s", resources.getString(R.string.product_weight), product.getWeight(), resources.getString(R.string.product_weight_unit));
        String volumeString = String.format("%s: %s%s", resources.getString(R.string.product_volume), product.getVolume(), resources.getString(R.string.product_volume_unit));

        nameTv.setText(product.getShortName());
        quantity.setText(quantityString);
        weightTv.setText(weightString);
        volumeTv.setText(volumeString);
        if (product.getHasSugar())
            hasSugar.setVisibility(View.VISIBLE);
        else
            hasSugar.setVisibility(View.GONE);
        if (product.getHasSalt())
            hasSalt.setVisibility(View.VISIBLE);
        else
            hasSalt.setVisibility(View.GONE);
        if (product.getIsVege())
            isVege.setVisibility(View.VISIBLE);
        else
            isVege.setVisibility(View.GONE);
        if (product.getIsBio())
            isBio.setVisibility(View.VISIBLE);
        else
            isBio.setVisibility(View.GONE);
        if (product.getExpirationDate().length() > 1) {
            DateHelper dateHelper = new DateHelper(product.getExpirationDate());
            expirationDateTv.setText(dateHelper.getDateInLocalFormat());
        } else {
            expirationDateTv.setText(product.getExpirationDate());
        }

        if (viewHolder.getAdapterPosition() > itemAnimPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
            viewHolder.itemView.startAnimation(animation);
            itemAnimPosition = viewHolder.getAdapterPosition();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        binding = RvSingleGroupProductBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(binding.getRoot());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}