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

package com.geemvc.bind.param.adapter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.geemvc.annotation.Adapter;
import com.geemvc.bind.param.ParamAdapters;
import com.geemvc.bind.param.ParamContext;
import com.geemvc.bind.param.TypedParamAdapter;
import com.geemvc.bind.param.annotation.Session;
import com.geemvc.converter.BeanConverter;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Adapter
public class SessionAdapter implements TypedParamAdapter<Session> {
    protected ParamAdapters paramAdapters;
    protected ReflectionProvider reflectionProvider;
    protected BeanConverter beanConverter;

    @Inject
    protected Injector injector;

    @Inject
    protected SessionAdapter(ParamAdapters paramAdapters, ReflectionProvider reflectionProvider, BeanConverter beanConverter) {
        this.paramAdapters = paramAdapters;
        this.reflectionProvider = reflectionProvider;
        this.beanConverter = beanConverter;
    }

    @Override
    public String getName(Session paramAnnotation) {
        return paramAnnotation.value() == null ? paramAnnotation.name() : paramAnnotation.value();
    }

    @Override
    public Object getTypedValue(Session sessionParam, String paramName, ParamContext paramCtx) {
        HttpSession session = ((HttpServletRequest) paramCtx.requestCtx().getRequest()).getSession();

        Object value = session.getAttribute(paramName);

        if (value == null) {
            Class<?> type = reflectionProvider.getPrimaryType(paramCtx.methodParam().type(), paramCtx.methodParam().parameterizedType());

            if (!reflectionProvider.isSimpleType(type)) {
                value = injector.getInstance(type);

                if (value != null) {
                    List<String> requestValues = getValue(sessionParam, paramName, paramCtx);

                    if (requestValues != null && !requestValues.isEmpty())
                        beanConverter.bindProperties(requestValues, paramName, value);

                    session.setAttribute(paramName, value);
                }
            }
        } else {
            List<String> requestValues = getValue(sessionParam, paramName, paramCtx);

            if (requestValues != null && !requestValues.isEmpty())
                beanConverter.bindProperties(requestValues, paramName, value);
        }

        return value;
    }

    @Override
    public List<String> getValue(Session sessionParam, String paramName, ParamContext paramCtx) {
        return paramAdapters.getRequestValues(paramName, paramCtx.requestCtx());
    }
}
