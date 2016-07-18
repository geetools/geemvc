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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.cb.geemvc.Char;
import com.cb.geemvc.annotation.Adapter;
import com.cb.geemvc.converter.BeanConverter;
import com.cb.geemvc.converter.ConverterAdapter;
import com.cb.geemvc.converter.ConverterContext;
import com.cb.geemvc.converter.SimpleConverter;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Adapter
public class CollectionConverterAdapter implements ConverterAdapter<Collection<Object>> {
    @Inject
    Injector injector;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Collection<Object> fromStrings(List<String> values, ConverterContext ctx) {
        SimpleConverter simpleConverter = injector.getInstance(SimpleConverter.class);
        BeanConverter beanConverter = injector.getInstance(BeanConverter.class);

        Collection returnValue = null;

        String name = ctx.name();
        Class<?> type = ctx.type();
        List<Class<?>> genericType = ctx.genericType();

        if (Set.class.isAssignableFrom(type)) {
            if (genericType.size() == 1 && genericType.get(0) == String.class) {
                returnValue = SortedSet.class.isAssignableFrom(type) ? new TreeSet<>(values) : new LinkedHashSet<>(values);
            } else {
                Set<Object> valueSet = SortedSet.class.isAssignableFrom(type) ? new TreeSet<>() : new LinkedHashSet<>();
                Class<?> setType = genericType.get(0);

                for (String val : values) {
                    if (simpleConverter.canConvert(setType)) {
                        Object typedVal = simpleConverter.fromString(val, setType);
                        valueSet.add(typedVal);
                    } else {
                        Object bean = beanConverter.fromStrings(values, name, type);
                        valueSet.add(bean);
                    }
                }

                returnValue = valueSet;
            }
        } else {
            if (genericType.size() == 1 && genericType.get(0) == String.class) {
                returnValue = new ArrayList<>(values);
            } else {
                List<Object> valueList = new ArrayList<>();
                Class<?> listType = genericType.get(0);

                if (simpleConverter.canConvert(listType)) {
                    for (String val : values) {
                        Object typedVal = simpleConverter.fromString(val, listType);
                        valueList.add(typedVal);
                    }
                } else {
                    Set<Integer> beanPositions = beanPositions(values);

                    if (genericType.size() >= 3 && Map.class.isAssignableFrom(genericType.get(0))) {
                        for (Integer pos : beanPositions) {
                            Map<Object, Object> valueMap = SortedMap.class.isAssignableFrom(type) ? new TreeMap<>() : new LinkedHashMap<>();

                            Set<String> beanMapKeys = beanMapKeys(values, pos);

                            for (String mapKey : beanMapKeys) {
                                Class<?> mapKeyType = genericType.get(1);
                                Class<?> mapValueType = genericType.get(2);
                                Object typedMapKey = mapKeyType == String.class ? mapKey : simpleConverter.fromString(mapKey, mapKeyType);

                                if (simpleConverter.canConvert(mapValueType)) {
                                    for (String val : values) {
                                        int equalsPos = val.indexOf(Char.EQUALS);
                                        String propertyExpression = val.substring(0, equalsPos);

                                        if (propertyExpression.startsWith(new StringBuilder().append(Char.SQUARE_BRACKET_OPEN).append(pos).append(Char.SQUARE_BRACKET_CLOSE).append(Char.SQUARE_BRACKET_OPEN).append(mapKey)
                                                .append(Char.SQUARE_BRACKET_CLOSE).toString())) {
                                            String properyValue = val.substring(equalsPos + 1);
                                            valueMap.put(typedMapKey, mapValueType == String.class ? properyValue : simpleConverter.fromString(properyValue, mapValueType));
                                        }
                                    }
                                } else {
                                    Object bean = beanConverter.fromStrings(values, name, mapValueType, pos, mapKey);
                                    valueMap.put(typedMapKey, bean);
                                }
                            }

                            valueList.add(valueMap);
                        }
                    } else {
                        for (Integer pos : beanPositions) {
                            Object bean = beanConverter.fromStrings(values, name, genericType.get(0), pos);
                            valueList.add(bean);
                        }
                    }
                }

                returnValue = valueList;
            }
        }

        return returnValue;
    }

    @Override
    public String toString(Collection<Object> value) {
        return null;
    }

    protected Set<String> beanMapKeys(List<String> value, int positionInCollection) {
        if (value == null || value.size() == 0)
            return null;

        Set<String> mapKeys = new TreeSet<>();

        for (String val : value) {
            int equalsPos = val.indexOf(Char.EQUALS);
            String propertyExpression = val.substring(0, equalsPos);

            int closeSquareBrackenPos = val.indexOf(Char.SQUARE_BRACKET_CLOSE);

            int openSquareBrackenPos2 = val.indexOf(Char.SQUARE_BRACKET_OPEN, closeSquareBrackenPos + 1);
            int closeSquareBrackenPos2 = val.indexOf(Char.SQUARE_BRACKET_CLOSE, closeSquareBrackenPos + 1);

            mapKeys.add(propertyExpression.substring(openSquareBrackenPos2 + 1, closeSquareBrackenPos2));
        }

        return mapKeys;
    }

    protected Set<Integer> beanPositions(List<String> value) {
        if (value == null || value.size() == 0)
            return null;

        Set<Integer> beanPositions = new TreeSet<>();

        for (String val : value) {
            int equalsPos = val.indexOf(Char.EQUALS);
            String propertyExpression = val.substring(0, equalsPos);

            int openSquareBrackenPos = val.indexOf(Char.SQUARE_BRACKET_OPEN);
            int closeSquareBrackenPos = val.indexOf(Char.SQUARE_BRACKET_CLOSE);

            beanPositions.add(Integer.valueOf(propertyExpression.substring(openSquareBrackenPos + 1, closeSquareBrackenPos)));
        }

        return beanPositions;
    }
}
