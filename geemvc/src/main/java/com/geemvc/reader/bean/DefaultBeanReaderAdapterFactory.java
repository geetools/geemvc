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

package com.geemvc.reader.bean;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.geemvc.cache.Cache;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.reader.ReaderAdapterKey;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class DefaultBeanReaderAdapterFactory implements BeanReaderAdapterFactory {
    protected final ReflectionProvider reflectionProvider;

    protected static final String BEAN_READER_ADAPTERS_CACHE_KEY = "geemvc/beanReaderAdapters/%s";

    @Inject
    protected Cache cache;

    @Logger
    protected Log log;

    @Inject
    protected Injector inject;

    @Inject
    public DefaultBeanReaderAdapterFactory(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> BeanReaderAdapter<T> create(Class<T> type, Type parameterizedType) {
        final String typeAsStr = reflectionProvider.toString(type, parameterizedType);
        final String cacheKey = String.format(BEAN_READER_ADAPTERS_CACHE_KEY, typeAsStr);

        return (BeanReaderAdapter<T>) cache.get(DefaultBeanReaderAdapterFactory.class, cacheKey, () -> {
            BeanReaderAdapter<T> beanReaderAdapter = null;

            List<Class<?>> genericTypes = reflectionProvider.getGenericType(parameterizedType);

            Map<ReaderAdapterKey, BeanReaderAdapter<?>> beanReaderAdapters = reflectionProvider.locateBeanReaderAdapters();
            Set<ReaderAdapterKey> keys = beanReaderAdapters.keySet();

            // ------------------------------------------------------------------------------
            // First we try to find bean readers that have exact type and generic
            // types match.
            // ------------------------------------------------------------------------------

            for (ReaderAdapterKey key : keys) {
                ReaderAdapterKey lookupKey = inject.getInstance(ReaderAdapterKey.class).build(type, genericTypes);

                if (key.equals(lookupKey)) {
                    beanReaderAdapter = (BeanReaderAdapter<T>) beanReaderAdapters.get(key);
                    break;
                }
            }

            // ------------------------------------------------------------------------------
            // If no exact match could be found, we try to find bean readers where all
            // types are assignable but ignore generic types Object for now.
            // ------------------------------------------------------------------------------

            if (beanReaderAdapter == null) {
                for (ReaderAdapterKey key : keys) {
                    ReaderAdapterKey lookupKey = inject.getInstance(ReaderAdapterKey.class).build(type, genericTypes);

                    if (key.type() != Object.class && key.type() != Object[].class && key.type().isAssignableFrom(lookupKey.type())) {
                        if ((lookupKey.genericTypes() == null || lookupKey.genericTypes().size() == 0) && (key.genericTypes() == null || key.genericTypes().size() == 0)) {
                            beanReaderAdapter = (BeanReaderAdapter<T>) beanReaderAdapters.get(key);
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
                                beanReaderAdapter = (BeanReaderAdapter<T>) beanReaderAdapters.get(key);
                        }
                    }
                }
            }

            // ------------------------------------------------------------------------------
            // Same as above but now also include unspecific Object types.
            // ------------------------------------------------------------------------------

            if (beanReaderAdapter == null) {
                for (ReaderAdapterKey key : keys) {
                    ReaderAdapterKey lookupKey = inject.getInstance(ReaderAdapterKey.class).build(type, genericTypes);

                    if (key.type().isAssignableFrom(lookupKey.type())) {
                        List<Class<?>> keyGenericTypes = key.genericTypes();
                        List<Class<?>> lookupGenericTypes = lookupKey.genericTypes();

                        if ((lookupKey.genericTypes() == null || lookupKey.genericTypes().size() == 0) && (key.genericTypes() == null || key.genericTypes().size() == 0)) {
                            beanReaderAdapter = (BeanReaderAdapter<T>) beanReaderAdapters.get(key);
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
                                beanReaderAdapter = (BeanReaderAdapter<T>) beanReaderAdapters.get(key);
                        }
                    }
                }
            }

            if (beanReaderAdapter != null) {
                cache.put(DefaultBeanReaderAdapterFactory.class, cacheKey, beanReaderAdapter);
            } else {
                log.debug("Unable to find bean reader for type '{}'.", () -> typeAsStr);
            }

            final BeanReaderAdapter<T> logReaderAdapter = beanReaderAdapter;
            log.trace("Using bean reader '{}' for the type '{}'.", () -> logReaderAdapter, () -> typeAsStr);

            return beanReaderAdapter;
        });
    }
}
