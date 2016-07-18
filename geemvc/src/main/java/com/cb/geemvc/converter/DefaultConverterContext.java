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

package com.cb.geemvc.converter;

import java.util.List;

import com.cb.geemvc.RequestContext;

public class DefaultConverterContext implements ConverterContext {
    protected String name = null;
    protected Class<?> type = null;
    protected List<Class<?>> genericType = null;
    protected RequestContext requestCtx = null;

    @Override
    public ConverterContext build(Class<?> type, List<Class<?>> genericType) {
        this.type = type;
        this.genericType = genericType;
        return this;
    }

    @Override
    public ConverterContext build(String name, Class<?> type, List<Class<?>> genericType) {
        this.name = name;
        this.type = type;
        this.genericType = genericType;
        return this;
    }

    @Override
    public ConverterContext build(Class<?> type, List<Class<?>> genericType, RequestContext requestCtx) {
        this.type = type;
        this.genericType = genericType;
        this.requestCtx = requestCtx;
        return this;
    }

    @Override
    public ConverterContext build(String name, Class<?> type, List<Class<?>> genericType, RequestContext requestCtx) {
        this.name = name;
        this.type = type;
        this.genericType = genericType;
        this.requestCtx = requestCtx;
        return this;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<?> type() {
        return type;
    }

    @Override
    public List<Class<?>> genericType() {
        return genericType;
    }

    @Override
    public RequestContext requestCtx() {
        return requestCtx;
    }
}
