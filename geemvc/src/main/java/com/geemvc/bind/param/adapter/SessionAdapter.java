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
import com.geemvc.converter.ConverterContext;
import com.geemvc.converter.bean.BeanConverterAdapter;
import com.geemvc.converter.bean.BeanConverterAdapterFactory;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Adapter
public class SessionAdapter implements TypedParamAdapter<Session> {
    protected ParamAdapters paramAdapters;
    protected ReflectionProvider reflectionProvider;
    protected BeanConverterAdapterFactory beanConverterAdapterFactory;

    @Logger
    protected Log log;

    @Inject
    protected Injector injector;

    @Inject
    protected SessionAdapter(ParamAdapters paramAdapters, ReflectionProvider reflectionProvider, BeanConverterAdapterFactory beanConverterAdapterFactory) {
        this.paramAdapters = paramAdapters;
        this.reflectionProvider = reflectionProvider;
        this.beanConverterAdapterFactory = beanConverterAdapterFactory;
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

                    if (requestValues != null && !requestValues.isEmpty()) {
                        BeanConverterAdapter beanConverter = beanConverterAdapterFactory.create(type, null);

                        if (beanConverter != null) {
                            ConverterContext converterCtx = injector.getInstance(ConverterContext.class).build(paramName, type, null, paramCtx.requestCtx(), paramCtx.requestValues(), paramCtx.errors(), paramCtx.notices());
                            beanConverter.bindProperties(requestValues, paramName, value, converterCtx);
                        } else {
                            log.warn("Unable to find a compatible bean converter for the bean '{}' while attempting to bind values to the @Session({}) param.", type.getName(), paramName);
                        }
                    }

                    session.setAttribute(paramName, value);
                }
            }
        } else {
            List<String> requestValues = getValue(sessionParam, paramName, paramCtx);

            if (requestValues != null && !requestValues.isEmpty()) {
                BeanConverterAdapter beanConverter = beanConverterAdapterFactory.create(value.getClass(), null);

                if (beanConverter != null) {
                    ConverterContext converterCtx = injector.getInstance(ConverterContext.class).build(paramName, value.getClass(), null, paramCtx.requestCtx(), paramCtx.requestValues(), paramCtx.errors(), paramCtx.notices());
                    beanConverter.bindProperties(requestValues, paramName, value, converterCtx);
                } else {
                    log.warn("Unable to find a compatible bean converter for the bean '{}' while attempting to bind values to the @Session({}) param.", value.getClass().getName(), paramName);
                }
            }
        }

        return value;
    }

    @Override
    public List<String> getValue(Session sessionParam, String paramName, ParamContext paramCtx) {
        return paramAdapters.getRequestValues(paramName, paramCtx.requestCtx());
    }
}
