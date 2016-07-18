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

package com.cb.geemvc.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultCache implements Cache {
    protected static final Map<Object, com.google.common.cache.Cache> caches = new ConcurrentHashMap<>();

    protected static final String DEFAULT_CACHE_NAME = "__DEFAULT_CACHE";

    @Override
    public void put(Object key, Object value) {
        cache(DEFAULT_CACHE_NAME).put(key, value);
    }

    @Override
    public void put(Object cacheKey, Object key, Object value) {
        cache(cacheKey).put(key, value);
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        cache(DEFAULT_CACHE_NAME).put(key, value);
        return true;
    }

    @Override
    public Object putIfAbsent(Object cacheKey, Object key, Object value) {
        cache(cacheKey).put(key, value);
        return true;
    }

    @Override
    public Object get(Object key) {
        return cache(DEFAULT_CACHE_NAME).getIfPresent(key);
    }

    @Override
    public Object get(Object cacheKey, Object key) {
        return cache(cacheKey).getIfPresent(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return cache(DEFAULT_CACHE_NAME).getIfPresent(key) != null;
    }

    @Override
    public boolean containsKey(Object cacheKey, Object key) {
        return cache(cacheKey).getIfPresent(key) != null;
    }

    @Override
    public void remove(Object key) {
        cache(DEFAULT_CACHE_NAME).invalidate(key);
    }

    @Override
    public void remove(Object cacheKey, Object key) {
        cache(cacheKey).invalidate(key);
    }

    @Override
    public void clear() {
        cache(DEFAULT_CACHE_NAME).invalidateAll();
    }

    @Override
    public void clear(Object cacheKey) {
        cache(cacheKey).invalidateAll();
    }

    protected com.google.common.cache.Cache cache(Object cacheKey) {
//        org.cache2k.Cache<Object, Object> cache = caches.get(cacheKey);

        com.google.common.cache.Cache<Object, Object> cache = caches.get(cacheKey);

        if (cache == null) {
            com.google.common.cache.Cache<Object, Object> newCache = com.google.common.cache.CacheBuilder.newBuilder()
                    .maximumSize(100000).build();

/*
            org.cache2k.Cache<Object, Object> newCache =
                    CacheBuilder.newCache(Object.class, Object.class)
                            .eternal(true)
                            .build();

            org.cache2k.Cache<Object, Object> newCache = Cache2kBuilder.of(Object.class, Object.class)
                    .name(cacheKey.toString())
                    .entryCapacity(100000)
                    .eternal(true)
                    .build();
*/
            cache = caches.putIfAbsent(cacheKey, newCache);

            if (cache == null)
                cache = newCache;
        }

        return cache;
    }
}