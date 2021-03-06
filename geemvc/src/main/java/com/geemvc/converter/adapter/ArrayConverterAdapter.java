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

import java.lang.reflect.Array;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.geemvc.Char;
import com.geemvc.annotation.Adapter;
import com.geemvc.converter.ConverterAdapter;
import com.geemvc.converter.ConverterContext;
import com.geemvc.converter.SimpleConverter;
import com.geemvc.converter.bean.BeanConverterAdapter;
import com.geemvc.converter.bean.BeanConverterAdapterFactory;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Adapter
@Singleton
public class ArrayConverterAdapter implements ConverterAdapter<Object[]> {
    @Inject
    protected BeanConverterAdapterFactory beanConverterAdapterFactory;

    @Logger
    protected Log log;

    @Inject
    protected Injector injector;

    @Override
    public boolean canConvert(List<String> values, ConverterContext ctx) {
        return true;
    }

    @Override
    public Object[] fromStrings(List<String> values, ConverterContext ctx) {
        SimpleConverter simpleConverter = injector.getInstance(SimpleConverter.class);

        Object[] returnValue = null;

        String name = ctx.name();
        Class<?> type = ctx.type();

        if (type.isArray() && type.getComponentType() == String.class) {
            returnValue = values.toArray(new String[values.size()]);
        } else if (type.isArray() && simpleConverter.canConvert(type.getComponentType())) {
            Object arr = Array.newInstance(type.getComponentType(), values.size());

            int x = 0;
            for (String val : values) {
                Array.set(arr, x++, simpleConverter.fromString(val, type.getComponentType()));
            }

            returnValue = (Object[]) arr;
        } else if (type.isArray()) {
            Set<Integer> arrayPositions = arraysPositions(values);
            Object arr = Array.newInstance(type.getComponentType(), arrayPositions.size());

            BeanConverterAdapter beanConverter = beanConverterAdapterFactory.create(type.getComponentType(), null);

            if (beanConverter == null) {
                log.warn("Unable to find a compatible bean converter for the bean '{}' while attempting to bind values in array.", type.getComponentType());
                return null;
            }

            for (Integer index : arrayPositions) {
                Object bean = beanConverter.fromStrings(values, name, type.getComponentType(), index, ctx);
                Array.set(arr, index, bean);
            }

            returnValue = (Object[]) arr;
        }

        return returnValue;
    }

    @Override
    public String toString(Object[] value) {
        return null;
    }

    protected Set<Integer> arraysPositions(List<String> value) {
        if (value == null || value.size() == 0)
            return null;

        Set<Integer> arraysPositions = new TreeSet<>();

        for (String val : value) {
            int equalsPos = val.indexOf(Char.EQUALS);
            String propertyExpression = val.substring(0, equalsPos);

            int openSquareBrackenPos = val.indexOf(Char.SQUARE_BRACKET_OPEN);
            int closeSquareBrackenPos = val.indexOf(Char.SQUARE_BRACKET_CLOSE);

            arraysPositions.add(Integer.valueOf(propertyExpression.substring(openSquareBrackenPos + 1, closeSquareBrackenPos)));
        }

        return arraysPositions;
    }
}
