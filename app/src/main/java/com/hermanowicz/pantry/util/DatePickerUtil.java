package com.hermanowicz.pantry.util;

import androidx.databinding.ObservableField;

import java.util.Calendar;

public class DatePickerUtil {

    public static void resetDateInDatePicker(
            ObservableField<Integer> year,
            ObservableField<Integer> month,
            ObservableField<Integer> day
    ) {
        Calendar now = Calendar.getInstance();
        year.set(now.get(Calendar.YEAR));
        month.set(now.get(Calendar.MONTH));
        day.set(now.get(Calendar.DAY_OF_MONTH));
    }
}
