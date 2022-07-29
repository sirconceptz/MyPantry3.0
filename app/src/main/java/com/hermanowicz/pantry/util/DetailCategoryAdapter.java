package com.hermanowicz.pantry.util;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ArrayAdapter;

import com.hermanowicz.pantry.R;

public class DetailCategoryAdapter {

    public static ArrayAdapter<CharSequence> getDetailCategoryAdapter(
            Context context,
            String productTypeSpinnerValue,
            String[] ownCategories){

        ArrayAdapter<CharSequence> detailCategoryAdapter = null;
        Resources resources = context.getResources();
        String[] productTypes = resources.getStringArray(R.array.Product_type_of_product_array);

        if (productTypeSpinnerValue.equals(productTypes[1]))
            detailCategoryAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, ownCategories);
        else if (productTypeSpinnerValue.equals(productTypes[2]))
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_store_products_array, android.R.layout.simple_spinner_dropdown_item);
        else if (productTypeSpinnerValue.equals(productTypes[3]))
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_ready_meals_array, android.R.layout.simple_spinner_dropdown_item);
        else if (productTypeSpinnerValue.equals(productTypes[4]))
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_vegetables_array, android.R.layout.simple_spinner_dropdown_item);
        else if (productTypeSpinnerValue.equals(productTypes[5]))
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_fruits_array, android.R.layout.simple_spinner_dropdown_item);
        else if (productTypeSpinnerValue.equals(productTypes[6]))
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_herbs_array, android.R.layout.simple_spinner_dropdown_item);
        else if (productTypeSpinnerValue.equals(productTypes[7]))
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_liqueurs_array, android.R.layout.simple_spinner_dropdown_item);
        else if (productTypeSpinnerValue.equals(productTypes[8]))
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_wines_type_array, android.R.layout.simple_spinner_dropdown_item);
        else if (productTypeSpinnerValue.equals(productTypes[9]))
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_mushrooms_array, android.R.layout.simple_spinner_dropdown_item);
        else if (productTypeSpinnerValue.equals(productTypes[10]))
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_vinegars_array, android.R.layout.simple_spinner_dropdown_item);
        else if (productTypeSpinnerValue.equals(productTypes[11]))
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_chemical_products_array, android.R.layout.simple_spinner_dropdown_item);
        else if (productTypeSpinnerValue.equals(productTypes[12]))
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_other_products_array, android.R.layout.simple_spinner_dropdown_item);
        else
            detailCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_choose_array, android.R.layout.simple_spinner_dropdown_item);

        return detailCategoryAdapter;
    }
}
