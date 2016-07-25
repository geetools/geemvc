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

import com.geemvc.Char;
import com.geemvc.annotation.Adapter;
import com.geemvc.converter.BeanConverter;
import com.geemvc.converter.ConverterAdapter;
import com.geemvc.converter.ConverterContext;
import com.geemvc.converter.SimpleConverter;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Adapter
public class MapConverterAdapter implements ConverterAdapter<Map<Object, Object>> {
    @Inject
    protected Injector injector;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Map<Object, Object> fromStrings(List<String> values, ConverterContext ctx) {
        SimpleConverter simpleConverter = injector.getInstance(SimpleConverter.class);
        BeanConverter beanConverter = injector.getInstance(BeanConverter.class);

        Map returnValue = null;

        String name = ctx.name();
        Class<?> type = ctx.type();
        List<Class<?>> genericType = ctx.genericType();

        Class<?> mapKeyType = genericType.get(0);
        Class<?> mapValueType1 = genericType.get(1);
        Class<?> mapValueType2 = null;

        if (genericType.size() > 2)
            mapValueType2 = genericType.get(2);

        Class<?> mapValueType = mapValueType1;

        if (mapValueType1.isArray()) {
            mapValueType = mapValueType1.getComponentType();
        } else if (Collection.class.isAssignableFrom(mapValueType1)) {
            mapValueType = mapValueType2;
        }

        if (genericType.size() >= 2 && simpleConverter.canConvert(mapValueType)) {
            if (mapValueType1.isArray() || Collection.class.isAssignableFrom(mapValueType1)) {
                Map<Object, Collection<Object>> valueMap = SortedMap.class.isAssignableFrom(type) ? new TreeMap<>() : new LinkedHashMap<>();

                for (String val : values) {
                    int equalsPos = val.indexOf(Char.EQUALS);
                    Object mapKey = mapKeyType == String.class ? val.substring(0, equalsPos) : simpleConverter.fromString(val.substring(0, equalsPos), mapKeyType);

                    String mapValue = val.substring(equalsPos + 1);

                    Collection<Object> innerValues = valueMap.get(mapKey);

                    if (innerValues == null) {
                        innerValues = SortedSet.class.isAssignableFrom(mapValueType1) ? new TreeSet<>() : Set.class.isAssignableFrom(mapValueType1) ? new LinkedHashSet<>() : new ArrayList<>();
                        valueMap.put(mapKey, innerValues);
                    }

                    innerValues.add(mapValueType == String.class ? mapValue : simpleConverter.fromString(mapValue, mapValueType));
                }

                if (!mapValueType1.isArray()) {
                    returnValue = valueMap;
                } else {
                    Map arrValueMap = SortedMap.class.isAssignableFrom(type) ? new TreeMap<>() : new LinkedHashMap<>();

                    Set<Object> keys = valueMap.keySet();

                    for (Object key : keys) {
                        Collection<Object> innerValues = valueMap.get(key);

                        Object arr = Array.newInstance(mapValueType, innerValues.size());

                        int x = 0;
                        for (Object o : innerValues) {
                            Array.set(arr, x++, o);
                        }

                        arrValueMap.put(key, arr);
                    }

                    returnValue = arrValueMap;
                }
            } else {
                Map<Object, Object> valueMap = SortedMap.class.isAssignableFrom(type) ? new TreeMap<>() : new LinkedHashMap<>();

                for (String val : values) {
                    int equalsPos = val.indexOf(Char.EQUALS);
                    Object mapKey = mapKeyType == String.class ? val.substring(0, equalsPos) : simpleConverter.fromString(val.substring(0, equalsPos), mapKeyType);
                    Object mapValue = mapValueType == String.class ? val.substring(equalsPos + 1) : simpleConverter.fromString(val.substring(equalsPos + 1), mapValueType);

                    valueMap.put(mapKey, mapValue);
                }

                returnValue = valueMap;
            }

        } else if (genericType.size() >= 2) {
            Map valueMap = null;

            if (mapValueType1.isArray() || Collection.class.isAssignableFrom(mapValueType1)) {
                Map<Object, Object> beanColValueMap = SortedMap.class.isAssignableFrom(type) ? new TreeMap<>() : new LinkedHashMap<>();
                Set<String> mapKeys = mapKeys(values);

                for (String mapKey : mapKeys) {
                    Object typedMapKey = mapKeyType == String.class ? mapKey : simpleConverter.fromString(mapKey, mapKeyType);
                    Set<Integer> listPositions = listPositions(values, mapKey);

                    List<Object> valueList = new ArrayList<>();

                    for (Integer index : listPositions) {
                        Object bean = beanConverter.fromStrings(values, name, mapValueType, mapKey, index);
                        valueList.add(bean);
                    }

                    if (mapValueType1.isArray()) {
                        Object arr = Array.newInstance(mapValueType, valueList.size());

                        for (int i = 0; i < valueList.size(); i++) {
                            Array.set(arr, i, valueList.get(i));
                        }

                        beanColValueMap.put(typedMapKey, arr);
                    } else {
                        beanColValueMap.put(typedMapKey, valueList);
                    }
                }

                valueMap = beanColValueMap;
            } else {
                Map<Object, Object> beanValueMap = SortedMap.class.isAssignableFrom(type) ? new TreeMap<>() : new LinkedHashMap<>();
                Set<String> mapKeys = mapKeys(values);

                for (String mapKey : mapKeys) {
                    Object bean = beanConverter.fromStrings(values, name, mapValueType, mapKey);
                    Object typedMapKey = mapKeyType == String.class ? mapKey : simpleConverter.fromString(mapKey, mapKeyType);

                    beanValueMap.put(typedMapKey, bean);
                }

                valueMap = beanValueMap;
            }

            returnValue = valueMap;
        } else {
            // TODO: HANDLE ERROR ... MUST PROVIDE GENERIC INFORMATION!
        }

        return returnValue;
    }

    @Override
    public String toString(Map<Object, Object> value) {
        return null;
    }

    protected Set<String> mapKeys(List<String> value) {
        if (value == null || value.size() == 0)
            return null;

        Set<String> mapKeys = new TreeSet<>();

        for (String val : value) {
            int equalsPos = val.indexOf(Char.EQUALS);
            String propertyExpression = val.substring(0, equalsPos);

            int openSquareBrackenPos = val.indexOf(Char.SQUARE_BRACKET_OPEN);
            int closeSquareBrackenPos = val.indexOf(Char.SQUARE_BRACKET_CLOSE);

            mapKeys.add(propertyExpression.substring(openSquareBrackenPos + 1, closeSquareBrackenPos));
        }

        return mapKeys;
    }

    protected Set<Integer> listPositions(List<String> value, String forMapKey) {
        if (value == null || value.size() == 0)
            return null;

        Set<Integer> listPositions = new TreeSet<>();

        for (String val : value) {
            int equalsPos = val.indexOf(Char.EQUALS);
            String propertyExpression = val.substring(0, equalsPos);

            int closeSquareBrackenPos = val.indexOf(Char.SQUARE_BRACKET_CLOSE);

            int openSquareBrackenPos2 = val.indexOf(Char.SQUARE_BRACKET_OPEN, closeSquareBrackenPos + 1);
            int closeSquareBrackenPos2 = val.indexOf(Char.SQUARE_BRACKET_CLOSE, closeSquareBrackenPos + 1);

            listPositions.add(Integer.valueOf(propertyExpression.substring(openSquareBrackenPos2 + 1, closeSquareBrackenPos2)));
        }

        return listPositions;
    }
}
