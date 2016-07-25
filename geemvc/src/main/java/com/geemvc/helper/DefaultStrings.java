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

package com.geemvc.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.geemvc.Char;
import com.google.inject.Singleton;

@Singleton
public class DefaultStrings implements Strings {
    @Override
    public Map<String, ?> keysToLowerCase(Map<String, ?> map) {
        if (map == null)
            return null;

        Map<String, Object> newMap = new LinkedHashMap<>();

        Set<String> keys = map.keySet();

        for (String key : keys) {
            newMap.put(key.toLowerCase(), map.get(key));
        }

        return newMap;
    }

    @Override
    public Set<String> lowerCaseKeys(Map<String, ?> map) {
        if (map == null)
            return null;

        Set<String> newKeys = new LinkedHashSet<>();

        Set<String> keys = map.keySet();

        for (String key : keys) {
            newKeys.add(key.toLowerCase());
        }

        return newKeys;
    }

    @Override
    public String[] keysToLowerCase(String[] keyValuePairs) {
        if (keyValuePairs == null)
            return null;

        List<String> newKeyValuePairs = new ArrayList<>();

        for (String keyValuePair : keyValuePairs) {
            int equalsPos = keyValuePair.indexOf(Char.EQUALS);

            if (equalsPos == -1) {
                newKeyValuePairs.add(keyValuePair.trim().toLowerCase());
            } else {
                newKeyValuePairs.add(new StringBuilder(keyValuePair.substring(0, equalsPos).trim().toLowerCase()).append(Char.EQUALS).append(keyValuePair.substring(equalsPos + 1).trim()).toString());
            }
        }

        return newKeyValuePairs.toArray(new String[newKeyValuePairs.size()]);
    }
}
