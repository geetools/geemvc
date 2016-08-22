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

import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;

import com.geemvc.Char;

public class DefaultProviderKey<T> implements ProviderKey {
    protected Class<?> providerType = null;
    protected Class<?> providerImplType = null;
    protected Class<T> forType = null;
    protected Type forGenericType = null;
    protected MediaType forMediaType = null;
    protected int relevance;

    @Override
    public ProviderKey build(Class providerType, Class providerImplType, Class forType, Type forGenericType, MediaType forMediaType, int relevance) {
        this.providerType = providerType;
        this.providerImplType = providerImplType;
        this.forType = forType;
        this.forGenericType = forGenericType;
        this.forMediaType = forMediaType;
        this.relevance = relevance;

        return this;
    }

    @Override
    public Class<?> providerType() {
        return providerType;
    }

    @Override
    public Class<?> providerImplType() {
        return providerImplType;
    }

    @Override
    public Class<T> forType() {
        return forType;
    }

    @Override
    public Type forGenericType() {
        return forGenericType;
    }

    @Override
    public MediaType forMediaType() {
        return forMediaType;
    }

    @Override
    public int relevance() {
        return relevance;
    }

    @Override
    public int compareTo(Object o) {
        ProviderKey pk = (ProviderKey) o;

        if (this.equals(pk))
            return 0;

        return (pk.relevance() - relevance) < 0 ? -1 : 1;
    }

    @Override
    public int hashCode() {
        return asString(this).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return asString(this).equals(asString((ProviderKey) obj));
    }

    protected String asString(ProviderKey pk) {
        return new StringBuilder()
                .append(pk.providerType().getName())
                .append(Char.HASH)
                .append(pk.providerImplType().getName())
                .append(Char.HASH)
                .append(pk.forType().getName())
                .append(Char.HASH)
                .append(pk.forGenericType().getTypeName())
                .append(Char.HASH)
                .append(pk.forMediaType()).toString();
    }

    @Override
    public String toString() {
        return "DefaultProviderKey [providerType=" + providerType + ", providerImplType=" + providerImplType + ", forType=" + forType + ", forGenericType=" + forGenericType + ", forMediaType=" + forMediaType + ", relevance=" + relevance + "]";
    }
}
