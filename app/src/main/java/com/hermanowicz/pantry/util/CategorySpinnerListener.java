package com.hermanowicz.pantry.util;

import android.view.View;
import android.widget.AdapterView;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.R;

public class CategorySpinnerListener implements AdapterView.OnItemSelectedListener {

    public MutableLiveData<String> mainCategoryOut;
    public MutableLiveData<String> detailCategoryOut;
    public ObservableField<Integer> detailCategoryVisibilityOut;

    public CategorySpinnerListener(MutableLiveData<String> mainCategoryOut,
                                   MutableLiveData<String> detailCategoryOut,
                                   ObservableField<Integer> detailCategoryVisibilityOut){
        this.mainCategoryOut = mainCategoryOut;
        this.detailCategoryOut = detailCategoryOut;
        this.detailCategoryVisibilityOut = detailCategoryVisibilityOut;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selected = parent.getId();
        if (selected == R.id.mainCategoryInput)
            setMainCategoryIfChanged(parent, position);
        else if(selected == R.id.detailCategoryInput)
            setDetailCategoryIfChanged(parent, position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void setMainCategoryIfChanged(AdapterView<?> parent, int position) {
        if (position == 0) { // no category
            mainCategoryOut.setValue("");
            detailCategoryOut.setValue("");
            detailCategoryVisibilityOut.set(View.GONE);
        } else {
            mainCategoryOut.setValue((String) parent.getItemAtPosition(position));
            detailCategoryOut.setValue("");
            detailCategoryVisibilityOut.set(View.VISIBLE);
        }
    }

    private void setDetailCategoryIfChanged(AdapterView<?> parent, int position) {
        if (position == 0)
            detailCategoryOut.setValue("");
        else
            detailCategoryOut.setValue((String) parent.getItemAtPosition(position));
    }
}