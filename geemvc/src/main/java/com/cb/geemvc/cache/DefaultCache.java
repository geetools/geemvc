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

import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class DefaultCache implements Cache {
    protected static final Map<Object, com.google.common.cache.Cache> caches = new ConcurrentHashMap<>();

    protected static final String DEFAULT_CACHE_NAME = "__DEFAULT_CACHE";

    @Inject
    protected Injector injector;

    @Override
    public void put(Object key, Object value) {
        cache(DEFAULT_CACHE_NAME).put(key, injector.getInstance(CacheEntry.class).build(value));
    }

    @Override
    public void put(Object cacheKey, Object key, Object value) {
        cache(cacheKey).put(key, injector.getInstance(CacheEntry.class).build(value));
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        cache(DEFAULT_CACHE_NAME).put(key, injector.getInstance(CacheEntry.class).build(value));
        return true;
    }

    @Override
    public Object putIfAbsent(Object cacheKey, Object key, Object value) {
        cache(cacheKey).put(key, injector.getInstance(CacheEntry.class).build(value));
        return true;
    }

    @Override
    public Object get(Object key) {
        CacheEntry entry = cache(DEFAULT_CACHE_NAME).getIfPresent(key);
        return entry == null ? null : entry.get();
    }

    @Override
    public Object get(Object key, Callable callable) {
        CacheEntry entry = null;
        try {
            entry = cache(DEFAULT_CACHE_NAME).get(key, wrap(callable));
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return entry == null ? null : entry.get();
    }

    @Override
    public Object get(Object cacheKey, Object key) {
        CacheEntry entry = cache(cacheKey).getIfPresent(key);
        return entry == null ? null : entry.get();
    }

    @Override
    public Object get(Object cacheKey, Object key, Callable callable) {
        CacheEntry entry = null;

        try {
            entry = cache(cacheKey).get(key, wrap(callable));
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return entry == null ? null : entry.get();
    }

    protected Callable wrap(Callable callable) {
        return () -> {
            try {
                final Object val = callable.call();
                return injector.getInstance(CacheEntry.class).build(val);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        };
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

    protected com.google.common.cache.Cache<Object, CacheEntry> cache(Object cacheKey) {
        com.google.common.cache.Cache<Object, CacheEntry> cache = caches.get(cacheKey);

        if (cache == null) {

            com.google.common.cache.Cache newCache =
                    CacheBuilder.newBuilder()
                            .maximumSize(100000)
                            .build();

            cache = caches.putIfAbsent(cacheKey, newCache);

            if (cache == null)
                cache = newCache;
        }

        return cache;
    }
}