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

package com.geemvc.rest.jaxrs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

public class DefaultMultivaluedMap<K, V> extends HashMap<K, List<V>> implements MultivaluedMap<K, V> {
    private static final long serialVersionUID = -5396504390408808161L;

    @Override
    public List<V> get(Object key) {
        return super.get(key);
    }

    @Override
    public List<V> put(K key, List<V> value) {
        return super.put(key, value);
    }

    @Override
    public List<V> remove(Object key) {
        return super.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> m) {
        super.putAll(m);
    }

    @Override
    public void putSingle(K key, V value) {
        List list = new ArrayList<>();
        list.add(value);

        put(key, list);
    }

    @Override
    public void add(K key, V value) {
        list(key).add(value);
    }

    @Override
    public V getFirst(K key) {
        List<V> l = get(key);

        return l == null || l.size() == 0 ? null : l.get(0);
    }

    protected List list(K key) {
        List list = get(key);

        if (list == null) {
            list = new ArrayList();
            put(key, list);
        }

        return list;
    }
}
