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

package com.geemvc.converter.bean;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.geemvc.cache.Cache;
import com.geemvc.converter.ConverterAdapterKey;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class DefaultBeanConverterAdapterFactory implements BeanConverterAdapterFactory {
    protected final ReflectionProvider reflectionProvider;

    protected static final String BEAN_CONVERTER_ADAPTERS_CACHE_KEY = "geemvc/beanConverterAdapters/%s";

    @Inject
    protected Cache cache;

    @Logger
    protected Log log;

    @Inject
    protected Injector inject;

    @Inject
    public DefaultBeanConverterAdapterFactory(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> BeanConverterAdapter<T> create(Class<T> type, Type parameterizedType) {
        final String typeAsStr = reflectionProvider.toString(type, parameterizedType);
        final String cacheKey = String.format(BEAN_CONVERTER_ADAPTERS_CACHE_KEY, typeAsStr);

        return (BeanConverterAdapter<T>) cache.get(DefaultBeanConverterAdapterFactory.class, cacheKey, () -> {
            BeanConverterAdapter<T> beanConverterAdapter = null;

            List<Class<?>> genericTypes = reflectionProvider.getGenericType(parameterizedType);

            Map<ConverterAdapterKey, BeanConverterAdapter<?>> beanConverterAdapters = reflectionProvider.locateBeanConverterAdapters();
            Set<ConverterAdapterKey> keys = beanConverterAdapters.keySet();

            // ------------------------------------------------------------------------------
            // First we try to find bean converters that have exact type and generic
            // types match.
            // ------------------------------------------------------------------------------

            for (ConverterAdapterKey key : keys) {
                ConverterAdapterKey lookupKey = inject.getInstance(ConverterAdapterKey.class).build(type, genericTypes);

                if (key.equals(lookupKey)) {
                    beanConverterAdapter = (BeanConverterAdapter<T>) beanConverterAdapters.get(key);
                    break;
                }
            }

            // ------------------------------------------------------------------------------
            // If no exact match could be found, we try to find bean converters where all
            // types are assignable but ignore generic types Object for now.
            // ------------------------------------------------------------------------------

            if (beanConverterAdapter == null) {
                for (ConverterAdapterKey key : keys) {
                    ConverterAdapterKey lookupKey = inject.getInstance(ConverterAdapterKey.class).build(type, genericTypes);

                    if (key.type() != Object.class && key.type() != Object[].class && key.type().isAssignableFrom(lookupKey.type())) {
                        if ((lookupKey.genericTypes() == null || lookupKey.genericTypes().size() == 0) && (key.genericTypes() == null || key.genericTypes().size() == 0)) {
                            beanConverterAdapter = (BeanConverterAdapter<T>) beanConverterAdapters.get(key);
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
                                beanConverterAdapter = (BeanConverterAdapter<T>) beanConverterAdapters.get(key);
                        }
                    }
                }
            }

            // ------------------------------------------------------------------------------
            // Same as above but now also include unspecific Object types.
            // ------------------------------------------------------------------------------

            if (beanConverterAdapter == null) {
                for (ConverterAdapterKey key : keys) {
                    ConverterAdapterKey lookupKey = inject.getInstance(ConverterAdapterKey.class).build(type, genericTypes);

                    if (key.type().isAssignableFrom(lookupKey.type())) {
                        List<Class<?>> keyGenericTypes = key.genericTypes();
                        List<Class<?>> lookupGenericTypes = lookupKey.genericTypes();

                        if ((lookupKey.genericTypes() == null || lookupKey.genericTypes().size() == 0) && (key.genericTypes() == null || key.genericTypes().size() == 0)) {
                            beanConverterAdapter = (BeanConverterAdapter<T>) beanConverterAdapters.get(key);
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
                                beanConverterAdapter = (BeanConverterAdapter<T>) beanConverterAdapters.get(key);
                        }
                    }
                }
            }

            if (beanConverterAdapter != null) {
                cache.put(DefaultBeanConverterAdapterFactory.class, cacheKey, beanConverterAdapter);
            } else {
                log.debug("Unable to find bean converter for type '{}'.", () -> typeAsStr);
            }

            final BeanConverterAdapter<T> logConverterAdapter = beanConverterAdapter;
            log.trace("Using bean converter '{}' for the type '{}'.", () -> logConverterAdapter, () -> typeAsStr);
            
            return beanConverterAdapter;
        });
    }
}
