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

package com.hermanowicz.pantry.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {

    public static String getDateInFormatToShow(int day, int month, int year) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

    public static String getDateInFormatToShow(String dateString) {
        String[] dateArrayString = dateString.split("\\.");
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