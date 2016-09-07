/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.geemvc.converter.adapter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.geemvc.Str;
import com.geemvc.annotation.Adapter;
import com.geemvc.converter.ConverterAdapter;
import com.geemvc.converter.ConverterContext;
import com.google.inject.Inject;
import com.google.inject.Injector;

import jodd.typeconverter.TypeConverter;
import jodd.typeconverter.impl.DateConverter;

@Adapter
public class DateConverterAdapter implements ConverterAdapter<Date>, TypeConverter<Date> {
    @Inject
    protected Injector injector;

    protected Pattern datePattern = Pattern.compile("\\d\\d\\d\\d\\-\\d\\d\\-\\d\\d(\\+\\d\\d:\\d\\d)?");
    protected Pattern dateTimePattern = Pattern.compile("\\d\\d\\d\\d\\-\\d\\d\\-\\d\\dT\\d\\d:\\d\\d:\\d\\d(\\+\\d\\d:\\d\\d)?");
    protected DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
    protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
    protected DateConverter joddDateConverter = new DateConverter();

    @Override
    public boolean canConvert(List<String> values, ConverterContext ctx) {
        if (values.isEmpty() || values.size() > 1)
            return false;

        for (String val : values) {
            Matcher dateMatcher = datePattern.matcher(val);

            if (!dateMatcher.matches()) {
                Matcher dateTimeMatcher = dateTimePattern.matcher(val);

                if (!dateTimeMatcher.matches())
                    return false;
            }
        }

        return true;
    }

    @Override
    public Date fromStrings(List<String> values, ConverterContext ctx) {
        String dateString = values.get(0);
        Date date = null;

        Matcher dateMatcher = datePattern.matcher(dateString);

        if (dateMatcher.matches()) {
            LocalDate localDate = LocalDate.parse(dateString, dateFormatter);
            date = java.sql.Date.valueOf(localDate);
        } else {
            LocalDateTime localDate = LocalDateTime.parse(dateString, dateTimeFormatter);
            date = Timestamp.valueOf(localDate);
        }

        return date;
    }

    @Override
    public String toString(Date value) {
        return value.toString();
    }

    /**
     * Conversion method for jodd type conversion.
     */
    @Override
    public Date convert(Object value) {
        if (value instanceof String) {
            String dateString = (String) value;

            if (Str.isEmpty(dateString)) {
                return null;
            }

            List<String> dateInParamList = Arrays.asList(dateString);

            if (canConvert(dateInParamList, null)) {
                return fromStrings(dateInParamList, null);
            } else {
                return joddDateConverter.convert(value);
            }
        }

        return joddDateConverter.convert(value);
    }
}
