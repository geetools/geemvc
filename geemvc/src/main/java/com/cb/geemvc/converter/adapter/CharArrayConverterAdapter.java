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

package com.cb.geemvc.converter.adapter;

import java.lang.reflect.Array;
import java.util.List;

import com.cb.geemvc.annotation.Adapter;
import com.cb.geemvc.converter.ConverterAdapter;
import com.cb.geemvc.converter.ConverterContext;
import com.cb.geemvc.converter.SimpleConverter;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Adapter
public class CharArrayConverterAdapter implements ConverterAdapter<char[]> {
    @Inject
    Injector injector;

    @Override
    public char[] fromStrings(List<String> values, ConverterContext ctx) {
        SimpleConverter simpleConverter = injector.getInstance(SimpleConverter.class);

        char[] returnValue = null;

        Class<?> type = ctx.type();
        Object arr = Array.newInstance(type.getComponentType(), values.size());

        int x = 0;
        for (String val : values) {
            Array.set(arr, x++, simpleConverter.fromString(val, type.getComponentType()));
        }

        returnValue = (char[]) arr;

        return returnValue;
    }

    @Override
    public String toString(char[] value) {
        return null;
    }
}
