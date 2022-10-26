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

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <h1>DateHelper</h1>
 * Class used to get date in specific formats
 *
 * @author Mateusz Hermanowicz
 */

public class DateHelper {

    private final Calendar calendar = Calendar.getInstance();
    private final DateFormat localDateFormat = DateFormat.getDateInstance();
    private String[] dateArray;

    public DateHelper(@NonNull String date) {
        dateArray = date.split("\\.");
        if (dateArray.length < 2)
            dateArray = date.split("-");
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

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

    public String getDateInLocalFormat() {
        if (dateArray.length >= 2) {
            calendar.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]) - 1, Integer.parseInt(dateArray[2]));
            Date date = calendar.getTime();
            return localDateFormat.format(date);
        } else
            return "-";
    }
}