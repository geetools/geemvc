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

package com.cb.geemvc.converter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cb.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class DefaultConverterAdapterFactory implements ConverterAdapterFactory {
    protected final ReflectionProvider reflectionProvider;

    @Inject
    protected Injector inject;

    @Inject
    public DefaultConverterAdapterFactory(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ConverterAdapter<T> create(Class<T> type, Type parameterizedType) {
        List<Class<?>> genericTypes = reflectionProvider.getGenericType(parameterizedType);

        Map<ConverterAdapterKey, ConverterAdapter<?>> converterAdapters = reflectionProvider.locateConverterAdapters();
        Set<ConverterAdapterKey> keys = converterAdapters.keySet();

        ConverterAdapter<T> converterAdapter = null;

        // ------------------------------------------------------------------------------
        // First we try to find converters that have exact type and generic
        // types match.
        // ------------------------------------------------------------------------------

        for (ConverterAdapterKey key : keys) {
            ConverterAdapterKey lookupKey = inject.getInstance(ConverterAdapterKey.class).build(type, genericTypes);

            if (key.equals(lookupKey)) {
                converterAdapter = (ConverterAdapter<T>) converterAdapters.get(key);
                break;
            }
        }

        // ------------------------------------------------------------------------------
        // If no exact match could be found, we try to find converters where all
        // types are assignable but ignore generic types Object for now.
        // ------------------------------------------------------------------------------

        if (converterAdapter == null) {
            for (ConverterAdapterKey key : keys) {
                ConverterAdapterKey lookupKey = inject.getInstance(ConverterAdapterKey.class).build(type, genericTypes);

                if (key.type() != Object.class && key.type() != Object[].class && key.type().isAssignableFrom(lookupKey.type())) {
                    if ((lookupKey.genericTypes() == null || lookupKey.genericTypes().size() == 0) && (key.genericTypes() == null || key.genericTypes().size() == 0)) {
                        converterAdapter = (ConverterAdapter<T>) converterAdapters.get(key);
                        break;
                    } else {
                        List<Class<?>> keyGenericTypes = key.genericTypes();
                        List<Class<?>> lookupGenericTypes = lookupKey.genericTypes();

                        boolean genericTypesMatch = false;

                        for (int i = 0; i < keyGenericTypes.size(); i++) {
                            Class<?> genericType = keyGenericTypes.get(i);

                            Class<?> lookupGenericType = null;

                            if (lookupGenericTypes != null && lookupGenericTypes.size() >= keyGenericTypes.size())
                                lookupGenericType = lookupGenericTypes.get(i);

                            if (genericType == Object.class || lookupGenericType == null) {
                                genericTypesMatch = false;
                                break;
                            }

                            if (genericType.isAssignableFrom(lookupGenericType)) {
                                genericTypesMatch = true;
                                break;
                            } else {
                                genericTypesMatch = false;
                                break;
                            }
                        }

                        if (genericTypesMatch)
                            converterAdapter = (ConverterAdapter<T>) converterAdapters.get(key);
                    }
                }
            }
        }

        // ------------------------------------------------------------------------------
        // Same as above but now also include unspecific Object types.
        // ------------------------------------------------------------------------------

        if (converterAdapter == null) {
            for (ConverterAdapterKey key : keys) {
                ConverterAdapterKey lookupKey = inject.getInstance(ConverterAdapterKey.class).build(type, genericTypes);

                if (key.type().isAssignableFrom(lookupKey.type())) {
                    List<Class<?>> keyGenericTypes = key.genericTypes();
                    List<Class<?>> lookupGenericTypes = lookupKey.genericTypes();

                    if ((lookupKey.genericTypes() == null || lookupKey.genericTypes().size() == 0) && (key.genericTypes() == null || key.genericTypes().size() == 0)) {
                        converterAdapter = (ConverterAdapter<T>) converterAdapters.get(key);
                        break;
                    } else {
                        boolean genericTypesMatch = false;

                        for (int i = 0; i < keyGenericTypes.size(); i++) {
                            Class<?> genericType = keyGenericTypes.get(i);
                            Class<?> lookupGenericType = null;

                            if (lookupGenericTypes != null && lookupGenericTypes.size() >= keyGenericTypes.size())
                                lookupGenericType = lookupGenericTypes.get(i);

                            if (lookupGenericType != null && genericType.isAssignableFrom(lookupGenericType)) {
                                genericTypesMatch = true;
                                break;
                            } else {
                                genericTypesMatch = false;
                                break;
                            }
                        }

                        if (genericTypesMatch)
                            converterAdapter = (ConverterAdapter<T>) converterAdapters.get(key);
                    }
                }
            }
        }

        return converterAdapter;
    }
}
