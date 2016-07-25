package com.geemvc.cache;

/**
 * Created by Michael on 20.07.2016.
 */
public interface CacheEntry {
    CacheEntry build(Object value);

    Object get();
}
