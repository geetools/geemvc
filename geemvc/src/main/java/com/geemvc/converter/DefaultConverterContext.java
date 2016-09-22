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

package com.geemvc.converter;

import java.util.List;
import java.util.Map;

import com.geemvc.RequestContext;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.validation.Errors;

public class DefaultConverterContext implements ConverterContext {
    protected String name = null;
    protected Class<?> type = null;
    protected List<Class<?>> genericType = null;
    protected RequestContext requestCtx = null;
    protected Map<String, List<String>> requestValues = null;
    protected Errors errors = null;
    protected Notices notices = null;

    @Override
    public ConverterContext build(String name, Class<?> type, List<Class<?>> genericType, RequestContext requestCtx, Map<String, List<String>> requestValues, Errors errors, Notices notices) {
        this.name = name;
        this.type = type;
        this.genericType = genericType;
        this.requestCtx = requestCtx;
        this.requestValues = requestValues;
        this.errors = errors;
        this.notices = notices;
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

    @Override
    public Map<String, List<String>> requestValues() {
        return requestValues;
    }

    @Override
    public Errors errors() {
        return errors;
    }

    @Override
    public Notices notices() {
        return notices;
    }
}
