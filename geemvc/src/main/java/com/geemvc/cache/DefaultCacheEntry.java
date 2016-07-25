package com.geemvc.cache;

/**
 * Created by Michael on 20.07.2016.
 */
public class DefaultCacheEntry implements CacheEntry {
    protected Object value;

    @Override
    public CacheEntry build(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public Object get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultCacheEntry that = (DefaultCacheEntry) o;

        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
