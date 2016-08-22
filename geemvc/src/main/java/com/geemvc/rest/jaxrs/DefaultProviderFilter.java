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

package com.geemvc.rest.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Created by Michael on 16.08.2016.
 */
@Singleton
public class DefaultProviderFilter implements ProviderFilter {

    protected final ReflectionProvider reflectionProvider;

    @Inject
    protected Injector injector;

    @Inject
    public DefaultProviderFilter(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
    }

    @Override
    public Object mostRelevantProvider(Class<?> providerType, Class<?> forType, Type forGenericType, MediaType forMediaType, List<?> providers) {
        Map<ProviderKey, Object> providerMap = new TreeMap<>();

        if (forMediaType == null)
            forMediaType = MediaType.WILDCARD_TYPE;

        List<Class<?>> targetGenericTypeList = targetGenericType(forType, forGenericType);

        for (Object provider : providers) {
            if (providerType.isAssignableFrom(provider.getClass())) {
                List<Class<?>> providerGenericTypeList = providerGenericType(provider, providerType);

                int relevance = relevanceScore(targetGenericTypeList, providerGenericTypeList);

                if (relevance != -1) {
                    ProviderKey pk = injector.getInstance(ProviderKey.class).build(providerType, provider.getClass(), forType, forGenericType, forMediaType, relevance);
                    providerMap.put(pk, provider);
                }
            }
        }

        return closestMediaType(providerMap);
    }

    @Override
    public ContextResolver<?> mostRelevantContextResolver(Class<?> forType, Type forGenericType, MediaType forMediaType, List<ContextResolver> providers) {
        Map<ProviderKey, ContextResolver<?>> providerMap = new TreeMap<>();

        if (forMediaType == null)
            forMediaType = MediaType.WILDCARD_TYPE;

        List<Class<?>> targetGenericTypeList = targetGenericType(forType, forGenericType);

        for (Object provider : providers) {
            if (ContextResolver.class.isAssignableFrom(provider.getClass())) {
                Set<String> providerMediaTypes = mediaTypes(provider.getClass().getAnnotationsByType(Produces.class));

                if (isCompatible(providerMediaTypes, forMediaType)) {
                    List<Class<?>> providerGenericTypeList = providerGenericType(provider, ContextResolver.class);

                    int relevance = relevanceScore(targetGenericTypeList, providerGenericTypeList);

                    if (relevance != -1) {
                        ProviderKey pk = injector.getInstance(ProviderKey.class).build(ContextResolver.class, provider.getClass(), forType, forGenericType, forMediaType, relevance);
                        providerMap.put(pk, (ContextResolver<?>) provider);
                    }
                }
            }
        }

        if (providerMap.isEmpty()) {
            return null;
        } else if (providerMap.size() == 1) {
            return providerMap.values().iterator().next();
        } else {
            List<ContextResolver<?>> sortedContextResolvers = sortedList(providerMap, forMediaType);

            for (ContextResolver<?> contextResolver : sortedContextResolvers) {
                if (contextResolver.getContext(forType) != null) {
                    return contextResolver;
                }
            }
        }

        return null;
    }

    protected List<ContextResolver<?>> sortedList(Map<ProviderKey, ContextResolver<?>> providerMap, MediaType targetMediaType) {
        List<ContextResolver<?>> contextResolvers = new ArrayList<>(providerMap.values());

        Collections.sort(contextResolvers, new Comparator<ContextResolver<?>>() {
            public int compare(ContextResolver<?> cr1, ContextResolver<?> cr2) {

                Set<String> providerMediaTypes1 = mediaTypes(cr1.getClass().getAnnotationsByType(Produces.class));
                Set<String> providerMediaTypes2 = mediaTypes(cr2.getClass().getAnnotationsByType(Produces.class));

                int score1 = relevanceScore(providerMediaTypes1, targetMediaType);
                int score2 = relevanceScore(providerMediaTypes2, targetMediaType);

                if (score1 == score2)
                    return 0;

                return (score2 - score1) < 0 ? -1 : 1;
            }
        });

        return contextResolvers;
    }

    @Override
    public Constructor<?> mostRelevantConstructor(Class<?> type) {
        Constructor[] constructors = type.getConstructors();
        int maxNumContextParameters = 0;
        Constructor mostRelevantConstructor = null;

        for (Constructor constructor : constructors) {
            int count = numContextParameters(constructor);

            if (count > maxNumContextParameters) {
                maxNumContextParameters = count;
                mostRelevantConstructor = constructor;
            }
        }

        return mostRelevantConstructor;
    }

    protected int numContextParameters(Constructor constructor) {
        Parameter[] parameters = constructor.getParameters();

        int count = 0;
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(Context.class))
                count++;
        }

        return count;
    }

    protected int relevanceScore(Set<String> providerMediaTypes, MediaType targetMediaType) {
        int lastMediaTypeRelevance = 0;

        for (String providerMediaType : providerMediaTypes) {
            MediaType provMediaType = MediaType.valueOf(providerMediaType);

            if (lastMediaTypeRelevance < 3 && targetMediaType.isCompatible(provMediaType)) {
                // Exact match.
                if (targetMediaType.equals(provMediaType)) {
                    lastMediaTypeRelevance = 3;
                    // Cannot improve on exact match.
                    break;
                } else if (lastMediaTypeRelevance < 2 && !provMediaType.isWildcardType() && provMediaType.isWildcardSubtype()) {
                    lastMediaTypeRelevance = 2;
                } else if (lastMediaTypeRelevance < 1 && provMediaType.isWildcardType() && provMediaType.isWildcardSubtype()) {
                    lastMediaTypeRelevance = 1;
                }
            }
        }

        return lastMediaTypeRelevance;
    }

    protected boolean isCompatible(Set<String> providerMediaTypes, MediaType targetMediaType) {
        for (String providerMediaType : providerMediaTypes) {
            MediaType provMediaType = MediaType.valueOf(providerMediaType);

            if (targetMediaType.isCompatible(provMediaType)) {
                return true;
            }
        }

        return false;
    }

    protected Object closestMediaType(Map<ProviderKey, Object> providerMap) {
        Object bestMatch = null;
        int lastRelevance = 0;
        int lastMediaTypeRelevance = 0;

        for (Map.Entry<ProviderKey, Object> providerEntry : providerMap.entrySet()) {
            ProviderKey providerKey = providerEntry.getKey();
            Object provider = providerEntry.getValue();
            MediaType forMediaType = providerKey.forMediaType();
            Set<String> providerMediaTypes = null;

            if (providerKey.providerType() == MessageBodyReader.class)
                providerMediaTypes = mediaTypes(provider.getClass().getAnnotationsByType(Consumes.class));

            else if (providerKey.providerType() == MessageBodyWriter.class || providerKey.providerType() == ContextResolver.class)
                providerMediaTypes = mediaTypes(provider.getClass().getAnnotationsByType(Produces.class));

            if (bestMatch != null && providerKey.relevance() < lastRelevance)
                break;

            lastRelevance = providerKey.relevance();

            for (String providerMediaType : providerMediaTypes) {
                MediaType provMediaType = MediaType.valueOf(providerMediaType);

                if (lastMediaTypeRelevance < 3 && forMediaType.isCompatible(provMediaType)) {
                    // Exact match.
                    if (forMediaType.equals(provMediaType)) {
                        bestMatch = provider;
                        lastMediaTypeRelevance = 3;
                        // Cannot improve on exact match.
                        break;
                    } else if (lastMediaTypeRelevance < 2 && !provMediaType.isWildcardType() && provMediaType.isWildcardSubtype()) {
                        bestMatch = provider;
                        lastMediaTypeRelevance = 2;
                    } else if (lastMediaTypeRelevance < 1 && provMediaType.isWildcardType() && provMediaType.isWildcardSubtype()) {
                        bestMatch = provider;
                        lastMediaTypeRelevance = 1;
                    }
                }
            }
        }

        return bestMatch;
    }

    protected <T extends Annotation> Set<String> mediaTypes(T[] annotations) {
        Set<String> mediaTypes = new TreeSet<>(Collections.reverseOrder());

        if (annotations != null && annotations.length > 0) {
            if (annotations[0] instanceof Consumes) {
                for (T anno : annotations) {
                    Consumes consumes = (Consumes) anno;
                    mediaTypes.addAll(Arrays.asList(consumes.value()));
                }
            } else {
                for (T anno : annotations) {
                    Produces consumes = (Produces) anno;
                    mediaTypes.addAll(Arrays.asList(consumes.value()));
                }
            }
        } else {
            mediaTypes.add(MediaType.WILDCARD);
        }

        return mediaTypes;
    }

    protected int relevanceScore(List<Class<?>> targetGenericTypeList, List<Class<?>> providerGenericTypeList) {
        int relevance = 0;

        if (providerGenericTypeList.size() == 1 && providerGenericTypeList.get(0) == Object.class && targetGenericTypeList.get(0) != Object.class) {
            relevance = 1;
            return relevance;
        }

        // First try exact match.
        if (targetGenericTypeList.equals(providerGenericTypeList)) {
            relevance = numSuperClasses(providerGenericTypeList) + providerGenericTypeList.size();
            return relevance;
        }

        // Almost exact match
        if (targetGenericTypeList.size() == providerGenericTypeList.size()) {
            boolean allMatch = true;

            for (int i = 0; i < targetGenericTypeList.size(); i++) {
                if (!providerGenericTypeList.get(i).isAssignableFrom(targetGenericTypeList.get(i))) {
                    allMatch = false;
                    break;
                }

                relevance += numSuperClasses(providerGenericTypeList.get(i));

                if (providerGenericTypeList.get(i) != Object.class)
                    relevance += 1;
            }

            if (allMatch) {
                return relevance;
            }
        }

        if (providerGenericTypeList.size() == 1
                && targetGenericTypeList.size() > providerGenericTypeList.size()
                && providerGenericTypeList.get(0).isAssignableFrom(targetGenericTypeList.get(0))) {

            int numSuperClasses = numSuperClasses(providerGenericTypeList.get(0));

            relevance = numSuperClasses + 1;
            return relevance;
        }

        return -1;
    }

    protected int numSuperClasses(List<Class<?>> types) {
        int numSuperClasses = types.size();

        for (Class<?> type : types) {
            Class<?> superClass = null;

            while ((superClass = type.getSuperclass()) != null) {
                numSuperClasses++;
                type = superClass;
            }
        }

        return numSuperClasses;
    }

    protected int numSuperClasses(Class<?> type) {
        int numSuperClasses = 1;

        Class<?> superClass = null;

        while ((superClass = type.getSuperclass()) != null) {
            numSuperClasses++;
            type = superClass;
        }

        return numSuperClasses;
    }

    protected List<Class<?>> targetGenericType(Class<?> targetObjType, Type targetObjGenericType) {
        if (targetObjGenericType != null && targetObjGenericType instanceof ParameterizedType) {
            List<Class<?>> targetGenericType = new ArrayList<>(reflectionProvider.getGenericType(targetObjGenericType));
            targetGenericType.add(0, targetObjType);
            return targetGenericType;
        } else {
            List<Class<?>> targetGenericType = new ArrayList<>();
            targetGenericType.add(targetObjType);
            return targetGenericType;
        }
    }

    protected List<Class<?>> providerGenericType(Object provider, Class<?> providerType) {
        Class<?>[] interfaces = provider.getClass().getInterfaces();
        Type[] genericInterfaces = provider.getClass().getGenericInterfaces();

        List<Class<?>> genericType = null;

        for (int i = 0; i < interfaces.length; i++) {
            if (providerType == interfaces[i]) {
                genericType = reflectionProvider.getGenericType(genericInterfaces[i]);
                break;
            }
        }

        return genericType;
    }
}
