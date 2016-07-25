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

package com.geemvc.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import jodd.typeconverter.TypeConverter;
import jodd.typeconverter.TypeConverterManager;

public class DefaultSimpleConverter implements SimpleConverter {
    @Override
    public Object fromString(String value, Class<?> toClass) {
        return fromString(value, toClass, (Object[]) null);
    }

    @Override
    public Object fromString(String value, Class<?> toClass, Object... options) {
        Object o = null;

        TypeConverter<?> typeConverter = TypeConverterManager.lookup(toClass);

        if (typeConverter != null) {
            o = typeConverter.convert(value);
        } else if (hasValueOfMethod(toClass)) {
            Method m;

            try {
                m = toClass.getMethod("valueOf", String.class);
                m.setAccessible(true);
                o = m.invoke(null, value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            }
        } else if (hasFromString(toClass)) {
            Method m;

            try {
                m = toClass.getMethod("fromString", String.class);
                m.setAccessible(true);
                o = m.invoke(null, value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            }
        } else if (hasSingleStringConstructor(toClass)) {
            Constructor<?> c;
            try {
                c = toClass.getConstructor(String.class);
                c.setAccessible(true);
                o = c.newInstance(value);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            }
        }

        return o;
    }

    @Override
    public String toString(Object value) {
        return String.valueOf(value);
    }

    @Override
    public boolean canConvert(Class<?> toClass) {
        return TypeConverterManager.lookup(toClass) != null || hasValueOfMethod(toClass) || hasFromString(toClass) || hasAsString(toClass) || hasSingleStringConstructor(toClass);
    }

    protected boolean hasValueOfMethod(Class<?> clazz) {
        Method m;

        try {
            m = clazz.getMethod("valueOf", String.class);
        } catch (NoSuchMethodException | SecurityException e) {
            return false;
        }

        return m != null && Modifier.isStatic(m.getModifiers());
    }

    protected boolean hasFromString(Class<?> clazz) {
        Method m;

        try {
            m = clazz.getMethod("fromString", String.class);
        } catch (NoSuchMethodException | SecurityException e) {
            return false;
        }

        return m != null && Modifier.isStatic(m.getModifiers());
    }

    protected boolean hasAsString(Class<?> clazz) {
        Method m;

        try {
            m = clazz.getMethod("asString");
        } catch (NoSuchMethodException | SecurityException e) {
            return false;
        }

        return m != null && Modifier.isStatic(m.getModifiers());
    }

    protected boolean hasSingleStringConstructor(Class<?> clazz) {
        Constructor<?> c;

        try {
            c = clazz.getConstructor(String.class);
        } catch (NoSuchMethodException | SecurityException e) {
            return false;
        }

        return c != null;
    }
}
