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

import com.geemvc.annotation.Adapter;
import com.geemvc.converter.ConverterAdapter;
import com.geemvc.converter.ConverterContext;
import com.geemvc.converter.SimpleConverter;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Adapter
public class LongArrayConverterAdapter implements ConverterAdapter<long[]> {
    @Inject
    protected Injector injector;

    @Override
    public boolean canConvert(List<String> values, ConverterContext ctx) {
        return true;
    }

    @Override
    public long[] fromStrings(List<String> values, ConverterContext ctx) {
        SimpleConverter simpleConverter = injector.getInstance(SimpleConverter.class);

        long[] returnValue = null;

        Class<?> type = ctx.type();
        Object arr = Array.newInstance(type.getComponentType(), values.size());

        int x = 0;
        for (String val : values) {
            Array.set(arr, x++, simpleConverter.fromString(val, type.getComponentType()));
        }

        returnValue = (long[]) arr;

        return returnValue;
    }

    @Override
    public String toString(long[] value) {
        return null;
    }
}
